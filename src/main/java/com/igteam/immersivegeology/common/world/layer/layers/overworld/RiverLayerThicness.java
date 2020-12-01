package com.igteam.immersivegeology.common.world.layer.layers.overworld;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;

import static com.igteam.immersivegeology.common.world.layer.IGLayerUtil.OCEAN;
import static com.igteam.immersivegeology.common.world.layer.IGLayerUtil.RIVER;

public enum RiverLayerThicness implements ICastleTransformer
{
	INSTANCE;

	public int apply(INoiseRandom context, int north, int west, int south, int east, int center)
	{
		if(center!=RIVER&&(south==RIVER||north==RIVER||west==RIVER||east==RIVER))
		{
			return RIVER;
		}
		return center;
	}
}