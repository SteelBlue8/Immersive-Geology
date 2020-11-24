package com.igteam.immersivegeology.common.world.gen.carver.controller;

import com.igteam.immersivegeology.common.world.biome.IGBiome;
import com.igteam.immersivegeology.common.world.gen.carver.RavineCarver;
import net.minecraft.block.BlockState;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;

import java.util.BitSet;
import java.util.Map;

public class RavineController {
    private IWorld world;
    private long seed;
    private SharedSeedRandom random = new SharedSeedRandom();

    private ConfiguredCarver<ProbabilityConfig> ravineCarver;

    public RavineController(IWorld world){
        this.world = world;
        this.seed = world.getSeed();
        this.ravineCarver = new ConfiguredCarver<>(new RavineCarver(world, ProbabilityConfig::deserialize), new ProbabilityConfig(0.01f));
    }


    public void carveChunk(IChunk chunkIn, int chunkX, int chunkZ, BlockState[][] liquidBlocks, Map<Long, IGBiome> biomeMap, BitSet airCarvingMask, BitSet liquidCarvingMask) {
        for (int currChunkX = chunkX - 8; currChunkX <= chunkX + 8; currChunkX++) {
            for (int currChunkZ = chunkZ - 8; currChunkZ <= chunkZ + 8; currChunkZ++) {
                random.setLargeFeatureSeed(seed, currChunkX, currChunkZ);
                if (ravineCarver.shouldCarve(random, chunkX, chunkZ)) {
                    ((RavineCarver) ravineCarver.carver).carve(chunkIn, random, world.getSeaLevel(), currChunkX, currChunkZ, chunkX, chunkZ, liquidBlocks, biomeMap, airCarvingMask, liquidCarvingMask);
                }
            }
        }
    }

    public void setWorld(IWorld worldIn) {
        this.world = worldIn;
        ((RavineCarver)this.ravineCarver.carver).setWorld(worldIn);
    }
}
