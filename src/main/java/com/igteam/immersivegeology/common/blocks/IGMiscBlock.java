package com.igteam.immersivegeology.common.blocks;

import com.igteam.immersivegeology.api.materials.Material;
import com.igteam.immersivegeology.api.materials.MaterialUseType;

public class IGMiscBlock extends IGMaterialBlock {

    private String custom_tex_name;

    public IGMiscBlock(MaterialUseType subtype, String customTextureName, Material... materials) {
        super(subtype, materials);
        setCustomTextureName(customTextureName);
    }

    public void setCustomTextureName(String cn){
        custom_tex_name = cn;
    }

    public String getCustom_tex_name() {
        return custom_tex_name;
    }
}
