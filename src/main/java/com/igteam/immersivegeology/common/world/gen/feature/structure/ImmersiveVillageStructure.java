package com.igteam.immersivegeology.common.world.gen.feature.structure;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.MarginedStructureStart;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.structure.VillagePieces;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;
import java.util.function.Function;

public class ImmersiveVillageStructure extends Structure<VillageConfig> {
    public ImmersiveVillageStructure(Function<Dynamic<?>, ? extends VillageConfig> p_i51419_1_) {
        super(p_i51419_1_);
    }

    protected ChunkPos getStartPositionForPosition(ChunkGenerator<?> p_211744_1_, Random p_211744_2_, int p_211744_3_, int p_211744_4_, int p_211744_5_, int p_211744_6_) {
        int lvt_7_1_ = p_211744_1_.getSettings().getVillageDistance();
        int lvt_8_1_ = p_211744_1_.getSettings().getVillageSeparation();
        int lvt_9_1_ = p_211744_3_ + lvt_7_1_ * p_211744_5_;
        int lvt_10_1_ = p_211744_4_ + lvt_7_1_ * p_211744_6_;
        int lvt_11_1_ = lvt_9_1_ < 0 ? lvt_9_1_ - lvt_7_1_ + 1 : lvt_9_1_;
        int lvt_12_1_ = lvt_10_1_ < 0 ? lvt_10_1_ - lvt_7_1_ + 1 : lvt_10_1_;
        int lvt_13_1_ = lvt_11_1_ / lvt_7_1_;
        int lvt_14_1_ = lvt_12_1_ / lvt_7_1_;
        ((SharedSeedRandom)p_211744_2_).setLargeFeatureSeedWithSalt(p_211744_1_.getSeed(), lvt_13_1_, lvt_14_1_, 10387312);
        lvt_13_1_ *= lvt_7_1_;
        lvt_14_1_ *= lvt_7_1_;
        lvt_13_1_ += p_211744_2_.nextInt(lvt_7_1_ - lvt_8_1_);
        lvt_14_1_ += p_211744_2_.nextInt(lvt_7_1_ - lvt_8_1_);
        return new ChunkPos(lvt_13_1_, lvt_14_1_);
    }

    public boolean hasStartAt(ChunkGenerator<?> p_202372_1_, Random p_202372_2_, int p_202372_3_, int p_202372_4_) {
        ChunkPos lvt_5_1_ = this.getStartPositionForPosition(p_202372_1_, p_202372_2_, p_202372_3_, p_202372_4_, 0, 0);
        if (p_202372_3_ == lvt_5_1_.x && p_202372_4_ == lvt_5_1_.z) {
            Biome lvt_6_1_ = p_202372_1_.getBiomeProvider().getBiome(new BlockPos((p_202372_3_ << 4) + 9, 0, (p_202372_4_ << 4) + 9));
            return p_202372_1_.hasStructure(lvt_6_1_, Feature.VILLAGE);
        } else {
            return false;
        }
    }

    public IStartFactory getStartFactory() {
        return net.minecraft.world.gen.feature.structure.VillageStructure.Start::new;
    }

    public String getStructureName() {
        return "Village";
    }

    public int getSize() {
        return 8;
    }

    public static class Start extends MarginedStructureStart {
        public Start(Structure<?> p_i51110_1_, int p_i51110_2_, int p_i51110_3_, Biome p_i51110_4_, MutableBoundingBox p_i51110_5_, int p_i51110_6_, long p_i51110_7_) {
            super(p_i51110_1_, p_i51110_2_, p_i51110_3_, p_i51110_4_, p_i51110_5_, p_i51110_6_, p_i51110_7_);
        }

        public void init(ChunkGenerator<?> p_214625_1_, TemplateManager p_214625_2_, int p_214625_3_, int p_214625_4_, Biome p_214625_5_) {
            VillageConfig lvt_6_1_ = (VillageConfig)p_214625_1_.getStructureConfig(p_214625_5_, Feature.VILLAGE);
            BlockPos lvt_7_1_ = new BlockPos(p_214625_3_ * 16, 96, p_214625_4_ * 16);
            VillagePieces.func_214838_a(p_214625_1_, p_214625_2_, lvt_7_1_, this.components, this.rand, lvt_6_1_);
            this.recalculateStructureSize();
        }
    }
}
