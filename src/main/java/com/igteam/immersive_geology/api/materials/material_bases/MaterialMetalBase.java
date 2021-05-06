package com.igteam.immersive_geology.api.materials.material_bases;

import com.igteam.immersive_geology.api.materials.Material;
import com.igteam.immersive_geology.api.materials.MaterialTypes;
import com.igteam.immersive_geology.api.materials.MaterialUseType;

/**
 * Created by Pabilo8 on 25-03-2020.
 */
public abstract class MaterialMetalBase extends Material
{
	public abstract EnumMetalType getMetalType();
	protected boolean isNativeMetal = false;
	@Override
	public boolean hasSubtype(MaterialUseType useType)
	{
		switch(useType)
		{
			case INGOT:
				return hasIngot();
			case NUGGET:
				return hasNugget();
			case PLATE:
				return hasPlate();
			case ROD:
				return hasRod();
			case GEAR:
				return hasGear();
			case WIRE:
				return hasWire();
			case DUST:
			case TINY_DUST:
				return hasDust();
			case SHEETMETAL:
				return hasSheetmetal();
			case STAIRS:
				return hasStairs();
			case DUST_BLOCK:
				return hasDustBlock();
			case FLUIDS:
				return true;
			case ORE_CHUNK:
			case ORE_BIT:
			case ORE_STONE:
				return isNativeMetal;
			default:
				return false;
		}
	}

	@Override
	public MaterialTypes getMaterialType()
	{
		return MaterialTypes.METAL;
	}
	
	@Override
	public MaterialTypes getMaterialSubType()
	{
		return (isNativeMetal ? MaterialTypes.MINERAL : MaterialTypes.METAL);
	}

	@Override
	public net.minecraft.block.material.Material getBlockMaterial()
	{
		return net.minecraft.block.material.Material.IRON;
	}

	public enum EnumMetalType
	{
		METALLOID,
		METAL,
		ALLOY
	}

	public boolean hasIngot()
	{
		return true;
	}

	public boolean hasNugget()
	{
		return true;
	}

	public boolean hasPlate()
	{
		return true;
	}

	public boolean hasRod()
	{
		return true;
	}

	public boolean hasGear()
	{
		return true;
	}

	public boolean hasWire()
	{
		return true;
	}

	public boolean hasSheetmetal()
	{
		return true;
	}

	public boolean hasStairs() { return true; }

	public boolean hasDust()
	{
		return true;
	}

	public boolean hasDustBlock()
	{
		return true;
	}
}
