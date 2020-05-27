package com.igteam.immersivegeology.common.world.biome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.igteam.immersivegeology.api.materials.Material;
import com.igteam.immersivegeology.common.blocks.IGBaseBlock;

import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;

public class BiomeLayerData {

	private ArrayList<IGBaseBlock> layerMap = new ArrayList<IGBaseBlock>();
	private ArrayList<LayerOre> layerOreMap = new ArrayList<LayerOre>();

	private Biome lbiome;
	private float baseHardnessMod;

	public BiomeLayerData(Biome biome, float baseHardnessMod) {
		this.lbiome = biome;
		this.baseHardnessMod = baseHardnessMod;
	}
	
	public ArrayList<IGBaseBlock> getLayers(){
		return layerMap;
	}

	public void addLayer(IGBaseBlock layerBlock) {
		layerMap.add(layerBlock);
	}

	public void addLayerOre(int rarity, int veinSize, IGBaseBlock ore) {
		layerOreMap.add(new LayerOre(rarity, veinSize, ore));
	}

	public IGBaseBlock getLayerBlock(int layerID) {
		layerID--;
		return layerMap.get(layerID);
	}

	public LayerOre getLayerOre(int layerID) {
		return layerOreMap.get(layerID);
	}

	public Biome getLbiome() {
		return lbiome;
	}

	public float getBaseHardnessMod() {
		return baseHardnessMod;
	}

	public class LayerOre {

		private int rarity, veinSize;
		private Block ore;

		public LayerOre(int rarity, int veinSize, Block ore) {
			this.rarity = rarity;
			this.veinSize = veinSize;
			this.ore = ore;
		}

		public int getRarity() {
			return rarity;
		}

		public void setRarity(int rarity) {
			this.rarity = rarity;
		}

		public int getVeinSize() {
			return veinSize;
		}

		public void setVeinSize(int veinSize) {
			this.veinSize = veinSize;
		}

		public Block getOre() {
			return ore;
		}

		public void setOre(Block ore) {
			this.ore = ore;
		}
	}

	public int getLayerCount() {
		// TODO Auto-generated method stub
		return this.layerMap.size();
	}
}
