package com.igteam.immersive_geology.common.block.tileentity;

import blusunrize.immersiveengineering.api.fluid.FluidUtils;
import blusunrize.immersiveengineering.api.utils.CapabilityReference;
import blusunrize.immersiveengineering.api.utils.DirectionalBlockPos;
import blusunrize.immersiveengineering.api.utils.shapes.CachedShapesWithTransform;
import blusunrize.immersiveengineering.client.utils.TextUtils;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.generic.PoweredMultiblockTileEntity;
import blusunrize.immersiveengineering.common.blocks.metal.MetalPressTileEntity;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import com.google.common.collect.ImmutableSet;
import com.igteam.immersive_geology.api.crafting.recipes.recipe.VatRecipe;
import com.igteam.immersive_geology.common.multiblocks.ChemicalVatMultiblock;
import com.igteam.immersive_geology.core.registration.IGTileTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

//Sorry to IE for using their internal classes, we should have used an API, and we'll maybe fix it later.
public class ChemicalVatTileEntity extends PoweredMultiblockTileEntity<ChemicalVatTileEntity, VatRecipe> implements IEBlockInterfaces.IBlockOverlayText, IEBlockInterfaces.IPlayerInteraction, IBlockBounds, IIEInventory {

    public static final int OUTPUT_EMPTY = 4;
    public static final int OUTPUT_FILLED = 5;
    public FluidTank[] tanks = new FluidTank[]{
            new FluidTank(12* FluidAttributes.BUCKET_VOLUME),
            new FluidTank(12* FluidAttributes.BUCKET_VOLUME),
            new FluidTank(24* FluidAttributes.BUCKET_VOLUME)
    };
    public NonNullList<ItemStack> inventory;
    private LazyOptional<IItemHandler> insertionHandler;

    public ItemStack holdItem;

