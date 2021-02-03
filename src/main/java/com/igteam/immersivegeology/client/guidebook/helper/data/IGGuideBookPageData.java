package com.igteam.immersivegeology.client.guidebook.helper.data;

import com.igteam.immersivegeology.ImmersiveGeology;
import com.igteam.immersivegeology.client.guidebook.helper.PageButton;
import com.igteam.immersivegeology.client.guidebook.helper.PageType;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class IGGuideBookPageData {
	private String name;
	private int id;
	private String tooltip;
	private PageType page_type;
	private boolean is_subpage;
	private String text;
	private int text_yOffset;
	private String graphic_location;
	private GraphicData graphic_data;
	private PageButton[] page_buttons;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public PageType getPage_type() {
		return page_type;
	}

	public void setPage_type(PageType page_type) {
		this.page_type = page_type;
	}

	public boolean isIs_subpage() {
		return is_subpage;
	}

	public void setIs_subpage(boolean is_subpage) {
		this.is_subpage = is_subpage;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getGraphic_location() {
		return graphic_location;
	}

	public ResourceLocation getGraphicLocation() {
		return graphic_location != null ? new ResourceLocation(ImmersiveGeology.MODID, graphic_location) : null;
	}

	public void setGraphic_location(String graphic_location) {
		this.graphic_location = graphic_location;
	}

	public GraphicData getGraphic_data() {
		return graphic_data;
	}

	public void setGraphic_data(GraphicData graphic_data) {
		this.graphic_data = graphic_data;
	}

	public PageButton[] getPage_buttons() {
		return page_buttons;
	}

	public void setPage_buttons(PageButton[] page_buttons) {
		this.page_buttons = page_buttons;
	}

	public int getText_yOffset() {
		return (new Integer(text_yOffset) != null) ? text_yOffset : 0;
	}

	public void setText_yOffset(int text_yOffset) {
		this.text_yOffset = text_yOffset;
	}

	public static class GraphicData {
		private int xpos, ypos, width, height;

		public int getXpos() {
			return xpos;
		}

		public void setXpos(int xpos) {
			this.xpos = xpos;
		}

		public int getYpos() {
			return ypos;
		}

		public void setYpos(int ypos) {
			this.ypos = ypos;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}
	}
}
