package com.igteam.immersivegeology.common.gui;


import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.common.gui.IEBaseContainer;
import com.igteam.immersivegeology.common.gui.container.CrudeForgeContainer;
import com.igteam.immersivegeology.common.tileentity.entities.CrudeForgeTileEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.IContainerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.igteam.immersivegeology.ImmersiveGeology.MODID;

/**
 * Code Pulled and modified via ImmersiveEngineering 1.14 branch
 */
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GuiHandler {
    private static final Map<Class<? extends TileEntity>, TileContainer<?,?>> TILE_CONTAINERS = new HashMap<>();
    private static final Map<ResourceLocation, ContainerType<?>> ALL_TYPES = new HashMap<>();

    public static void commonInitialization(){
        register(CrudeForgeTileEntity.class, GuiLib.CRUDE_FORGE_GUI_ID, CrudeForgeContainer::new);
    }

    public static <T extends TileEntity, C extends IEBaseContainer<? super T>>
    void register(Class<T> tileClass, ResourceLocation name,
                  blusunrize.immersiveengineering.common.gui.GuiHandler.TileContainerConstructor<T, C> container)
    {
        ContainerType<C> type = new ContainerType<>((IContainerFactory<C>)(windowId, inv, data) -> {
            World world = ImmersiveEngineering.proxy.getClientWorld();
            BlockPos pos = data.readBlockPos();
            TileEntity te = world.getTileEntity(pos);
            return container.construct(windowId, inv, (T)te);
        });
        type.setRegistryName(name);
        TILE_CONTAINERS.put(tileClass, new TileContainer<>(type, container));
        ALL_TYPES.put(name, type);
    }

    private static class TileContainer<T extends TileEntity, C extends IEBaseContainer<? super T>>
    {
        final ContainerType<C> type;
        final blusunrize.immersiveengineering.common.gui.GuiHandler.TileContainerConstructor<T, C> factory;

        private TileContainer(ContainerType<C> type, blusunrize.immersiveengineering.common.gui.GuiHandler.TileContainerConstructor<T, C> factory)
        {
            this.type = type;
            this.factory = factory;
        }
    }
}
