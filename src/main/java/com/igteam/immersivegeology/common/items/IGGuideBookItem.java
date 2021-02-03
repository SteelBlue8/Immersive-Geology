package com.igteam.immersivegeology.common.items;

import com.igteam.immersivegeology.client.gui.GuideBookScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.StringTextComponent;

public class IGGuideBookItem extends IGBaseItem {
    public IGGuideBookItem(String name) {
        super(name);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext p_195939_1_) {
        Minecraft.getInstance().displayGuiScreen(new GuideBookScreen(new StringTextComponent("Test Screen")));
        return super.onItemUse(p_195939_1_);
    }
}
