package com.igteam.immersivegeology.client.gui;

import blusunrize.immersiveengineering.client.ClientUtils;
import com.igteam.immersivegeology.ImmersiveGeology;
import com.igteam.immersivegeology.client.guidebook.helper.PageButton;
import com.igteam.immersivegeology.client.guidebook.helper.data.IGGuideBookPageData;
import com.igteam.immersivegeology.client.guidebook.importer.IGGuideBookDataHolder;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import javax.annotation.Nonnull;

public class GuideBookScreen extends Screen {

    private int homepage_id;
    private IGGuideBookDataHolder INSTANCE = IGGuideBookDataHolder.INSTANCE;
    private IGGuideBookPageData CURRENT_PAGE;

    int left = (int) Math.floor((this.width - 198) / 1.75);
    int top = (this.height - 148) / 5;

    public GuideBookScreen(ITextComponent p_i51108_1_, int id) {
        super(p_i51108_1_);
        this.minecraft = Minecraft.getInstance();
        homepage_id = id;
        this.CURRENT_PAGE = INSTANCE.guide_book_data.get(homepage_id);
    }

    @Override
    public void render(int mx, int my, float partialTicks) {
        left = (int) Math.floor((this.width - 198) / 1.75) + 12;
        top = ((this.height - 148) / 5) + 12;
        this.renderBackground();
        //update gui positions
        this.font.drawString(new TranslationTextComponent(CURRENT_PAGE.getName()).getFormattedText(), left + 12, top + 12, 0);

        if(CURRENT_PAGE.getGraphicLocation() != null){
            ClientUtils.bindTexture(CURRENT_PAGE.getGraphicLocation().toString());
            this.drawTexturedRect(left + CURRENT_PAGE.getGraphic_data().getXpos(), top + CURRENT_PAGE.getGraphic_data().getYpos(),0,0, CURRENT_PAGE.getGraphic_data().getWidth(), CURRENT_PAGE.getGraphic_data().getHeight());
        }

        if(CURRENT_PAGE.getText() != null)
        {
            String text = new TranslationTextComponent(CURRENT_PAGE.getText()).getFormattedText();
            this.font.drawSplitString(text, left + 12, top + CURRENT_PAGE.getText_yOffset(),120, 0);
        }

        for(PageButton b : CURRENT_PAGE.getPage_buttons()){
            ClientUtils.bindTexture(b.getResourceImage().toString());
            this.drawTexturedRect(left + b.getX(), top + b.getY(),b.getTex_x(),b.getTex_y(), b.getWidth(),b.getHeight());
        }
    }

    @Override
    public boolean mouseClicked(double mx, double my, int isPressed) {
        for(PageButton b : CURRENT_PAGE.getPage_buttons()){
           if(mx > (left + b.getX()) && my > (top + b.getY())){
               if(mx < ((left + b.getX()) + b.getWidth()) && my < ((top + b.getY()) + b.getHeight())){
                   this.CURRENT_PAGE = INSTANCE.guide_book_data.get(b.getButton_link().getPage_id());
               }
           }
        }

        return super.mouseClicked(mx,my,isPressed);
    }

    @Override
    public void renderBackground(int p_renderBackground_1_) {
        super.renderBackground(p_renderBackground_1_);
        GlStateManager.color3f(1.0F, 1.0F, 1.0F);
        ClientUtils.bindTexture(ImmersiveGeology.MODID + ":textures/gui/guide_page" + (homepage_id > 99 ? "_adv" : "") + ".png");
        this.drawTexturedRect(left, top, 0, 0, 148, 198);

    }

    private void drawTexturedRect(int xPos, int yPos, int texX, int texY, int texWidth, int texHeight){
        this.blit(xPos, yPos, texX, texY, texWidth, texHeight);
    }

}
