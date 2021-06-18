package com.igteam.immersive_geology.api.materials.material_bases;

import com.igteam.immersive_geology.api.materials.*;

import java.util.Set;

/**
 * Created by JStocke12 on 31-03-2020.
 */
public abstract class MaterialFluidBase extends Material
{
	public abstract EnumFluidType getFluidType();

	public abstract Set<PeriodicTableElement.ElementProportion> getSoluteElements();

	public abstract float getConcentration();

	@Override
	public boolean hasSubtype(MaterialUseType useType)
	{
		switch(useType)
		{
			case BUCKET:
				return hasBucket();
			case FLUIDS:
				return hasFluidBlock();
		}
		return false;
	}

	@Override
	public MaterialTypes getMaterialType()
	{
		return MaterialTypes.FLUID;
	}
	
	@Override
	public MaterialTypes getMaterialSubType()
	{
		return MaterialTypes.FLUID;
	}

	@Override
	public net.minecraft.block.material.Material getBlockMaterial()
	{
		return net.minecraft.block.material.Material.WATER;
	}

	public enum EnumFluidType
	{
		FLUID,
		SOLUTION,
		GAS
	}

	public boolean hasBucket()
	{
		return true;
	}

	public boolean hasFluidBlock()
	{
		return true;
	}

	@Override
	public MaterialEnum getProcessedType() {
		return null;
	}


	@Override
	public MaterialEnum getSecondaryType() {
		return null;
	}
}
