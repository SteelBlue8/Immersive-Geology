package com.igteam.immersivegeology.common.world.biome.biomes.overworld;

import com.igteam.immersivegeology.common.world.biome.IGBiome;
import com.igteam.immersivegeology.common.world.biome.IGDefaultBiomeFeatures;
import com.igteam.immersivegeology.common.world.gen.surface.util.SurfaceBlockType;
import com.igteam.immersivegeology.common.world.noise.INoise2D;
import com.igteam.immersivegeology.common.world.noise.SimplexNoise2D;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.DefaultBiomeFeatures;

import javax.annotation.Nonnull;

import static com.igteam.immersivegeology.common.world.gen.config.ImmersiveGenerationSettings.SEA_LEVEL;


public class VolcanicCenterBiome extends IGBiome
{
	public VolcanicCenterBiome()
	{
		super(new Builder().category(Category.DESERT).precipitation(RainType.NONE).downfall(0.1f).temperature(0.75f), .75f, .01f);
		IGDefaultBiomeFeatures.addCarvers(this);
	}

	@Nonnull
	@Override
	public INoise2D createNoiseLayer(long seed)
	{
		return new SimplexNoise2D(seed).octaves(4).spread(0.2f).scaled(SEA_LEVEL+32, SEA_LEVEL+36);
	}

	@Override
	public BlockState returnBlockType(SurfaceBlockType part, float chunkTemp, float chunkRain)
	{
		switch(part)
		{
			case grass:
			case dirt:
				return Blocks.SAND.getDefaultState();
			default:
				return Blocks.CLAY.getDefaultState();
		}
	}
}