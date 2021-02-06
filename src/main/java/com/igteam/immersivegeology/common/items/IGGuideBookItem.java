package com.igteam.immersivegeology.common.items;

import com.igteam.immersivegeology.client.gui.GuideBookScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class IGGuideBookItem extends IGBaseItem {
    private int homepage_id;
    public IGGuideBookItem(String name,int homepage_id) {
        super(name);
        this.homepage_id = homepage_id;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World p_77659_1_, PlayerEntity p_77659_2_, Hand p_77659_3_) {
        Minecraft.getInstance().displayGuiScreen(new GuideBookScreen(new StringTextComponent("field_guide"), homepage_id));
        return super.onItemRightClick(p_77659_1_, p_77659_2_, p_77659_3_);
    }
}
