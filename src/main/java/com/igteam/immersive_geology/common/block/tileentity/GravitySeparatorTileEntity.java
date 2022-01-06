package com.igteam.immersive_geology.common.block.tileentity;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.api.fluid.FluidUtils;
import blusunrize.immersiveengineering.api.utils.CapabilityReference;
import blusunrize.immersiveengineering.api.utils.DirectionalBlockPos;
import blusunrize.immersiveengineering.api.utils.shapes.CachedShapesWithTransform;
import blusunrize.immersiveengineering.client.utils.TextUtils;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.generic.MultiblockPartTileEntity;
import blusunrize.immersiveengineering.common.blocks.generic.PoweredMultiblockTileEntity;
import blusunrize.immersiveengineering.common.util.Utils;
import com.google.common.collect.ImmutableSet;
import com.igteam.immersive_geology.ImmersiveGeology;
import com.igteam.immersive_geology.api.crafting.IGMultiblockRecipe;
import com.igteam.immersive_geology.api.crafting.recipes.recipe.SeparatorRecipe;
import com.igteam.immersive_geology.api.multiblock.Dual;
import com.igteam.immersive_geology.api.multiblock.IGMachineInfo;
import com.igteam.immersive_geology.api.multiblock.IGProcessingQueue;
import com.igteam.immersive_geology.common.block.helpers.IIGMultiblockProcess;
import com.igteam.immersive_geology.common.multiblocks.GravitySeparatorMultiblock;
import com.igteam.immersive_geology.core.registration.IGTileTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
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
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import java.util.*;

//Sorry to IE for using their internal classes, we should have used an API, and we'll maybe fix it later.
public class GravitySeparatorTileEntity extends MultiblockPartTileEntity<GravitySeparatorTileEntity> implements IEBlockInterfaces.IBlockOverlayText, IEBlockInterfaces.IPlayerInteraction, IBlockBounds, IIGMultiblockProcess, ITickableTileEntity {

    public static final Set<BlockPos> Redstone_IN = ImmutableSet.of(new BlockPos(1, 6, 2));

    /** Input Fluid Tank<br> */
    public static final int TANK_INPUT = 0;

    public final FluidTank tank = new FluidTank(16 * FluidAttributes.BUCKET_VOLUME);
    private final List<CapabilityReference<IFluidHandler>> fluidNeighbors;
    public final IGProcessingQueue<SeparatorRecipe> masterQueue;

    public GravitySeparatorTileEntity() {
        super(GravitySeparatorMultiblock.INSTANCE, IGTileTypes.GRAVITY.get(),false);
        this.fluidNeighbors = new ArrayList();
        this.fluidNeighbors.add(CapabilityReference.forNeighbor(this, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.UP));
        this.masterQueue = new IGProcessingQueue<>();
        this.masterQueue.setMachineInfo(new IGMachineInfo(this));
    }

    @Override
    public void readCustomNBT(CompoundNBT nbt, boolean descPacket) {
        super.readCustomNBT(nbt, descPacket);
        tank.readFromNBT(nbt);
        masterQueue.readFromNBT(nbt);
    }

    @Override
    public void writeCustomNBT(CompoundNBT nbt, boolean descPacket) {
        super.writeCustomNBT(nbt, descPacket);
        tank.writeToNBT(nbt);
        masterQueue.writeToNBT(nbt);
    }


    @Override
    public TileEntityType<?> getType() {
        return IGTileTypes.GRAVITY.get();
    }

