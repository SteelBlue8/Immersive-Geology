package com.igteam.immersivegeology.common.world.gen.carver;

import com.igteam.immersivegeology.api.materials.Material;
import com.igteam.immersivegeology.api.materials.MaterialUseType;
import com.igteam.immersivegeology.api.util.IGMathHelper;
import com.igteam.immersivegeology.api.util.IGRegistryGrabber;
import com.igteam.immersivegeology.common.blocks.IGMaterialBlock;
import com.igteam.immersivegeology.common.blocks.IIGOreBlock;
import com.igteam.immersivegeology.common.blocks.property.IGProperties;
import com.igteam.immersivegeology.common.materials.EnumMaterials;
import com.igteam.immersivegeology.common.util.IGLogger;
import com.igteam.immersivegeology.common.world.biome.IGBiome;
import com.igteam.immersivegeology.common.world.chunk.ChunkGeneratorImmersiveOverworld;
import com.igteam.immersivegeology.common.world.layer.BiomeLayerData;
import com.igteam.immersivegeology.common.world.layer.BiomeLayerData.LayerOre;
import com.igteam.immersivegeology.common.world.layer.wld.WorldLayerData;
import com.igteam.immersivegeology.common.world.noise.INoise3D;
import com.igteam.immersivegeology.common.world.noise.NoiseUtil;
import com.igteam.immersivegeology.common.world.noise.OpenSimplexNoise;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;

import java.util.*;

public class WorleyOreCarver {
	private static final int SAMPLE_HEIGHT = 64;
	private static float NOISE_THRESHOLD = 0.35f;
	private static final float FEATURE_SIZE = 16;

	private static final BlockState BEDROCK = Blocks.BEDROCK.getDefaultState();

	public WorleyOreCarver() {}
	public static HashMap<Integer, INoise3D> finalNoiseData = new HashMap<>();

	public void createOreType(Random seedGenerator, EnumMaterials ore) {
		IGLogger.info("Generating "+ore.material.getName().toLowerCase() + " Ore Noise");
		OpenSimplexNoise oreNoiseWorley = new OpenSimplexNoise(seedGenerator.nextLong() + ore.ordinal());
		OpenSimplexNoise oreNoiseWorleySub = new OpenSimplexNoise(seedGenerator.nextLong() - ore.ordinal());

		//this is to make ore veins less common (without this it's way too common)
		INoise3D oreNoiseSub = (x, y, z) -> {
			return oreNoiseWorleySub.flattened(0.4f, 1f).octaves(1, 0.75f).noise(x/(FEATURE_SIZE*5), y/(FEATURE_SIZE*5), z/(FEATURE_SIZE*5));
		};

		INoise3D oreNoise = (x, y, z) -> {
			return oreNoiseWorley.flattened(0f, 1f).octaves(3, 0.8f).noise(x/FEATURE_SIZE, y/FEATURE_SIZE, z/FEATURE_SIZE);
		};

		finalNoiseData.put(ore.ordinal(), oreNoise.sub(oreNoiseSub));
	}

	HashMap<String, BiomeLayerData> worldLayerData = WorldLayerData.instance.worldLayerData;

