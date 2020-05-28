package com.igteam.immersivegeology.common.world.layer;

import java.util.function.IntPredicate;

import com.google.common.base.Predicate;
import com.igteam.immersivegeology.common.world.util.IGLayerUtil;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;

public enum ShoreLayer implements ICastleTransformer
{
    INSTANCE;

    @Override
    public int apply(INoiseRandom context, int north, int west, int south, int east, int center)
    {
        Predicate<IntPredicate> matcher = p -> p.test(north) || p.test(west) || p.test(south) || p.test(east);
        if (IGLayerUtil.isMountains(center))
        {
            if (matcher.test(IGLayerUtil::isOcean))
            {
                return IGLayerUtil.STONE_SHORE;
            }
            else if (matcher.test(i -> !IGLayerUtil.isMountains(i)))
            {
                return IGLayerUtil.MOUNTAINS_EDGE;
            }
        }
        else if (!IGLayerUtil.isOcean(center) && IGLayerUtil.isShoreCompatible(center))
        {
            if (matcher.test(IGLayerUtil::isOcean))
            {
                return IGLayerUtil.SHORE;
            }
        }
        else if (center == IGLayerUtil.DEEP_OCEAN || center == IGLayerUtil.DEEP_OCEAN_RIDGE)
        {
            if (matcher.test(i -> i == IGLayerUtil.OCEAN))
            {
                return IGLayerUtil.OCEAN;
            }
        }
        return center;
    }
}
