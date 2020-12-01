package com.igteam.immersivegeology.common.world.biome.biomes.overworld;

import com.igteam.immersivegeology.common.world.biome.IGBiome;
import com.igteam.immersivegeology.common.world.biome.IGDefaultBiomeFeatures;
import com.igteam.immersivegeology.common.world.gen.feature.IGFeatures;
import com.igteam.immersivegeology.common.world.gen.surface.util.SurfaceBlockType;
import com.igteam.immersivegeology.common.world.noise.INoise2D;
import com.igteam.immersivegeology.common.world.noise.SimplexNoise2D;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.MineshaftConfig;
import net.minecraft.world.gen.feature.structure.MineshaftStructure;
import net.minecraft.world.gen.feature.structure.PillagerOutpostConfig;
import net.minecraft.world.gen.feature.structure.VillageConfig;

import javax.annotation.Nonnull;

import static com.igteam.immersivegeology.common.world.gen.config.ImmersiveGenerationSettings.SEA_LEVEL;

public class PlainsBiome extends IGBiome
{
	private final float minHeight;
	private final float maxHeight;
	private final PlainsType plainType;
	
	public PlainsBiome(float minHeight, float maxHeight, PlainsType type)
	{
		super(new Builder().category(Category.PLAINS).precipitation(RainType.RAIN).downfall(0.4f).temperature(0.65f), 0.65f, 0.4f);
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;

		this.addStructure(Feature.MINESHAFT, new MineshaftConfig(0.004D, MineshaftStructure.Type.NORMAL));
		this.addStructure(Feature.STRONGHOLD, IFeatureConfig.NO_FEATURE_CONFIG);
		this.addStructure(IGFeatures.IMMERSIVE_VILLAGE, new VillageConfig("village/plains/town_centers", 6));
		this.addStructure(Feature.PILLAGER_OUTPOST, new PillagerOutpostConfig(0.004D));

		this.plainType = type;
		if(type != PlainsType.GLACIER) {
			IGDefaultBiomeFeatures.addCarvers(this);
			DefaultBiomeFeatures.addGrass(this);
			DefaultBiomeFeatures.addTaigaRocks(this);

			DefaultBiomeFeatures.func_222283_Y(this);
			DefaultBiomeFeatures.func_222299_R(this);
			this.addSpawn(EntityClassification.CREATURE, new SpawnListEntry(EntityType.SHEEP, 12, 4, 4));
			this.addSpawn(EntityClassification.CREATURE, new SpawnListEntry(EntityType.PIG, 10, 4, 4));
			this.addSpawn(EntityClassification.CREATURE, new SpawnListEntry(EntityType.CHICKEN, 10, 4, 4));
			this.addSpawn(EntityClassification.CREATURE, new SpawnListEntry(EntityType.COW, 8, 4, 4));
			this.addSpawn(EntityClassification.CREATURE, new SpawnListEntry(EntityType.HORSE, 5, 2, 6));
			this.addSpawn(EntityClassification.CREATURE, new SpawnListEntry(EntityType.DONKEY, 1, 1, 3));
		} else {
			DefaultBiomeFeatures.addIcebergs(this);
			DefaultBiomeFeatures.addBlueIce(this);
			DefaultBiomeFeatures.addFreezeTopLayer(this);
		}

		DefaultBiomeFeatures.addMonsterRooms(this);
		this.addSpawn(EntityClassification.AMBIENT, new SpawnListEntry(EntityType.BAT, 10, 8, 8));
		this.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.SPIDER, 100, 4, 4));
		this.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.ZOMBIE, 95, 4, 4));
		this.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.ZOMBIE_VILLAGER, 5, 1, 1));
		this.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.SKELETON, 100, 4, 4));
		this.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.CREEPER, 100, 4, 4));
		this.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.SLIME, 100, 4, 4));
		this.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.ENDERMAN, 10, 1, 4));
		this.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.WITCH, 5, 1, 1));
		DefaultBiomeFeatures.addStructures(this);
	}

	@Nonnull
	@Override
	public INoise2D createNoiseLayer(long seed)
	{
		return new SimplexNoise2D(seed).octaves(6).spread(0.17f).scaled(SEA_LEVEL+minHeight, SEA_LEVEL+maxHeight);
	}

	@Override
	public BlockState returnBlockType(SurfaceBlockType part, float chunkTemp, float chunkRain)
	{
		switch(plainType) {
		case GLACIER:
			switch(part)
			{
				case grass:
					return Blocks.ICE.getDefaultState();
				default:
					return Blocks.PACKED_ICE.getDefaultState();
			}
		case DEFAULT:
		default:
			switch(part)
			{
				case grass:
					return Blocks.GRASS_BLOCK.getDefaultState();
				default:
					return Blocks.DIRT.getDefaultState();
			}
		}

	}
	
	public enum PlainsType{
		DEFAULT,
		GLACIER
	}
	
}