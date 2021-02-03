package com.igteam.immersivegeology.client.guidebook.helper;

import com.igteam.immersivegeology.ImmersiveGeology;
import com.igteam.immersivegeology.client.guidebook.helper.data.IGGuideLink;
import net.minecraft.util.ResourceLocation;

public class PageButton {
    private String button_name;
    private IGGuideLink button_link;
    private String button_image;
    private int x_position;
    private int y_position;
    private int width;
    private int height;
    private int tex_x;
    private int tex_y;

    public PageButton(String name, IGGuideLink link, String image, int x, int y, int width, int height, int tex_x, int tex_y){
        this.button_name = name;
        this.button_link = link;
        this.button_image = image;
        this.x_position = x;
        this.y_position = y;
        this.width = width;
        this.height = height;
        this.tex_x = tex_x;
        this.tex_y = tex_y;
    }

    public int getX() { return x_position; }
    public int getY() { return y_position; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public int getTex_x() {
        return tex_x;
    }

    public void setTex_x(int tex_x) {
        this.tex_x = tex_x;
    }

    public int getTex_y() {
        return tex_y;
    }

    public void setTex_y(int tex_y) {
        this.tex_y = tex_y;
    }

    public String getButton_name() {
        return button_name;
    }

    public void setButton_name(String button_name) {
        this.button_name = button_name;
    }

    public IGGuideLink getButton_link() {
        return button_link;
    }

    public void setButton_link(IGGuideLink button_link) {
        this.button_link = button_link;
    }

    public void setButton_image(String image) {
        this.button_image = image;
    }

    public void setX_position(int x_position) {
        this.x_position = x_position;
    }

    public void setY_position(int y_position) {
        this.y_position = y_position;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImage() {
        return button_image;
    }

    public ResourceLocation getResourceImage() { return new ResourceLocation(ImmersiveGeology.MODID, button_image); }
}
