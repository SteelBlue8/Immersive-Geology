package com.igteam.immersivegeology.common.blocks;

import com.igteam.immersivegeology.api.materials.Material;
import com.igteam.immersivegeology.api.materials.MaterialUseType;
import com.igteam.immersivegeology.api.toolsystem.Tooltypes;
import com.igteam.immersivegeology.api.util.IGMathHelper;
import com.igteam.immersivegeology.api.util.IGRegistryGrabber;
import com.igteam.immersivegeology.common.blocks.property.IGProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import java.util.ArrayList;
import java.util.List;

public class IGVanillaOreBlock extends IGMaterialBlock implements IIGOreBlock
{
	public IGVanillaOreBlock(Material material, MaterialUseType type, Material oreMat)
	{
		this(material, type, "", oreMat);
	}

	public IGVanillaOreBlock(Material material, MaterialUseType type, String sub, Material oreMat)
	{
		super(sub, type, material, oreMat);

		this.setBlockLayer(BlockRenderLayer.CUTOUT);
		//set state variables
		this.setDefaultState(this.stateContainer.getBaseState());
	}

	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player)
	{
		ItemStack tool = player.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
		if(!player.isCreative()&&!player.isSpectator()&&this.canHarvestBlock(state, worldIn, pos, player))
		{
			if(subtype==MaterialUseType.ORE_BEARING&&tool.getToolTypes().contains(ToolType.PICKAXE))
			{
				//TODO find out how to store state in item (this.asItem())?
				worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this.itemBlock)));

			}
		}

		worldIn.playEvent(player, 2001, pos, getStateId(state));
	}

	public Material getOreMaterial()
	{
		return materials[1];
	}

	@Override
	public boolean hasMultipartModel()
	{
		return false;
	}

}
