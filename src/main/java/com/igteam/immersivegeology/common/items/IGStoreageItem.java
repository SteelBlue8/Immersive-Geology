package com.igteam.immersivegeology.common.items;

import blusunrize.immersiveengineering.common.items.IEItemInterfaces;
import com.igteam.immersivegeology.ImmersiveGeology;
import com.igteam.immersivegeology.api.materials.Material;
import com.igteam.immersivegeology.client.menu.helper.IIGSubGroupContained;
import com.igteam.immersivegeology.client.menu.helper.ItemSubGroup;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;

public class IGStoreageItem extends BucketItem implements IEItemInterfaces.IColouredItem, IIGSubGroupContained {
    public IGStoreageItem(Fluid p_i49025_1_, Material material) {
        super(p_i49025_1_, new Properties().containerItem(Items.BUCKET).maxStackSize(1).group(ImmersiveGeology.IG_ITEM_GROUP));
        setRegistryName(ImmersiveGeology.MODID, "item_bucket_" + material.getName());
    }

    @Override
    public ItemSubGroup getSubGroup() {
        return ItemSubGroup.misc;
    }
}
