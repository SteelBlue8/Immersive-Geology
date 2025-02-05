package com.igteam.immersive_geology.common.block.tileentity;

import blusunrize.immersiveengineering.api.utils.shapes.CachedShapesWithTransform;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.generic.PoweredMultiblockTileEntity;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import com.google.common.collect.ImmutableSet;
import com.igteam.immersive_geology.ImmersiveGeology;
import com.igteam.immersive_geology.api.crafting.recipes.recipe.ReverberationRecipe;
import com.igteam.immersive_geology.api.materials.fluid.FluidEnum;
import com.igteam.immersive_geology.common.multiblocks.ReverberationFurnaceMultiblock;
import com.igteam.immersive_geology.core.registration.IGRegistrationHolder;
import com.igteam.immersive_geology.core.registration.IGTileTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

//Sorry to IE for using their internal classes, we should have used an API, and we'll maybe fix it later.
public class ReverberationFurnaceTileEntity extends PoweredMultiblockTileEntity<ReverberationFurnaceTileEntity, ReverberationRecipe> implements IIEInventory, IEBlockInterfaces.IActiveState, IEBlockInterfaces.IBlockOverlayText, IEBlockInterfaces.IInteractionObjectIE, IEBlockInterfaces.IProcessTile, IBlockBounds {
    private static final Set<BlockPos> redStonePos = ImmutableSet.of(
            new BlockPos(1, 0, 0)
    );
    private static final ImmutableSet<BlockPos> gasOutputs = ImmutableSet.of(new BlockPos(4, 11, 1), new BlockPos(4,11,4));

    public static HashMap<Item, Integer> fuelMap = new HashMap<>();
    private static CachedShapesWithTransform<BlockPos, Pair<Direction, Boolean>> SHAPES = CachedShapesWithTransform.createForMultiblock(ReverberationFurnaceTileEntity::getShape);
    public int FUEL_SLOT1 = 0, FUEL_SLOT2 = 1;
    public int OUTPUT_SLOT1 = 2, OUTPUT_SLOT2 = 3;
    public int INPUT_SLOT1 = 4, INPUT_SLOT2 = 5;
    protected FluidTank gasTank;
    protected NonNullList<ItemStack> inventory;
    private Logger log = ImmersiveGeology.getNewLogger();
    private int burntime[] = new int[2];
    private int maxBurntime = 100;
    private final LazyOptional<IFluidHandler> holder;
    private LazyOptional<IItemHandler> insertionHandler1,insertionHandler2;
    private final LazyOptional<IItemHandler> extractionHandler1, extractionHandler2;

    private static final BlockPos input1Pos = new BlockPos(1, 1, 1);
    private static final BlockPos input2Pos = new BlockPos(1, 1, 4);

    private static final BlockPos output1Pos = new BlockPos(0, 0, 1);
    private static final BlockPos output2Pos = new BlockPos(0, 0, 4);