    public ChemicalVatTileEntity(){
        super(ChemicalVatMultiblock.INSTANCE, 16000, true, IGTileTypes.VAT.get());
        this.inventory = NonNullList.withSize(2, ItemStack.EMPTY);
        holdItem = ItemStack.EMPTY;

        this.insertionHandler = this.registerConstantCap(new IEInventoryHandler(1, this.master(), 0, true, false){
            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                ChemicalVatTileEntity master = (ChemicalVatTileEntity) master(); //Need to manually tell the inserter to insert to Master tile only
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
        });
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            ChemicalVatTileEntity master = (ChemicalVatTileEntity) this.master();
            if (master == null) {
                return LazyOptional.empty();
            }

            return this.insertionHandler.cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void readCustomNBT(CompoundNBT nbt, boolean descPacket)
    {
        super.readCustomNBT(nbt, descPacket);
        tanks[0].readFromNBT(nbt.getCompound("tank_input_1"));
        tanks[1].readFromNBT(nbt.getCompound("tank_input_2"));
        tanks[2].readFromNBT(nbt.getCompound("tank_output"));
        inventory = Utils.readInventory(nbt.getList("inventory", 10), 2);
    }

    @Override
    public void writeCustomNBT(CompoundNBT nbt, boolean descPacket)
    {
        super.writeCustomNBT(nbt, descPacket);
        nbt.put("tank_input_1", tanks[0].writeToNBT(new CompoundNBT()));
        nbt.put("tank_input_2", tanks[1].writeToNBT(new CompoundNBT()));
        nbt.put("tank_output", tanks[2].writeToNBT(new CompoundNBT()));
        nbt.put("inventory", Utils.writeInventory(inventory));
    }

    @Override
    public void tick()
    {
        super.tick();

        if(world.isRemote||isDummy())
            return;

        ChemicalVatTileEntity master = (ChemicalVatTileEntity) this.master();
        boolean update = false;
        if(master.energyStorage.getEnergyStored() > 0 && master.processQueue.size() < master.getProcessQueueMaxLength())
        {
            if(master.tanks[0].getFluidAmount() > 0 || master.tanks[1].getFluidAmount() > 0)
            {
                ItemStack inputStack = master.getInventory().get(0); //Input Item
                VatRecipe recipe = VatRecipe.findRecipe(inputStack, master.tanks[0].getFluid(), master.tanks[1].getFluid());
                if(recipe!=null)
                {
                    MultiblockProcessInMachine<VatRecipe> process = new MultiblockProcessInMachine<>(recipe)
                            .setInputTanks((master.tanks[0].getFluidAmount() > 0&&master.tanks[1].getFluidAmount() > 0)?new int[]{0, 1}: master.tanks[0].getFluidAmount() > 0?new int[]{0}: new int[]{1});
                    if(master.addProcessToQueue(process, true))
                    {
                        master.addProcessToQueue(process, false);
                        update = true;
                        System.out.println("Added Process to Queue!");
                    }
                }
            }
        }

        if(update)
        {
            this.markDirty();
            this.markContainingBlockForUpdate(null);
        }
    }

    private static final CachedShapesWithTransform<BlockPos, Pair<Direction, Boolean>> SHAPES =
            CachedShapesWithTransform.createForMultiblock(ChemicalVatTileEntity::getShape);

    @Override
    public VoxelShape getBlockBounds(@Nullable ISelectionContext ctx)
    {
        return getShape(SHAPES);
    }

    @Override
    public Set<BlockPos> getEnergyPos()
    {
        return ImmutableSet.of(
                new BlockPos(3, 1, 2)
        );
    }

    @Override
    public Set<BlockPos> getRedstonePos()
    {
        return ImmutableSet.of(
                new BlockPos(3, 1, 2)
        );
    }

    @Override
    public boolean isInWorldProcessingMachine()
    {
        return true;
    }

    @Override
    public boolean additionalCanProcessCheck(MultiblockProcess<VatRecipe> process)
    {
        ChemicalVatTileEntity master = (ChemicalVatTileEntity) master();
        return master.tanks[2].getFluidAmount() < master.tanks[2].getCapacity();
    }

    private CapabilityReference<IItemHandler> output = CapabilityReference.forTileEntityAt(this,
            () -> new DirectionalBlockPos(getPos().add(1, 0, 0).offset(getFacing(), -2), getFacing()),
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);

    @Override
    public void doProcessOutput(ItemStack output)
    {
        output = Utils.insertStackIntoInventory(this.output, output, false);
        if(!output.isEmpty())
            Utils.dropStackAtPos(world, getPos().add(1, 0, 0).offset(getFacing(), -2), output, getFacing().getOpposite());
    }

    @Override
    public void doProcessFluidOutput(FluidStack output)
    {

    }

    @Override
    public void onProcessFinish(MultiblockProcess<VatRecipe> process)
    {
        int primaryDrainAmount = process.recipe.getInputFluids()[0].getAmount();
        int secondaryDrainAmount = process.recipe.getInputFluids()[1].getAmount();
        int shrinkAmount = process.recipe.getItemInput().getCount();
        ChemicalVatTileEntity master = (ChemicalVatTileEntity) master();
        master.tanks[0].drain(primaryDrainAmount, IFluidHandler.FluidAction.EXECUTE);
        master.tanks[1].drain(secondaryDrainAmount, IFluidHandler.FluidAction.EXECUTE);
        master.getInventory().get(0).shrink(shrinkAmount);
    }

    @Override
    public int getMaxProcessPerTick()
    {
        return 1;
    }

    @Override
    public int getProcessQueueMaxLength()
    {
        return 16;
    }

    @Override
    public float getMinProcessDistance(MultiblockProcess<VatRecipe> process)
    {
        return 0;
    }


    @Override
    public NonNullList<ItemStack> getInventory()
    {
        return this.inventory;
    }

    @Override
    public boolean isStackValid(int slot, ItemStack stack)
    {
        return true;
    }

    @Override
    public int getSlotLimit(int i) {
        return 8;
    }

    @Override
    public IFluidTank[] getInternalTanks() {
        return tanks;
    }

    private static final BlockPos outputOffset = new BlockPos(1, 0, 2);
    private static final Set<BlockPos> inputPrimary = ImmutableSet.of(
            new BlockPos(3, 0, 0)
    );

    private static final Set<BlockPos> inputSecondary = ImmutableSet.of(
            new BlockPos(3, 0, 1)
    );

    @Override
    protected IFluidTank[] getAccessibleFluidTanks(Direction side)
    {
        ChemicalVatTileEntity master = this.master();
        if(master!=null)
        {
            if(outputOffset.equals(posInMultiblock)&&(side==null||side==getFacing().getOpposite()))
                return new FluidTank[]{master.tanks[2]};
            if(inputPrimary.contains(posInMultiblock)&&(side==null||side.getAxis()==getFacing().rotateYCCW().getAxis()))
                return new FluidTank[]{master.tanks[0]};
            if(inputSecondary.contains(posInMultiblock)&&(side==null||side.getAxis()==getFacing().rotateYCCW().getAxis()))
                return new FluidTank[]{master.tanks[1]};
        }
        return new FluidTank[0];
    }

    @Override
    protected boolean canFillTankFrom(int iTank, Direction side, FluidStack resource)
    {
        if(inputPrimary.contains(posInMultiblock)&&(side==null||side.getAxis()==getFacing().rotateYCCW().getAxis()))
        {
            ChemicalVatTileEntity master = this.master();
            if(master == null || (master.tanks[0].getFluidAmount() >= master.tanks[0].getCapacity()))
                return false;
            return true;
        }

        if(inputSecondary.contains(posInMultiblock)&&(side==null||side.getAxis()==getFacing().rotateYCCW().getAxis()))
        {
            ChemicalVatTileEntity master = this.master();
            if(master == null || (master.tanks[1].getFluidAmount() >= master.tanks[1].getCapacity()))
                return false;
            return true;
        }
        return false;
    }

    @Override
    protected boolean canDrainTankFrom(int iTank, Direction side)
    {
        return outputOffset.equals(posInMultiblock)&&(side==null||side==getFacing().getOpposite());
    }

   @Override
    public void doGraphicalUpdates()
    {
        this.markDirty();
        this.markContainingBlockForUpdate(null);
    }

    @Override
    public VatRecipe findRecipeForInsertion(ItemStack inserting) {
        VatRecipe primeSec = VatRecipe.findRecipe(inserting, tanks[0].getFluid(), tanks[1].getFluid());
        return primeSec == null ? VatRecipe.findRecipe(inserting, tanks[1].getFluid(), tanks[0].getFluid()) : primeSec;
    }

    @Override
    public int[] getOutputSlots() {
        return new int[0];
    }

    @Override
    public int[] getOutputTanks() {
        return new int[]{2};
    }

    @Override
    protected VatRecipe getRecipeForId(ResourceLocation id) {
        return VatRecipe.recipes.get(id);
    }

    //Direct Copy from IP's Pumpjack, this will need to be changed.
    private static List<AxisAlignedBB> getShape(BlockPos posInMultiblock){
        final int bX = posInMultiblock.getX();
        final int bY = posInMultiblock.getY();
        final int bZ = posInMultiblock.getZ();

        //Empty space
        if (bX == 0 && bZ == 0)
        {
            if (bY == 2 || bY == 3)
            {
                return new ArrayList<>();
            }
            if (bY == 1)
            {
                return Arrays.asList(new AxisAlignedBB(0.1875, 0.0, 0.0, 1.0, 1.0, 1.0));
            }
        }
        return Arrays.asList(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0));
    }

