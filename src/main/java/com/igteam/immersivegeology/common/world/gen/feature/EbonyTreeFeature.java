package com.igteam.immersivegeology.common.world.gen.feature;

import com.igteam.immersivegeology.api.materials.MaterialUseType;
import com.igteam.immersivegeology.api.util.IGRegistryGrabber;
import com.igteam.immersivegeology.common.materials.EnumMaterials;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.IPlantable;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class EbonyTreeFeature extends AbstractTreeFeature<NoFeatureConfig> {

    private static final BlockState LOG;
    private static final BlockState LEAF;
    private final boolean useExtraRandomHeight;

    public EbonyTreeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> config, boolean p_i4, boolean random_height){
        super(config, p_i4);
        this.useExtraRandomHeight = random_height;
    }

    @Override
    protected boolean place(Set<BlockPos> p_208519_1_, IWorldGenerationReader p_208519_2_, Random p_208519_3_, BlockPos p_208519_4_, MutableBoundingBox p_208519_5_) {
        int i = p_208519_3_.nextInt(4) + 8;
        if (this.useExtraRandomHeight) {
            i += p_208519_3_.nextInt(8);
        }

        boolean flag = true;
        if (p_208519_4_.getY() >= 1 && p_208519_4_.getY() + i + 1 <= p_208519_2_.getMaxHeight()) {
            int l1;
            int l2;
            int i3;
            for(l1 = p_208519_4_.getY(); l1 <= p_208519_4_.getY() + 1 + i; ++l1) {
                int k = 1;
                if (l1 == p_208519_4_.getY()) {
                    k = 0;
                }

                if (l1 >= p_208519_4_.getY() + 1 + i - 2) {
                    k = 2;
                }

                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                for(l2 = p_208519_4_.getX() - k; l2 <= p_208519_4_.getX() + k && flag; ++l2) {
                    for(i3 = p_208519_4_.getZ() - k; i3 <= p_208519_4_.getZ() + k && flag; ++i3) {
                        if (l1 >= 0 && l1 < p_208519_2_.getMaxHeight()) {
                            if (!func_214587_a(p_208519_2_, blockpos$mutableblockpos.setPos(l2, l1, i3))) {
                                flag = false;
                            }
                        } else {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag) {
                return false;
            } else if (isSoil(p_208519_2_, p_208519_4_.down(), this.getSapling()) && p_208519_4_.getY() < p_208519_2_.getMaxHeight() - i - 1) {
                this.setDirtAt(p_208519_2_, p_208519_4_.down(), p_208519_4_);

                for(l1 = p_208519_4_.getY() - 3 + i; l1 <= p_208519_4_.getY() + i; ++l1) {
                    int j2 = l1 - (p_208519_4_.getY() + i);
                    int k2 = 1 - j2 / 2;

                    for(l2 = p_208519_4_.getX() - k2; l2 <= p_208519_4_.getX() + k2; ++l2) {
                        i3 = l2 - p_208519_4_.getX();

                        for(int j1 = p_208519_4_.getZ() - k2; j1 <= p_208519_4_.getZ() + k2; ++j1) {
                            int k1 = j1 - p_208519_4_.getZ();
                            if (Math.abs(i3) != k2 || Math.abs(k1) != k2 || p_208519_3_.nextInt(2) != 0 && j2 != 0) {
                                BlockPos blockpos = new BlockPos(l2, l1, j1);
                                if (isAirOrLeaves(p_208519_2_, blockpos)) {
                                    this.setLogState(p_208519_1_, p_208519_2_, blockpos, LEAF, p_208519_5_);
                                }
                            }
                        }
                    }
                }

                for(l1 = 0; l1 < i; ++l1) {
                    if (isAirOrLeaves(p_208519_2_, p_208519_4_.up(l1))) {
                        this.setLogState(p_208519_1_, p_208519_2_, p_208519_4_.up(l1), LOG, p_208519_5_);
                    }
                }

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    static {
        LOG = IGRegistryGrabber.grabBlock(MaterialUseType.LOG, EnumMaterials.Ebony.material).getDefaultState();
        LEAF = Blocks.BIRCH_LEAVES.getDefaultState();
    }
}