    private boolean hasItemsFlag = false;
    private boolean markDirty = false;
    @Override
    public void tick() {
        GravitySeparatorTileEntity master = this.master();
        assert master != null;
        if(master.masterQueue.hasElements()){
            master.masterQueue.doProcessingStep();
            master.updateContainingBlockInfo();

            if(!master.tank.isEmpty()){
                master.tank.drain(2, IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }



    @Override
    public Set<BlockPos> getRedstonePos() {
        return Redstone_IN;
    }

    public boolean isInInput(){
            return true;
    }

    @Override
    public void onEntityCollision(World world, Entity entity) {
        // Actual intersection with the input box is checked later
        boolean bpos = isInInput();
        if (bpos && !world.isRemote && entity.isAlive()) {
            GravitySeparatorTileEntity master = master();
            if (master == null)
                return;
            Vector3d center = Vector3d.copyCentered(master.getPos());
            AxisAlignedBB separatorInternal = new AxisAlignedBB(center.x - 4, center.y, center.z - 4, center.x + 4, center.y + 10, center.z + 4);
            if (!entity.getBoundingBox().intersects(separatorInternal))
                return;
            if (entity instanceof ItemEntity && !((ItemEntity) entity).getItem().isEmpty()) {
                ItemStack stack = ((ItemEntity) entity).getItem();
                if (stack.isEmpty())
                    return;
                SeparatorRecipe recipe = master.findRecipeForInsertion(stack);
                if (recipe == null) {
                    return;
                }
                ItemStack recipeInput = recipe.getDisplayStack(stack);

                ImmersiveGeology.getNewLogger().log(Level.INFO, "Testing Typed: " + this.getRecipeForId(recipe.getId()).getId().getPath());

                if(master.masterQueue.addProcessToQueue(recipe)) {
                    stack.shrink(recipeInput.getCount());
                    master.updateContainingBlockInfo();
                    if (stack.isEmpty()) {
                        entity.remove();
                    }
                }
            }
        }
    }

    public ITextComponent[] getOverlayText(PlayerEntity player, RayTraceResult mop, boolean hammer) {
        if (Utils.isFluidRelatedItemStack(player.getHeldItem(Hand.MAIN_HAND))) {
            GravitySeparatorTileEntity master = master();
            FluidStack fs = master != null ? master.tank.getFluid() : this.tank.getFluid();
            return new ITextComponent[]{TextUtils.formatFluidStack(fs)};
        } else {
            return null;
        }
    }

    @Override
    public boolean useNixieFont(PlayerEntity playerEntity, RayTraceResult rayTraceResult) {
        return false;
    }

    public boolean interact(Direction side, PlayerEntity player, Hand hand, ItemStack heldItem, float hitX, float hitY, float hitZ) {
        GravitySeparatorTileEntity master = (GravitySeparatorTileEntity)this.master();
        if (master != null && FluidUtils.interactWithFluidHandler(player, hand, master.tank)) {
            this.updateMasterBlock((BlockState)null, true);
            return true;
        } else {
            return false;
        }
    }

    public SeparatorRecipe findRecipeForInsertion(ItemStack inserting) {
        return SeparatorRecipe.findRecipe(inserting);
    }

    protected SeparatorRecipe getRecipeForId(ResourceLocation id) {
        return (SeparatorRecipe) masterQueue.getMachineInfo().getRecipeForId(id);
    }

    private static final Set<BlockPos> inputOffset = ImmutableSet.of(
            new BlockPos(1, 6, 1)
    );

    @Nonnull
    @Override
    protected IFluidTank[] getAccessibleFluidTanks(Direction side) {
        GravitySeparatorTileEntity master = master();
        if(master != null){
            if(inputOffset.contains(posInMultiblock)){
                return new IFluidTank[]{master.tank};
            }
        }
        return new IFluidTank[0];
    }


    @Override
    protected boolean canFillTankFrom(int iTank, Direction side, FluidStack resource) {
        if(inputOffset.contains(posInMultiblock) && resource.getFluid().equals(Fluids.WATER)) {
            GravitySeparatorTileEntity master = this.master();
            if(master == null || master.tank.getFluidAmount() >= master.tank.getCapacity()){
                return false;
            }
            return true;
        }

        return false;
    }

    @Override
    protected boolean canDrainTankFrom(int iTank, Direction side) {
        return false;
    }

    private static CachedShapesWithTransform<BlockPos, Pair<Direction, Boolean>> SHAPES = CachedShapesWithTransform.createForMultiblock(GravitySeparatorTileEntity::getShape);

    @Override
    public VoxelShape getBlockBounds(ISelectionContext ctx) {
        return SHAPES.get(this.posInMultiblock, Pair.of(getFacing(), getIsMirrored()));
    }

    //Direct Copy from IP's Pumpjack, this will need to be changed.
    private static List<AxisAlignedBB> getShape(BlockPos posInMultiblock){
        final int bX = posInMultiblock.getX();
        final int bY = posInMultiblock.getY();
        final int bZ = posInMultiblock.getZ();
        if (bY == 6)
        {
            if (bZ == 1 && bX == 2)
            {
                return Arrays.asList(new AxisAlignedBB(0.0, 0.75, 0.0, 0.5, 1.0, 1.0));

            }
            if (bZ == 2 && bX == 1)
            {
                return Arrays.asList(new AxisAlignedBB(0.0, 0.75, 0.0, 1.0, 1.0, 0.5));

            }
            if (bZ == 0 && bX == 1)
            {
                return Arrays.asList(new AxisAlignedBB(0.0, 0.75, 0.5, 1.0, 1.0, 1.0));

            }
            if (bZ == 1 && bX == 0)
            {
                return Arrays.asList(new AxisAlignedBB(0.5, 0.75, 0.0, 1.0, 1.0, 1.0));

            }
        }
        return Arrays.asList(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0));

    }

    @Override
    public boolean canProcessOutput() {
        return !tank.isEmpty();
    }

    private CapabilityReference<IItemHandler> output = CapabilityReference.forTileEntityAt(this,
            () -> new DirectionalBlockPos(getPos().add(0, 0, 0).offset(getFacing(), -2), getFacing()),
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);

    @Override
    public void doOutputFor(MultiblockRecipe recipe, IGMachineInfo info) {
        ItemStack output = recipe.getRecipeOutput().copy();
        output = Utils.insertStackIntoInventory(this.output, output, false);
        if(!output.isEmpty())
            Utils.dropStackAtPos(world, getPos().add(0, 0, 0).offset(getFacing(), -2), output, getFacing().getOpposite());
    }

    @Override
    public Class<? extends IGMultiblockRecipe> getRecipeClass() {
        return SeparatorRecipe.class;
    }
}