    public ReverberationFurnaceTileEntity() {
        super(ReverberationFurnaceMultiblock.INSTANCE, 0, true, IGTileTypes.REV_FURNACE.get());
        this.inventory = NonNullList.withSize(6, ItemStack.EMPTY);
        burntime[0] = 0;
        burntime[1] = 0;
        gasTank = new FluidTank(1000);
        holder = LazyOptional.of(() -> gasTank);
        this.insertionHandler1 = this.registerConstantCap(new IEInventoryHandler(1, this.master(),INPUT_SLOT1, true, false){
            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
            {
                return insertionHandlerImpl(slot+4, stack, simulate);
            }
        });

        this.insertionHandler2 = this.registerConstantCap(new IEInventoryHandler(1, this.master(),INPUT_SLOT2, true, false){
            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
            {
                return insertionHandlerImpl(slot+5, stack, simulate);
            }
        });

        this.extractionHandler1 =  this.registerConstantCap(new IEInventoryHandler(1, this.master(), OUTPUT_SLOT1, false, true));
        this.extractionHandler2 =  this.registerConstantCap(new IEInventoryHandler(1, this.master(), OUTPUT_SLOT2, false, true));

    }
  public ItemStack insertionHandlerImpl(int slot, ItemStack stack, boolean simulate) {
        ReverberationFurnaceTileEntity master = (ReverberationFurnaceTileEntity) master(); //Need to manually tell the inserter to insert to Master tile only
        if (!stack.isEmpty()) {
            if (!master.isStackValid(slot, stack)) {
                return stack;
            } else {
                int offsetSlot = slot;
                ItemStack currentStack = (ItemStack)master.getInventory().get(offsetSlot);
                int accepted;
                if (currentStack.isEmpty()) {
                    accepted = Math.min(stack.getMaxStackSize(), master.getSlotLimit(offsetSlot));
                    if (accepted < stack.getCount()) {
                        stack = stack.copy();
                        if (!simulate) {
                            master.getInventory().set(offsetSlot, stack.split(accepted));
                            master.doGraphicalUpdates();
                        } else {
                            stack.shrink(accepted);
                        }

                        return stack;
                    } else {
                        if (!simulate) {
                            master.getInventory().set(offsetSlot, stack.copy());
                            master.doGraphicalUpdates();
                        }

                        return ItemStack.EMPTY;
                    }
                } else if (!ItemHandlerHelper.canItemStacksStack(stack, currentStack)) {
                    return stack;
                } else {
                    accepted = Math.min(stack.getMaxStackSize(), master.getSlotLimit(offsetSlot)) - currentStack.getCount();
                    ItemStack newStack;
                    if (accepted < stack.getCount()) {
                        stack = stack.copy();
                        if (!simulate) {
                            newStack = stack.split(accepted);
                            newStack.grow(currentStack.getCount());
                            master.getInventory().set(offsetSlot, newStack);
                            master.doGraphicalUpdates();
                        } else {
                            stack.shrink(accepted);
                        }

                        return stack;
                    } else {
                        if (!simulate) {
                            newStack = stack.copy();
                            newStack.grow(currentStack.getCount());
                            master.getInventory().set(offsetSlot, newStack);
                            master.doGraphicalUpdates();
                        }

                        return ItemStack.EMPTY;
                    }
                }
            }
        } else {
            return stack;
        }
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && gasOutputs.contains(posInMultiblock) && facing == Direction.UP) {
            ReverberationFurnaceTileEntity master = master();
            if(master != null)
                return master.holder.cast();

            return LazyOptional.empty();
        }
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            ReverberationFurnaceTileEntity master = (ReverberationFurnaceTileEntity) this.master();
            if (master == null) {
                return LazyOptional.empty();
            }

            if (output1Pos.equals(this.posInMultiblock))
            {
                return master.extractionHandler1.cast();
            }

            if (output2Pos.equals(this.posInMultiblock))
            {
                return master.extractionHandler2.cast();
            }

