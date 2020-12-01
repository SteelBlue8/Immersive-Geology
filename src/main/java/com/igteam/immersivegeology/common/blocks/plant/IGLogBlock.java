package com.igteam.immersivegeology.common.blocks.plant;

import com.igteam.immersivegeology.api.materials.Material;
import com.igteam.immersivegeology.api.materials.MaterialUseType;
import com.igteam.immersivegeology.api.util.IGRegistryGrabber;
import com.igteam.immersivegeology.common.blocks.IGMaterialBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.IPlantable;

/**
 * Created by Pabilo8 on 26-03-2020.
 */
public class IGLogBlock extends IGMaterialBlock
{

	public static final EnumProperty<Direction.Axis> AXIS;
	public IGLogBlock(MaterialUseType use_type,Material material)
	{
		super(use_type, material);
		this.setDefaultState((BlockState)this.getDefaultState().with(NATURAL, false).with(AXIS, Direction.Axis.Y));
	}

	@Override
	public void onPlayerDestroy(IWorld world, BlockPos pos, BlockState state) {
		super.onPlayerDestroy(world, pos, state);
		//world.addEntity(new ItemEntity(world.getWorld(), pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this.itemBlock.getItem())));
	}

	//No changes to stats
	private static Properties createPropertyFromMaterial(Material material)
	{
		return Properties.create(net.minecraft.block.material.Material.WOOD).hardnessAndResistance(material.getHardness(), material.getMiningResistance());
	}

	public BlockState rotate(BlockState p_185499_1_, Rotation p_185499_2_) {
		switch(p_185499_2_) {
			case COUNTERCLOCKWISE_90:
			case CLOCKWISE_90:
				switch((Direction.Axis)p_185499_1_.get(AXIS)) {
					case X:
						return (BlockState)p_185499_1_.with(AXIS, Direction.Axis.Z);
					case Z:
						return (BlockState)p_185499_1_.with(AXIS, Direction.Axis.X);
					default:
						return p_185499_1_;
				}
			default:
				return p_185499_1_;
		}
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(AXIS);
	}

	public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
		return (BlockState)this.getDefaultState().with(AXIS, p_196258_1_.getFace().getAxis());
	}

	static {
		AXIS = BlockStateProperties.AXIS;
	}
}
