package com.igteam.immersivegeology.common.data;

import com.igteam.immersivegeology.api.materials.Material;
import com.igteam.immersivegeology.api.materials.MaterialUseType;
import com.igteam.immersivegeology.api.util.IGRegistryGrabber;
import com.igteam.immersivegeology.common.materials.EnumMaterials;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;

import java.util.Arrays;

public class IGBlockTagProvider extends BlockTagsProvider {
    public IGBlockTagProvider(DataGenerator p_i48256_1_) {
        super(p_i48256_1_);
    }

    @Override
    protected void registerTags() {
        super.registerTags();
        //Register Tags Automatically
        for (EnumMaterials eMat : EnumMaterials.values()) {
            Material mat = eMat.material;
            //Iterate through all materials thus almost all content we add
            if(mat.hasUsetype(MaterialUseType.LOG) && mat.hasUsetype(MaterialUseType.STRIPPED_LOG)){
                //after the conditions have been checked, register the corresponding tags as shown below.
                this.getBuilder(BlockTags.LOGS).add(new Block[]{IGRegistryGrabber.grabBlock(MaterialUseType.LOG, mat),IGRegistryGrabber.grabBlock(MaterialUseType.STRIPPED_LOG, mat)});
            }

        }
    }

    @Override
    public String getName() {
        return "IG Blocktag Provider";
    }
}
