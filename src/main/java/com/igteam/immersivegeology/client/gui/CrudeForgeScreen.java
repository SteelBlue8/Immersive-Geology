package com.igteam.immersivegeology.client.gui;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.IEContainerScreen;
import com.igteam.immersivegeology.ImmersiveGeology;
import com.igteam.immersivegeology.common.gui.container.CrudeForgeContainer;
import com.igteam.immersivegeology.common.tileentity.entities.CrudeForgeTileEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;

public class CrudeForgeScreen extends IEContainerScreen<CrudeForgeContainer>  {
    private CrudeForgeTileEntity tile;

    public CrudeForgeScreen(CrudeForgeContainer container, PlayerInventory inventory, ITextComponent title){
        super(container, inventory, title);
        this.tile = container.tile;
    }

    @Override
    public void render(int mx, int my, float partialTicks) {
        super.render(mx, my, partialTicks);
        ArrayList<ITextComponent> tooltip = new ArrayList<>();
        ClientUtils.handleGuiTank(tile.oreTank, guiLeft+129, guiTop+22, 16, 26, 176, 31, 20, 51, mx, my, ImmersiveGeology.MODID + ":textures/gui/crude_forge.png", tooltip);
        ClientUtils.handleGuiTank(tile.wasteTank, guiLeft+129, guiTop+53, 16, 16, 176, 31, 20, 51, mx, my, ImmersiveGeology.MODID + ":textures/gui/crude_forge.png", tooltip);

        if(!tooltip.isEmpty())
        {
            ClientUtils.drawHoveringText(tooltip, mx, my, font, guiLeft+xSize, -1);
            RenderHelper.enableGUIStandardItemLighting();
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mx, int my) {
        GlStateManager.color3f(1.0F, 1.0F, 1.0F);
        ClientUtils.bindTexture(ImmersiveGeology.MODID + ":textures/gui/crude_forge.png");
        this.blit(guiLeft, guiTop, 0, 0, xSize, ySize);
        ClientUtils.handleGuiTank(tile.oreTank, guiLeft+129, guiTop+22, 16, 26, 176, 31, 20, 51, mx, my, ImmersiveGeology.MODID + ":textures/gui/crude_forge.png", null);
        ClientUtils.handleGuiTank(tile.wasteTank, guiLeft+129, guiTop+53, 16, 16, 176, 31, 20, 51, mx, my, ImmersiveGeology.MODID + ":textures/gui/crude_forge.png", null);
    }
}