package com.igteam.immersivegeology.common.materials.crystals;

import com.igteam.immersivegeology.ImmersiveGeology;
import com.igteam.immersivegeology.api.materials.MaterialCrystalStructure;
import com.igteam.immersivegeology.api.materials.PeriodicTableElement;
import com.igteam.immersivegeology.api.materials.PeriodicTableElement.ElementProportion;
import com.igteam.immersivegeology.api.materials.material_bases.MaterialCrystalBase;
import net.minecraft.item.Rarity;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class MaterialCrystalPhlebotinum extends MaterialCrystalBase
{
	@Override
	public String getName()
	{
		return "phlebotinum";
	}

	@Nonnull
	@Override
	public String getModID()
	{
		return ImmersiveGeology.MODID;
	}

	@Override
	public LinkedHashSet<ElementProportion> getElements()
	{
		return new LinkedHashSet<>(Arrays.asList(
				new ElementProportion(PeriodicTableElement.PHLEBOTINUM)
		));
	}

	@Nonnull
	@Override
	public Rarity getRarity()
	{
		return Rarity.EPIC;
	}

	@Override
	public int getBoilingPoint()
	{
		return 982;
	}

	@Override
	public int getMeltingPoint()
	{
		return 485;
	}

	public static int baseColor = 0x663399;

	@Override
	public int getColor(int temperature)
	{
		return baseColor;
	}

	@Override
	public float getHardness()
	{
		return 7.0F;
	}

	@Override
	public float getMiningResistance()
	{
		return 7.0F;
	}

	@Override
	public float getBlastResistance()
	{
		return 16;
	}

	@Override
	public float getDensity()
	{
		return 0.95f;
	}

	@Override
	public int getBlockHarvestLevel()
	{
		return 1;
	}

	@Override
	public MaterialCrystalStructure getCrystalStructure() {
		return MaterialCrystalStructure.ISOMETRIC;
	}

	@Override
	public MaterialCrystalStructure.LatticeStructure getLatticeStructure() {
		return MaterialCrystalStructure.LatticeStructure.cubes;
	}

	@Override
	public boolean hasRawCrystal()
	{
		return false;
	}

	@Override
	public int getTipAttackDamage() {
		return 8;
	}

	@Override
	public int getTipDurability() {
		return 200;
	}

	@Override
	public int getTipMiningLevel() {
		return 8;
	}

	@Override
	public int getTipMiningSpeed() {
		return 10;
	}
}
