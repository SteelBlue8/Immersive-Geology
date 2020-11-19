package com.igteam.immersivegeology.common.gui.container;

import blusunrize.immersiveengineering.common.gui.IEBaseContainer;
import com.igteam.immersivegeology.common.tileentity.entities.CrudeForgeTileEntity;
import net.minecraft.entity.player.PlayerInventory;

public class CrudeForgeContainer extends IEBaseContainer<CrudeForgeTileEntity> {

    public CrudeForgeTileEntity.CrudeForgeData data;

    public CrudeForgeContainer(int id, PlayerInventory inventoryPlayer, CrudeForgeTileEntity tile) {
        super(inventoryPlayer, tile, id);
    }
}