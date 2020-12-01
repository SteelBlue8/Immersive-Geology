package com.igteam.immersivegeology.common.fluid;

import com.igteam.immersivegeology.ImmersiveGeology;
import com.igteam.immersivegeology.api.materials.Material;
import com.igteam.immersivegeology.api.materials.MaterialUseType;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

public class IGFluid extends Fluid {

   private final String name;

    public IGFluid(MaterialUseType type, Material mat){
        this.name = "molten_"+mat.getName();
        setRegistryName(ImmersiveGeology.MODID, this.name);

    }

    public String getName(){
        return this.name;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.SOLID;
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
    protected Vec3d func_215663_a(IBlockReader iBlockReader, BlockPos blockPos, IFluidState iFluidState) {
        return null;
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
    public float func_215662_a(IFluidState iFluidState, IBlockReader iBlockReader, BlockPos blockPos) {
        return 0;
    }

    @Override
    public float func_223407_a(IFluidState iFluidState) {
        return 0;
    }

    @Override
    protected BlockState getBlockState(IFluidState iFluidState) {
        return null;
    }

    @Override
    public boolean isSource(IFluidState iFluidState) {
        return false;
    }

    @Override
    public int getLevel(IFluidState iFluidState) {
        return 0;
    }

    @Override
    public VoxelShape func_215664_b(IFluidState iFluidState, IBlockReader iBlockReader, BlockPos blockPos) {
        return null;
    }
}
