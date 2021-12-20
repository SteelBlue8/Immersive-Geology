package com.igteam.immersive_geology.api.materials.material_data.fluids;

import com.igteam.immersive_geology.api.materials.MaterialEnum;
import com.igteam.immersive_geology.api.materials.MaterialUseType;
import com.igteam.immersive_geology.api.materials.fluid.FluidEnum;
import com.igteam.immersive_geology.api.materials.fluid.SlurryEnum;
import com.igteam.immersive_geology.api.materials.helper.MaterialTypes;
import com.igteam.immersive_geology.api.materials.helper.PeriodicTableElement;
import com.igteam.immersive_geology.api.materials.helper.PeriodicTableElement.ElementProportion;
import com.igteam.immersive_geology.api.materials.helper.processing.IGMaterialProcess;
import com.igteam.immersive_geology.api.materials.helper.processing.methods.IGVatProcessingMethod;
import com.igteam.immersive_geology.api.materials.material_bases.MaterialFluidBase;
import com.igteam.immersive_geology.core.lib.IGLib;
import com.igteam.immersive_geology.core.registration.IGRegistrationHolder;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;

import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 * Created by JStocke12 on 31-03-2020
 */
public class MaterialFluidBrine extends MaterialFluidBase
{
	@Override
	public String getName()
	{
		return "brine";
	}

	@Override
	public String getModID()
	{
		return IGLib.MODID;
	}

	@Override
	public LinkedHashSet<ElementProportion> getElements()
	{
		return new LinkedHashSet<>(Arrays.asList(
				new ElementProportion(PeriodicTableElement.OXYGEN),
				new ElementProportion(PeriodicTableElement.HYDROGEN, 2)
		));
	}

	@Override
	public Rarity getRarity()
	{
		return Rarity.COMMON;
	}

	@Override
	public EnumFluidType getFluidType()
	{
		return EnumFluidType.SOLUTION;
	}

	@Override
	public LinkedHashSet<ElementProportion> getSoluteElements()
	{
		return new LinkedHashSet<>(Arrays.asList(
				new ElementProportion(PeriodicTableElement.SODIUM),
				new ElementProportion(PeriodicTableElement.CHLORINE)
		));
	}

	@Override
	public float getConcentration()
	{
		return 1;
	}

	@Override
	public MaterialTypes getMaterialType()
	{
		return MaterialTypes.FLUID;
	}

	@Override
	public int getBoilingPoint()
	{
		return 373;
	}

	@Override
	public int getMeltingPoint()
	{
		return 269;
	}

	@Override
	public int getColor(int temperature)
	{
		return 0xBCA271;
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
	public boolean hasFlask() {
		return false;
	}

	@Override
	public boolean hasBucket() {
		return true;
	}

	@Override
	public IGMaterialProcess getProcessingMethod()
	{
		IGVatProcessingMethod rocksalt_solution_method = new IGVatProcessingMethod(500, 60);
		rocksalt_solution_method.addItemOutput(ItemStack.EMPTY);
		rocksalt_solution_method.addFluidOutput(FluidEnum.Brine, 125);
		rocksalt_solution_method.addPrimaryFluidInput(Fluids.WATER, 125);
		rocksalt_solution_method.addSecondaryFluidInput(Fluids.EMPTY, 0);
		rocksalt_solution_method.addItemInput(new ItemStack(IGRegistrationHolder.getItemByMaterial(MaterialEnum.RockSalt.getMaterial(), MaterialUseType.DUST), 1));

		inheritedProcessingMethods.add(rocksalt_solution_method);

		return super.getProcessingMethod();
	}
}
