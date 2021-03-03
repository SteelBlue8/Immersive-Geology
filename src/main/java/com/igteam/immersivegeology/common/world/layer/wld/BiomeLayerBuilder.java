package com.igteam.immersivegeology.common.world.layer.wld;

import com.igteam.immersivegeology.common.world.biome.IGBiome;
import com.igteam.immersivegeology.common.world.layer.BiomeLayerData;

import net.minecraft.world.biome.Biome;

public class BiomeLayerBuilder {

	public static BiomeLayerData create(IGBiome biome) {
		return create(biome, 1f);
	}
	
	public static BiomeLayerData create(IGBiome biome, float hardnessMod) {
		BiomeLayerData data = new BiomeLayerData(biome, hardnessMod);
		return data;
	}
}