    public ITextComponent[] getOverlayText(PlayerEntity player, RayTraceResult mop, boolean hammer) {
        if (Utils.isFluidRelatedItemStack(player.getHeldItem(Hand.MAIN_HAND))) {
            ChemicalVatTileEntity master = (ChemicalVatTileEntity)this.master();
            FluidStack fs1 = master != null ? master.tanks[0].getFluid() : this.tanks[0].getFluid();
            FluidStack fs2 = master != null ? master.tanks[1].getFluid() : this.tanks[1].getFluid();
            FluidStack fs3 = master != null ? master.tanks[2].getFluid() : this.tanks[2].getFluid();
            return new ITextComponent[]{TextUtils.formatFluidStack(fs1),TextUtils.formatFluidStack(fs2), new StringTextComponent("Input: " + master.getInventory().get(0).getDisplayName().getString() + " | Count: " + master.getInventory().get(0).getCount()), TextUtils.formatFluidStack(fs3)};
        } else {
            return null;
        }
    }

    @Override
    public boolean useNixieFont(PlayerEntity playerEntity, RayTraceResult rayTraceResult) {
        return false;
    }

    public boolean interact(Direction side, PlayerEntity player, Hand hand, ItemStack heldItem, float hitX, float hitY, float hitZ) {
        return false;
    }

}
