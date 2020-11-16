package com.igteam.immersivegeology.client.gui;

import blusunrize.immersiveengineering.client.gui.IEContainerScreen;
import com.igteam.immersivegeology.common.gui.container.CrudeForgeContainer;
import com.igteam.immersivegeology.common.tileentity.entities.CrudeForgeTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class CrudeForgeScreen extends IEContainerScreen<CrudeForgeContainer> {
    private CrudeForgeTileEntity tile;

    public CrudeForgeScreen(CrudeForgeContainer container, PlayerInventory inventory, ITextComponent title){
        super(container, inventory, title);
        this.tile = container.tile;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i1) {

    }
}