	public void carve(IChunk chunkIn, int chunkX, int chunkZ) {

		// run this through a full loop, for each ore, returns out if ore is not set in the biomes layer data!
		for(BiomeLayerData biomeData : worldLayerData.values()) {
			if (biomeData == null) {
				IGLogger.error("No IG Biome Found at Pos: " + chunkIn.getPos().asBlockPos().toString());
				return;
			}

			int totalLayerCount = biomeData.getLayerCount();
			for (int currentLayer = totalLayerCount; currentLayer > 0; currentLayer--) {
				ArrayList<LayerOre> oreArrayData = biomeData.getLayerOre(currentLayer);
				if (oreArrayData == null) {
					IGLogger.error("Ore Array Data is missing for biome:" + biomeData.getLbiome().getDisplayName().getFormattedText());
					return;
				}

				for (LayerOre oreData : oreArrayData) {
					if (oreData == null) {
						return;
					}

					int ordinal = oreData.getOre().ordinal();
					EnumMaterials oreMaterial = oreData.getOre();

					float coverage = oreData.getCoverage();

					NOISE_THRESHOLD = (1 - coverage);

					//We need to make sure that ore won't replace EVERYTHING, so a min of 0.25 is required.
					if (NOISE_THRESHOLD < 0.35) {
						NOISE_THRESHOLD = 0.35f;
					}

					if (NOISE_THRESHOLD > 0.8) {
						NOISE_THRESHOLD = 0.8f; //makes it so 'rare' ores actually have a chance to spawn!
					}

					//  if their is no noise array for material, return out of function again!
					// (This should never be the case, but this is incase someone does a stupid
					if (!finalNoiseData.containsKey(oreMaterial.ordinal())) {
						IGLogger.error("No Noise Map found for " + oreMaterial.toString().toLowerCase());
						return;
					}

					INoise3D oreNoise = finalNoiseData.get(ordinal);

					float[] noiseValues = new float[5 * 5 * SAMPLE_HEIGHT];

					// Sample initial noise values
					for (int x = 0; x < 5; x++) {
						for (int z = 0; z < 5; z++) {
							for (int y = 0; y < SAMPLE_HEIGHT; y++) {
								noiseValues[x + (z * 5) + (y * 25)] = oreNoise.noise((chunkX + x * 4), (y * 7.3f), (chunkZ + z * 4));
							}
						}
					}

					float[] section = new float[16 * 16];
					float[] prevSection = null;
					BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

					// Create caves, layer by layer
					for (int y = SAMPLE_HEIGHT - 1; y >= 0; y--) {
						for (int x = 0; x < 4; x++) {
							for (int z = 0; z < 4; z++) {
								float noiseUNW = noiseValues[(x + 0) + ((z + 0) * 5) + ((y + 0) * 25)];
								float noiseUNE = noiseValues[(x + 1) + ((z + 0) * 5) + ((y + 0) * 25)];
								float noiseUSW = noiseValues[(x + 0) + ((z + 1) * 5) + ((y + 0) * 25)];
								float noiseUSE = noiseValues[(x + 1) + ((z + 1) * 5) + ((y + 0) * 25)];


								float noiseMidN, noiseMidS;

								// Lerp east-west edges
								for (int sx = 0; sx < 4; sx++) {
									// Increasing x -> moving east
									noiseMidN = NoiseUtil.lerp(noiseUNW, noiseUNE, 0.25f * sx);
									noiseMidS = NoiseUtil.lerp(noiseUSW, noiseUSE, 0.25f * sx);

									// Lerp faces
									for (int sz = 0; sz < 4; sz++) {
										// Increasing z -> moving south
										section[(x * 4 + sx) + (z * 4 + sz) * 16] = NoiseUtil.lerp(noiseMidN, noiseMidS,
												0.25f * sz);
									}
								}

								if (prevSection != null) {
									// We aren't on the first section, so we need to interpolate between sections,
									// and assign blocks from the previous section up until this one
									for (int y0 = 4 - 1; y0 >= 0; y0--) {
										int yPos = y * 4 + y0;
										float heightFadeValue = 1;
										// Replacement state for cave interior based on height
										for (int x0 = x * 4; x0 < (x + 1) * 4; x0++) {
											for (int z0 = z * 4; z0 < (z + 1) * 4; z0++) {
												// set the current position
												pos.setPos(chunkX + x0, yPos, chunkZ + z0);
												BlockState replacementState;
												float finalNoise = NoiseUtil.lerp(section[x0 + 16 * z0], prevSection[x0 + 16 * z0], 0.25f * y0);
												int totHeight = 256;
												int topOfLayer = (totHeight * currentLayer) / totalLayerCount;
												int bottomOfLayer = (((totHeight * currentLayer) / totalLayerCount) - ((totHeight * currentLayer) / totalLayerCount) / currentLayer);

												if ((yPos <= topOfLayer) && (yPos >= bottomOfLayer)) {
													Material baseMaterial = ((IGMaterialBlock) chunkIn.getBlockState(pos).getBlock()).getMaterial();
													replacementState = IGRegistryGrabber.grabBlock(MaterialUseType.ORE_BEARING, baseMaterial, oreMaterial.material).getDefaultState().with(IGProperties.NATURAL, true);
													//Run spawn in here to avoid creating ore outside of the correct layer.

													float richnessNoise = finalNoise;
													richnessNoise *= heightFadeValue;

													if (richnessNoise > NOISE_THRESHOLD) {

														IGLogger.info("I SHOULD BE GENERATING");
														int richness = 1;

														if (richnessNoise >= NOISE_THRESHOLD + (0.8 - NOISE_THRESHOLD) * 0.73) {
															richness = 3; // DENSE
														} else if (richnessNoise >= NOISE_THRESHOLD + (0.8 - NOISE_THRESHOLD) * 0.55) {
															richness = 2; // RICH
														} else if (richnessNoise >= NOISE_THRESHOLD + (0.8 - NOISE_THRESHOLD) * 0.3) {
															richness = 1; // NORMAL
														} else {
															richness = 0;
														}
														// Create ore if possible
														BlockState originalState = chunkIn.getBlockState(pos);
														if (!(originalState.getBlock() instanceof IIGOreBlock)) {
															IGLogger.info("I AM GENERATING");
															chunkIn.setBlockState(pos, replacementState.with(IGProperties.ORE_RICHNESS, richness), false);
														}
													}

												}
											}
										}
									}
								}
							}
						}
						// End of x/z iteration
					}
					// End of x/z loop, so move section to previous
					prevSection = Arrays.copyOf(section, section.length);
				}
			}
		}
	}
}
