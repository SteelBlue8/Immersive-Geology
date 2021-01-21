package com.igteam.immersivegeology.common.fluid;

import com.igteam.immersivegeology.ImmersiveGeology;
import com.igteam.immersivegeology.api.materials.Material;
import com.igteam.immersivegeology.api.materials.MaterialUseType;
import com.igteam.immersivegeology.api.util.IGRegistryGrabber;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.item.Item;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.fluids.FluidAttributes;

public abstract class IGFluid extends FlowingFluid {

    protected String name;
    protected Material matType;
    private FluidAttributes attributes;
    private FluidAttributes.Builder builder;
    public IGFluid(MaterialUseType type, Material mat){
        super();
        builder = FluidAttributes.builder(new ResourceLocation(ImmersiveGeology.MODID,type.getName() + "/" + mat.getName() + "_still"),new ResourceLocation(ImmersiveGeology.MODID,type.getName() + "/" + mat.getName()+ "_flowing"));
        builder.color(mat.getColor(mat.getMeltingPoint()));
        builder.density(1000);
        builder.rarity(mat.getRarity());
        builder.temperature(mat.getMeltingPoint());
        builder.viscosity(1000);

        attributes = builder.build(this);

        this.matType = mat;
    }

    public String getName(){
        return this.name;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public Item getFilledBucket() {
        return null;
    }

    @Override
    protected boolean func_215665_a(IFluidState iFluidState, IBlockReader iBlockReader, BlockPos blockPos, Fluid fluid, Direction direction) {
        return false;
    }

    @Override
    public int getTickRate(IWorldReader iWorldReader) {
        return 0;
    }

    @Override
    protected float getExplosionResistance() {
        return 0;
    }

    @Override
    protected BlockState getBlockState(IFluidState iFluidState) {
        return IGRegistryGrabber.grabFluidBlock(MaterialUseType.FLUID_BLOCKS, matType, iFluidState.isSource()).getDefaultState();
    }

    @Override
    public int getLevel(IFluidState iFluidState) {
        return 0;
    }

    @Override
    protected FluidAttributes createAttributes() {
        return attributes;
    }

    @Override
    public Fluid getFlowingFluid() {
        return matType.getFluid(true);
    }

    @Override
    public Fluid getStillFluid() {
        return matType.getFluid(false);
    }

    @Override
    protected boolean canSourcesMultiply() {
        return false;
    }

    @Override
    protected void beforeReplacingBlock(IWorld iWorld, BlockPos blockPos, BlockState blockState) {
        TileEntity lvt_4_1_ = blockState.getBlock().hasTileEntity() ? iWorld.getTileEntity(blockPos) : null;
        Block.spawnDrops(blockState, iWorld.getWorld(), blockPos, lvt_4_1_);
    }

    @Override
    protected int getLevelDecreasePerBlock(IWorldReader iWorldReader) {
        return 1;
    }

    @Override
    protected int getSlopeFindDistance(IWorldReader iWorldReader) {
        return 0;
    }

    //extends the abstract IGFluid to allow source and flowing variants
    public static class Flowing extends IGFluid {
        public Flowing(MaterialUseType type, Material mat) {
            super(type, mat);
            this.name = "fluid_flowing_"+mat.getName();
            setRegistryName(ImmersiveGeology.MODID, this.name);
        }

        protected void fillStateContainer(StateContainer.Builder<Fluid, IFluidState> p_207184_1_) {
            super.fillStateContainer(p_207184_1_);
            p_207184_1_.add(new IProperty[]{LEVEL_1_8});
        }

        public int getLevel(IFluidState p_207192_1_) {
            return (Integer)p_207192_1_.get(LEVEL_1_8);
        }

        public boolean isSource(IFluidState p_207193_1_) {
            return false;
        }
    }

    public static class Source extends IGFluid {
        public Source(MaterialUseType type, Material mat) {
            super(type, mat);
            this.name = "fluid_source_"+mat.getName();
            setRegistryName(ImmersiveGeology.MODID, this.name);
        }

        public int getLevel(IFluidState p_207192_1_) {
            return 8;
        }

        public boolean isSource(IFluidState p_207193_1_) {
            return true;
        }
    }
}