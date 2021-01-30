package com.igteam.immersivegeology.common.items;

import blusunrize.immersiveengineering.client.ClientProxy;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces;
import com.igteam.immersivegeology.ImmersiveGeology;
import com.igteam.immersivegeology.api.materials.Material;
import com.igteam.immersivegeology.api.materials.MaterialUseType;
import com.igteam.immersivegeology.client.menu.helper.IIGSubGroupContained;
import com.igteam.immersivegeology.client.menu.helper.ItemSubGroup;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class IGStoreageItem extends BucketItem implements IEItemInterfaces.IColouredItem, IIGSubGroupContained {

    public final Material material;
    public final MaterialUseType subtype;
    public final String itemName;


    public IGStoreageItem(Fluid p_i49025_1_, Material material, MaterialUseType subtype) {
        super(p_i49025_1_, new Properties().containerItem(Items.BUCKET).maxStackSize(1).group(ImmersiveGeology.IG_ITEM_GROUP));
        setRegistryName(ImmersiveGeology.MODID, "item_bucket_" + material.getName());
        this.material = material;
        this.subtype = subtype;
        this.itemName = "item_bucket_" + material.getName();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public FontRenderer getFontRenderer(ItemStack stack)
    {
        return ClientProxy.itemFont;
    }

    @Override
    public ItemSubGroup getSubGroup() {
        return ItemSubGroup.misc;
    }

    @Override
    public boolean hasCustomItemColours()
    {
        return true;
    }

    @Override
    public int getColourForIEItem(ItemStack stack, int pass)
    {
        if(pass > 0) {
            return material.getColor(0);
        } else {
            return 0xFFFFFF;
        }
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack)
    {
        ArrayList<String> localizedNames = new ArrayList<>();
        localizedNames.add(I18n.format("material."+material.getModID()+"."+material.getName()+".name"));
        TranslationTextComponent name = new TranslationTextComponent("item."+ ImmersiveGeology.MODID+"."+ this.subtype.getName().toLowerCase(Locale.ENGLISH) +".name", localizedNames.toArray(new Object[localizedNames.size()]));
        return name;
    }
}