            if (input1Pos.equals(this.posInMultiblock)) {
                return master.insertionHandler1.cast();
            }
            if (input2Pos.equals(this.posInMultiblock)) {
                return master.insertionHandler2.cast();
            }

        }
        return super.getCapability(capability, facing);
    }

    private static List<AxisAlignedBB> getShape(BlockPos posInMultiblock) {
        final int bX = posInMultiblock.getX();
        final int bY = posInMultiblock.getY();
        final int bZ = posInMultiblock.getZ();
        if (bX < 3) {
            if (bY == 2) {
                if (bZ == 0) {
                    if (bX == 0) {
                        return Arrays.asList(new AxisAlignedBB(0.125, 0.0, 0.25, 1.0, 0.5, 1.0));
                    }
                    return Arrays.asList(new AxisAlignedBB(0.0, 0.0, 0.25, 1.0, 0.5, 1.0));
                }
                if (bZ == 2 || bZ == 3) {
                    if (bX == 0) {
                        return Arrays.asList(new AxisAlignedBB(0.125, 0.0, 0.0, 1.0, 0.5, 1.0));
                    }
                    return Arrays.asList(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0));

                }
                if (bZ == 5) {
                    if (bX == 0) {
                        return Arrays.asList(new AxisAlignedBB(0.125, 0.0, 0.0, 1.0, 0.5, 0.75));
                    }
                    return Arrays.asList(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 0.75));

                }
            }
            if (bY < 2) {
                if (bZ == 0) {
                    if (bX == 0) {
                        return Arrays.asList(new AxisAlignedBB(0.125, 0.0, 0.25, 1.0, 1.0, 1.0));
                    }
                    return Arrays.asList(new AxisAlignedBB(0.0, 0.0, 0.25, 1.0, 1.0, 1.0));

                }
                if (bZ == 5) {
                    if (bX == 0) {
                        return Arrays.asList(new AxisAlignedBB(0.125, 0.0, 0.0, 1.0, 1.0, 0.75));
                    }
                    return Arrays.asList(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.75));
                }

                if (bX == 0) {
                    if (bY == 0 && (bZ == 1 || bZ == 4)) {
                        return Arrays.asList(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0));
                    }
                    return Arrays.asList(new AxisAlignedBB(0.125, 0.0, 0.0, 1.0, 1.0, 1.0));
                }

            }
        }
        if (bY >= 6 && bY < 10) {
            if (bX == 3) {
                return Arrays.asList(new AxisAlignedBB(0.5, 0.0, 0.0, 1.0, 1.0, 1.0));
            }
            if (bX == 5) {
                return Arrays.asList(new AxisAlignedBB(0.0, 0.0, 0.0, 0.5, 1.0, 1.0));
            }
            if (bX == 4) {
                if (bZ == 5 || bZ == 2) {
                    return Arrays.asList(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.5));
                }
                if (bZ == 0 || bZ == 3) {
                    return Arrays.asList(new AxisAlignedBB(0.0, 0.0, 0.5, 1.0, 1.0, 1.0));

                }
            }
        }
        return Arrays.asList(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0));
    }

    public boolean canUseGui(PlayerEntity player) {
        return this.formed;
    }

    public IEBlockInterfaces.IInteractionObjectIE getGuiMaster() {
        return (IEBlockInterfaces.IInteractionObjectIE) this.master();
    }

    @Override
    public VoxelShape getBlockBounds(ISelectionContext ctx) {
        return SHAPES.get(this.posInMultiblock, Pair.of(getFacing(), getIsMirrored()));
    }

    @Nullable
    @Override
    protected ReverberationRecipe getRecipeForId(ResourceLocation id) {
        return ReverberationRecipe.recipes.get(id);
    }

    @Override
    public Set<BlockPos> getEnergyPos() {
        return null;
    }

    @Override
    public Set<BlockPos> getRedstonePos() {
        return redStonePos;
    }

    @Override
    public void tick() {
        ReverberationFurnaceTileEntity master = (ReverberationFurnaceTileEntity) this.master();
        assert master != null;

        checkForNeedlessTicking();

        if (world.isRemote || isDummy())
            return;

        super.tick();


        boolean update = false;

        for (int offset = 0; offset < 2; offset++) {
            if (!isDummy()) {
                //ask me not, but input binds are screwed
                if (master.isBurning(FUEL_SLOT1 + offset)) {
                    master.burntime[offset] = master.burntime[offset] - 1;
                } else if (hasFuel(FUEL_SLOT1 + offset)) {
                    master.burntime[offset] += fuelMap.get(master.getInventory().get(FUEL_SLOT1 + offset).getItem());
                    master.getInventory().get(FUEL_SLOT1 + offset).shrink(1);
                }
            }

            ItemStack inputItem = master.inventory.get(INPUT_SLOT1 + offset);
            if (!inputItem.isEmpty() && inputItem.getCount() > 0) {

                ReverberationRecipe recipe = ReverberationRecipe.findRecipe(inputItem);
                if (recipe != null) {
                    recipe.setSlotOffset(offset);
                    MultiblockProcessInMachine<ReverberationRecipe> process = new MultiblockProcessInMachine<ReverberationRecipe>(recipe, INPUT_SLOT1 + offset);
                    process.setInputAmounts(recipe.input.getCount());

                    if (master.addProcessToQueue(process, true, false)) {
                        update = master.addProcessToQueue(process, false, false);
                    }
                }
            }
        }

        if (master.gasTank.getFluidAmount() > 0 && gasOutputs.contains(posInMultiblock)) {
            FluidStack out = Utils.copyFluidStackWithAmount(master.gasTank.getFluid(), Math.min(master.gasTank.getFluidAmount(), 80), false);
            Direction fw = Direction.UP;
            Direction shift_1 =  this.getIsMirrored() ?  this.getFacing().rotateY() : this.getFacing().rotateYCCW();
            BlockPos outputPos1 = new BlockPos(4,11,4);
            BlockPos outputPos2 = new BlockPos(1,11,4);

            update |= (Boolean) FluidUtil.getFluidHandler(this.world, outputPos1,fw).map((output) -> {
                int accepted = output.fill(out, IFluidHandler.FluidAction.SIMULATE);

                if (accepted > 0) {
                    int drained = output.fill(Utils.copyFluidStackWithAmount(out, Math.min(out.getAmount(), accepted), false),
                            IFluidHandler.FluidAction.EXECUTE);
                    master.gasTank.drain(drained, IFluidHandler.FluidAction.EXECUTE);
                    return true;
                } else {
                    return false;
                }
            }).orElse(false);

            update |= (Boolean) FluidUtil.getFluidHandler(this.world, outputPos2,fw).map((output) -> {
                int accepted = output.fill(out, IFluidHandler.FluidAction.SIMULATE);

                if (accepted > 0) {
                    int drained = output.fill(Utils.copyFluidStackWithAmount(out, Math.min(out.getAmount(), accepted), false),
                            IFluidHandler.FluidAction.EXECUTE);
                    master.gasTank.drain(drained, IFluidHandler.FluidAction.EXECUTE);
                    return true;
                } else {
                    return false;
                }
            }).orElse(false);
        }

        if (update) {
            this.markDirty();
            this.markContainingBlockForUpdate(null);
        }
    }

    @Override
    public void readCustomNBT(CompoundNBT nbt, boolean descPacket) {
        super.readCustomNBT(nbt, descPacket);
        inventory = Utils.readInventory(nbt.getList("inventory", 10), 6);
        burntime[0] = nbt.getInt("burntime_1");
        burntime[1] = nbt.getInt("burntime_2");
        gasTank.readFromNBT(nbt.getCompound("gas_tank"));
    }

    @Override
    public void writeCustomNBT(CompoundNBT nbt, boolean descPacket) {
        super.writeCustomNBT(nbt, descPacket);
        nbt.put("inventory", Utils.writeInventory(inventory));
        nbt.putInt("burntime_1", burntime[0]);
        nbt.putInt("burntime_2", burntime[1]);
        nbt.put("gas_tank", gasTank.writeToNBT(new CompoundNBT()));
    }

    @Nullable
    @Override
    public IFluidTank[] getInternalTanks() {
        return new IFluidTank[]{gasTank};
    }

    @Nullable
    @Override
    public ReverberationRecipe findRecipeForInsertion(ItemStack itemStack) {
        return ReverberationRecipe.findRecipe(itemStack);
    }

    @Nullable
    @Override
    public int[] getOutputSlots() {
        return new int[]{OUTPUT_SLOT1, OUTPUT_SLOT2};
    }

    @Nullable
    @Override
    public int[] getOutputTanks() {
        return new int[]{0};
    }

    @Override
    public boolean additionalCanProcessCheck(MultiblockProcess multiblockProcess) {
        if (multiblockProcess.recipe instanceof ReverberationRecipe) {
            ReverberationRecipe r = (ReverberationRecipe) multiblockProcess.recipe;
            //WTF? -> (processQueue.get(r.getSlotOffset()).recipe.getId().equals(multiblockProcess.recipe.getId()) &&
            return isBurning(r.getSlotOffset());
        }
        return false;
    }

    @Override
    public void doProcessOutput(ItemStack itemStack) {

    }

    @Override
    public void doProcessFluidOutput(FluidStack fluidStack) {

    }

    @Override
    public void onProcessFinish(MultiblockProcess multiblockProcess) {
        if (multiblockProcess.recipe instanceof ReverberationRecipe) {
            ReverberationRecipe r = (ReverberationRecipe) multiblockProcess.recipe;
            int slotOffset = r.getSlotOffset();
            ReverberationFurnaceTileEntity master = this.master();
            if(master != null) {
                if (master.gasTank.getFluidAmount() < master.gasTank.getCapacity()) {
                    master.gasTank.fill(new FluidStack(IGRegistrationHolder.getFluidByMaterial(FluidEnum.SulfurDioxide.getMaterial(), false), Math.round(50 * r.getWasteMultipler())), IFluidHandler.FluidAction.EXECUTE);
                }
            }
        }
    }

    @Override
    public int getMaxProcessPerTick() {
        return 2;
    }

    @Override
    public int getProcessQueueMaxLength() {
        int size = 0;
        if ((!master().inventory.get(INPUT_SLOT1).isEmpty())) {
            size++;
        }
        if ((!master().inventory.get(INPUT_SLOT2).isEmpty())) {
            size++;
        }
        return size;
    }

    @Override
    public float getMinProcessDistance(MultiblockProcess multiblockProcess) {
        return 0;
    }

    @Override
    public boolean isInWorldProcessingMachine() {
        return true;
    }

    public boolean isBurning(int slot) {
        ReverberationFurnaceTileEntity master = (ReverberationFurnaceTileEntity) this.master();
        assert master != null;
        if(master.burntime.length > slot) {
            return master.burntime[slot] > 0;
        } else {
            return false;
        }
    }

    public boolean hasFuel(int slot) {
        ReverberationFurnaceTileEntity master = (ReverberationFurnaceTileEntity) this.master();
        assert master != null;
        return fuelMap.containsKey(master.inventory.get(slot).getItem());
    }

    @Nonnull
    @Override
    protected IFluidTank[] getAccessibleFluidTanks(Direction direction) {
        if(gasOutputs.contains(posInMultiblock) && (direction == null || direction == Direction.UP)) {
            return new IFluidTank[]{ gasTank };
        }

        return new FluidTank[0];
    }



    @Override
    protected boolean canFillTankFrom(int i, Direction direction, FluidStack fluidStack) {
        return false;
    }

    @Override
    protected boolean canDrainTankFrom(int i, Direction side) {
        return gasOutputs.contains(posInMultiblock) && (side == null || side == Direction.UP);
    }

    @Nonnull
    @Override
    public int[] getCurrentProcessesStep() {
        return new int[0];
    }

    @Nonnull
    @Override
    public int[] getCurrentProcessesMax() {
        return new int[0];
    }

    @Nullable
    @Override
    public NonNullList<ItemStack> getInventory() {
        return inventory;
    }

    @Override
    public boolean isStackValid(int i, ItemStack itemStack) {
        if (i == INPUT_SLOT1 || i == INPUT_SLOT2)
        {
            return (ReverberationRecipe.findRecipe(itemStack) != null);
        }
        if (i == FUEL_SLOT1 || i == FUEL_SLOT2)
        {
            return fuelMap.containsKey(itemStack.getItem());

        }
        return false;
    }

    @Override
    public int getSlotLimit(int i) {
        return 64;
    }

    @Override
    public void doGraphicalUpdates() {

    }

    @Nonnull
    @Override
    public TileEntityType<?> getType() {
        return IGTileTypes.REV_FURNACE.get();
    }

    @Nullable
    @Override
    public ITextComponent[] getOverlayText(PlayerEntity playerEntity, RayTraceResult rayTraceResult, boolean b) {
        ReverberationFurnaceTileEntity master = (ReverberationFurnaceTileEntity) this.master();
        assert master != null;

        ArrayList<StringTextComponent> info = new ArrayList<>();
        for (int offset = 0; offset < 2; offset++) {
            assert master.getInventory() != null;
            String FuelName = master.getInventory().get(FUEL_SLOT1 + offset).getDisplayName().getString();
            String InputName = master.getInventory().get(INPUT_SLOT1 + offset).getDisplayName().getString();
            String OutputName = master.getInventory().get(OUTPUT_SLOT1 + offset).getDisplayName().getString();

            StringTextComponent FuelNames = new StringTextComponent("Fuel Slot[" + offset + "]: " + FuelName + " x" + master.getInventory().get(FUEL_SLOT1 + offset).getCount());
            StringTextComponent InputNames = new StringTextComponent("Input Slot[" + offset + "]: " + InputName + " x" + master.getInventory().get(INPUT_SLOT1 + offset).getCount());
            StringTextComponent OutputNames = new StringTextComponent("Output Slot[" + offset + "]: " + OutputName + " x" + master.getInventory().get(OUTPUT_SLOT1 + offset).getCount());

            info.add(FuelNames);
            info.add(InputNames);
            info.add(OutputNames);
        }

        String TankOutputName = Objects.requireNonNull(master.getInternalTanks())[0].getFluid().getDisplayName().getString();

        info.add(new StringTextComponent("Gas Output: " + TankOutputName + " x" + master.getInternalTanks()[0].getFluidAmount()));

        if(master.burntime.length > FUEL_SLOT1) {
            info.add(new StringTextComponent("Burn Time[1]: " + master.burntime[FUEL_SLOT1]));
        }
        if(master.burntime.length > FUEL_SLOT2) {
            info.add(new StringTextComponent("Burn Time[2]: " + master.burntime[FUEL_SLOT2]));
        }

        info.add(new StringTextComponent("Pos in MB: " + posInMultiblock.toString()));

        return info.toArray(new ITextComponent[info.size()]);
    }

    @Override
    public boolean useNixieFont(PlayerEntity playerEntity, RayTraceResult rayTraceResult) {
        return false;
    }
}