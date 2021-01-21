package com.igteam.immersivegeology.common.blocks.metal;

import com.google.common.collect.Lists;
import com.igteam.immersivegeology.ImmersiveGeology;
import com.igteam.immersivegeology.api.materials.Material;
import com.igteam.immersivegeology.api.materials.MaterialUseType;
import com.igteam.immersivegeology.client.menu.helper.ItemSubGroup;
import com.igteam.immersivegeology.common.IGContent;
import com.igteam.immersivegeology.common.blocks.IGBlockItem;
import com.igteam.immersivegeology.common.blocks.IGMaterialBlock;
import com.igteam.immersivegeology.common.blocks.IIGBlock;
import com.igteam.immersivegeology.common.fluid.IGFluid;
import net.minecraft.block.*;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Created by Pabilo8 on 26-03-2020.
 */
public class IGFluidBlock extends FlowingFluidBlock implements IIGBlock
{
	public final String name;
	public BlockItem itemBlock = null;
	public ItemSubGroup itemSubGroup = ItemSubGroup.misc;


	public IGFluidBlock(IGFluid fluid ,Material material, boolean isSource) {
		this(fluid, material, ItemSubGroup.misc, isSource);
	}

	public IGFluidBlock(IGFluid fluid ,Material material, ItemSubGroup group, boolean isSource) {
		super(fluid, Properties.create(net.minecraft.block.material.Material.LAVA).doesNotBlockMovement().tickRandomly().hardnessAndResistance(100.0F).lightValue(15).noDrops());
		String prefix = isSource ? "source_" : "flowing_";
		this.name = "fluid_" + prefix + material.getName();
		setRegistryName(ImmersiveGeology.MODID, this.name);

		try
		{
			if(this.itemBlock == null) {
				this.itemSubGroup = group;
				this.itemBlock = new IGBlockItem(this, new Item.Properties().group(ImmersiveGeology.IG_ITEM_GROUP),group);
				this.itemBlock.setRegistryName(this.getRegistryName());
				IGContent.addItemBlockForBlock(name, this.itemBlock);
			}
		} catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}


	@Override
	public boolean hasFlavour() {
		return false;
	}

	@Override
	public Item getItemBlock() {
		return this.itemBlock;
	}

	@Override
	public String getNameForFlavour() {
		return this.name;
	}
}
