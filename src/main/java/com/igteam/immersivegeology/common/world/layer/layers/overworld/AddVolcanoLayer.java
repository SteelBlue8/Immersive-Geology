package com.igteam.immersivegeology.common.world.layer.layers.overworld;

import com.igteam.immersivegeology.common.world.layer.IGLayerUtil;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;

public enum AddVolcanoLayer implements ICastleTransformer
{
	INSTANCE;

	@Override
	public int apply(INoiseRandom context, int north, int west, int south, int east, int center)
	{
		if(IGLayerUtil.canPlaceVolcano(north)&&IGLayerUtil.canPlaceVolcano(west)&&IGLayerUtil.canPlaceVolcano(south)&&IGLayerUtil.canPlaceVolcano(east)&&IGLayerUtil.canPlaceVolcano(center))
		{
				return IGLayerUtil.VOLCANIC_CENTER;
		}
		return center;
	}
}