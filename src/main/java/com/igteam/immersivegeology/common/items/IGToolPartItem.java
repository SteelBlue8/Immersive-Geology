package com.igteam.immersivegeology.common.items;

import com.igteam.immersivegeology.api.materials.Material;
import com.igteam.immersivegeology.api.materials.MaterialUseType;
import com.igteam.immersivegeology.api.toolsystem.Tooltypes;

public class IGToolPartItem extends IGMaterialResourceItem {
    private Tooltypes.EnumToolPart tool_part;
    public IGToolPartItem(Tooltypes.EnumToolPart part, MaterialUseType key, Material... materials) {
        super(key, materials);
        tool_part = part;
    }
}
