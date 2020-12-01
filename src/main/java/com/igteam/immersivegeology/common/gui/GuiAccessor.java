package com.igteam.immersivegeology.common.gui;


import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.common.gui.GuiHandler;
import blusunrize.immersiveengineering.common.gui.IEBaseContainer;
import com.igteam.immersivegeology.ImmersiveGeology;
import com.igteam.immersivegeology.client.gui.CrudeForgeScreen;
import com.igteam.immersivegeology.common.gui.container.CrudeForgeContainer;
import com.igteam.immersivegeology.common.tileentity.entities.CrudeForgeTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.FurnaceContainer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.*;

import static com.igteam.immersivegeology.ImmersiveGeology.MODID;

/**
 * Code Pulled and modified via ImmersiveEngineering 1.14 branch
 */
public class GuiAccessor {

    //public static List<ContainerType<?>> CONTAINER_INFO = new ArrayList<>();
    public static Map<ResourceLocation, ContainerType<?>> CONTAINER_INFO = new HashMap<>();

    public static void commonInitialization(){
        //Access IE's Handler, required so I can piggyback their tileentity interation stuff, although doesn't seem to be able to register properly
       // GuiHandler.register(CrudeForgeTileEntity.class, GuiLib.CRUDE_FORGE_GUI_ID, CrudeForgeContainer::new); //screen registry ends up null? need to get container type again?
        ContainerType<? extends Container> crudeContainerType = new ContainerType(CrudeForgeContainer::new);
        CONTAINER_INFO.put(GuiLib.CRUDE_FORGE_GUI_ID, crudeContainerType);
    }

    public void clientInitialize(){
        registerScreen(GuiLib.CRUDE_FORGE_GUI_ID, CrudeForgeScreen::new);
    }

    public <C extends Container, S extends Screen & IHasContainer<C>>
    void registerScreen(ResourceLocation containerName, ScreenManager.IScreenFactory<C, S> factory)
    {
        ContainerType<C> type = (ContainerType<C>)CONTAINER_INFO.get(containerName);
        ScreenManager.registerFactory(type, factory);
    }
}
