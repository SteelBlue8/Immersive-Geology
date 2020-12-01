package com.igteam.immersivegeology.common.gui.container;

import blusunrize.immersiveengineering.api.crafting.CokeOvenRecipe;
import blusunrize.immersiveengineering.common.gui.IEBaseContainer;
import blusunrize.immersiveengineering.common.gui.IESlot;
import com.igteam.immersivegeology.common.gui.GuiAccessor;
import com.igteam.immersivegeology.common.gui.GuiLib;
import com.igteam.immersivegeology.common.tileentity.entities.CrudeForgeTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class CrudeForgeContainer extends Container {

    public CrudeForgeTileEntity.CrudeForgeData data;

    public CrudeForgeContainer(int id, PlayerInventory inv) {
        this(id, inv, new CrudeForgeTileEntity());
    }

    public final CrudeForgeTileEntity tile;

    public CrudeForgeContainer(int id, PlayerInventory inventoryPlayer, CrudeForgeTileEntity tile) {
        super(GuiAccessor.CONTAINER_INFO.get(GuiLib.CRUDE_FORGE_GUI_ID), id);
        this.tile = tile;
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 9; j++)
                addSlot(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 84+i*18));
        for(int i = 0; i < 9; i++)
            addSlot(new Slot(inventoryPlayer, i, 8+i*18, 142));
        data = tile.guiData;
        trackIntArray(data);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerEntity) {
        return true;
    }
}