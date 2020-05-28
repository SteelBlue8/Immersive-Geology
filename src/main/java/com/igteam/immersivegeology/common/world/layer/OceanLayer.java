package com.igteam.immersivegeology.common.world.layer;

import com.igteam.immersivegeology.common.world.util.IGLayerUtil;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;

public enum OceanLayer implements ICastleTransformer
{
    INSTANCE;

    @Override
    public int apply(INoiseRandom context, int north, int west, int south, int east, int center)
    {
        if (IGLayerUtil.isOcean(center))
        {
            if (!IGLayerUtil.isOcean(north) || !IGLayerUtil.isOcean(west) || !IGLayerUtil.isOcean(south) || !IGLayerUtil.isOcean(east))
            {
                return IGLayerUtil.OCEAN;
            }
            else if (north == IGLayerUtil.DEEP_OCEAN_RIDGE || west == IGLayerUtil.DEEP_OCEAN_RIDGE || south == IGLayerUtil.DEEP_OCEAN_RIDGE || east == IGLayerUtil.DEEP_OCEAN_RIDGE)
            {
                if (context.random(3) == 0)
                {
                    return IGLayerUtil.DEEP_OCEAN_RIDGE;
                }
            }
        }
        return center;
    }
}
