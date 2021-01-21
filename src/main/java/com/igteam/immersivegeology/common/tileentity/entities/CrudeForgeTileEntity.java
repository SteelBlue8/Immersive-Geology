package com.igteam.immersivegeology.common.tileentity.entities;

import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.blocks.IEBaseTileEntity;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.gui.TileInventory;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import com.igteam.immersivegeology.api.materials.MaterialUseType;
import com.igteam.immersivegeology.api.util.IGRegistryGrabber;
import com.igteam.immersivegeology.common.items.IGMaterialYieldItem;
import com.igteam.immersivegeology.common.materials.EnumMaterials;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.EnumProperty;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.lwjgl.system.CallbackI;

import javax.annotation.Nullable;

public class CrudeForgeTileEntity extends IEBaseTileEntity implements ITickableTileEntity, IEBlockInterfaces.IActiveState, IEBlockInterfaces.IInteractionObjectIE, IEBlockInterfaces.IProcessTile, IEBlockInterfaces.IBlockBounds, IIEInventory {
    //THIS IS REQUIRED FOR IT TO REGISTER,
    public static TileEntityType<CrudeForgeTileEntity> TYPE;

    public IInventory inventory = new Inventory(ItemStack.EMPTY,ItemStack.EMPTY);

    public CrudeForgeData guiData = new CrudeForgeData();

    public static final int SLOT_FUEL = 0;
    public static final int SLOT_INPUT = 1;

    public final FluidTank oreTank = new FluidTank(6000)
    {
        @Override
        protected void onContentsChanged()
        {

        }

        @Override
        public boolean isFluidValid(FluidStack fluid)
        {
            return true;
        }
    };

    public final FluidTank wasteTank = new FluidTank(2000)
    {
        @Override
        protected void onContentsChanged()
        {

        }

        @Override
        public boolean isFluidValid(FluidStack fluid)
        {
            return true;
        }
    };

    public CrudeForgeTileEntity() {
        super(TYPE);
    }

    @Override
    public void readCustomNBT(CompoundNBT compoundNBT, boolean b) {

    }

    @Override
    public void writeCustomNBT(CompoundNBT compoundNBT, boolean b) {

    }

    @Override
    public VoxelShape getBlockBounds(@Nullable ISelectionContext iSelectionContext) {
        return null;
    }

    @Nullable
    @Override
    public IEBlockInterfaces.IInteractionObjectIE getGuiMaster() {
        return this;
    }

    @Override
    public boolean canUseGui(PlayerEntity playerEntity) {
        return true;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("crude_forge");
    }

    @Nullable
    @Override
    public NonNullList<ItemStack> getInventory() {
        return null;
    }

    @Override
    public boolean isStackValid(int i, ItemStack itemStack) {
        return true;
    }

    @Override
    public int getSlotLimit(int i) {
        return 2;
    }

    @Override
    public void doGraphicalUpdates(int i) {

    }

    @Override
    public int[] getCurrentProcessesStep() {
        return new int[0];
    }

    @Override
    public int[] getCurrentProcessesMax() {
        return new int[0];
    }

    @Override
    public void tick() {
        if(this.inventory != null) {
            if (this.inventory.getStackInSlot(SLOT_INPUT).getItem() instanceof IGMaterialYieldItem) {
                IGMaterialYieldItem yeild = (IGMaterialYieldItem) this.inventory.getStackInSlot(SLOT_INPUT).getItem();
                oreTank.fill(new FluidStack(IGRegistryGrabber.grabFluid(true, yeild.getMaterial()), yeild.getMaxOreYield()), IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }

    public class CrudeForgeData implements IIntArray
    {
        public static final int MAX_BURN_TIME = 0;
        public static final int BURN_TIME = 1;

        @Override
        public int get(int index)
        {
            switch(index)
            {
                case MAX_BURN_TIME:
                    return 0;//processMax;
                case BURN_TIME:
                    return 0;//process;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value)
        {
            switch(index)
            {
                case MAX_BURN_TIME:
                    //processMax = value;
                    break;
                case BURN_TIME:
                   // process = value;
                    break;
                default:
                    break;
            }
        }

        @Override
        public int size()
        {
            return 4;
        }
    }
}
