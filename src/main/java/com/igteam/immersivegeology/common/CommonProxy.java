package com.igteam.immersivegeology.common;

import com.igteam.immersivegeology.common.gui.GuiHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class CommonProxy
{
	public void preInit()
	{

	}

	public void init()
	{
		GuiHandler.commonInitialization();
	}

	public void postInit()
	{

	}

	public void serverStarting()
	{

	}

	public void onWorldLoad()
	{

	}

	public void openTileScreen(ResourceLocation guiId, TileEntity tileEntity)
	{
	}

	public void registerContainersAndScreens(){

	}

}
