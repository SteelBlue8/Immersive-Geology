package com.igteam.immersivegeology.common.materials.minerals;

import com.igteam.immersivegeology.ImmersiveGeology;
import com.igteam.immersivegeology.api.materials.PeriodicTableElement;
import com.igteam.immersivegeology.api.materials.PeriodicTableElement.ElementProportion;
import com.igteam.immersivegeology.api.materials.material_bases.MaterialMineralBase;
import net.minecraft.item.Rarity;

import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 * Created by JStocke12 on 31-03-2020
 */
public class MaterialMineralChalcopyrite extends MaterialMineralBase
{
	@Override
	public String getName()
	{
		return "chalcopyrite";
	}

	@Override
	public String getModID()
	{
		return ImmersiveGeology.MODID;
	}

	@Override
	public LinkedHashSet<ElementProportion> getElements()
	{
		return new LinkedHashSet<>(Arrays.asList(
						new PeriodicTableElement.ElementProportion(PeriodicTableElement.COPPER),
						new PeriodicTableElement.ElementProportion(PeriodicTableElement.IRON),
						new PeriodicTableElement.ElementProportion(PeriodicTableElement.SULFUR, 2)
				)
		);
	}

	@Override
	public Rarity getRarity()
	{
		return Rarity.COMMON;
	}

	@Override
	public EnumMineralType getMineralType() {
		return EnumMineralType.MINERAL;
	}

	@Override
	public int getBoilingPoint()
	{
		return -1;
	}

	@Override
	public int getMeltingPoint()
	{
		return -1;
	}

	@Override
	public int getColor(int temperature)
	{
		return 0xffffff;
	}

	//Needs to be changed in code for subtypes, such as sheetmetal
	@Override
	public float getHardness()
	{
		return -1F;
	}

	@Override
	public float getMiningResistance()
	{
		return -1F;
	}

	@Override
	public float getBlastResistance()
	{
		return -1F;
	}

	//Copied from Immersive Intelligence (steel has i think 1.65, leaves 0.35)
	@Override
	public float getDensity()
	{
		return 1f;
	}

	//Stone pickaxe level
	@Override
	public int getBlockHarvestLevel()
	{
		return 0;
	}

	@Override
	public net.minecraft.block.material.Material getBlockMaterial()
	{
		return null;
	}

	/*@Nullable
	@Override
	public IItemTier getToolTier()
	{
		return IGContent.;
	}*/
}
