package com.igteam.immersivegeology.client;

import blusunrize.immersiveengineering.client.IEDefaultColourHandlers;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.gui.GuiHandler;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces;
import com.igteam.immersivegeology.client.gui.CrudeForgeScreen;
import com.igteam.immersivegeology.common.CommonProxy;
import com.igteam.immersivegeology.common.IGContent;
import com.igteam.immersivegeology.common.gui.GuiLib;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ObjectHolder;

import static com.igteam.immersivegeology.ImmersiveGeology.MODID;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID, bus = Bus.MOD)
public class ClientProxy extends CommonProxy
{

	public static Minecraft mc()
	{
		return Minecraft.getInstance();
	}

	@Override
	public void preInit()
	{
		super.preInit();
	}

	@Override
	public void init()
	{
		super.init();
		//Colour registration
		for(Item item : IGContent.registeredIGItems.values())
			if(item instanceof IEItemInterfaces.IColouredItem&&((IEItemInterfaces.IColouredItem)item).hasCustomItemColours())
				mc().getItemColors().register(IEDefaultColourHandlers.INSTANCE, item);
		for(Block block : IGContent.registeredIGBlocks.values())
			if(block instanceof IEBlockInterfaces.IColouredBlock&&((IEBlockInterfaces.IColouredBlock)block).hasCustomBlockColours())
				mc().getBlockColors().register(IEDefaultColourHandlers.INSTANCE, block);
	}


	@Override
	public void openTileScreen(ResourceLocation guiId, TileEntity tileEntity) {
		super.openTileScreen(guiId, tileEntity);
	}

	@Override
	public void registerContainersAndScreens()
	{
		super.registerContainersAndScreens();
		registerScreen(GuiLib.CRUDE_FORGE_GUI_ID, CrudeForgeScreen::new);
	}

	public <C extends Container, S extends Screen & IHasContainer<C>>
	void registerScreen(ResourceLocation containerName, ScreenManager.IScreenFactory<C, S> factory)
	{
		ContainerType<C> type = (ContainerType<C>) GuiHandler.getContainerType(containerName);
		ScreenManager.registerFactory(type, factory);
	}

	@Override
	public void postInit()
	{
		super.postInit();
	}

	@Override
	public void serverStarting()
	{
		super.serverStarting();
	}

	@Override
	public void onWorldLoad()
	{
		super.onWorldLoad();
	}
}
