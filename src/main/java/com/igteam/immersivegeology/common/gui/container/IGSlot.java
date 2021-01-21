package com.igteam.immersivegeology.common.gui.container;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.common.gui.IESlot;
import com.igteam.immersivegeology.common.items.IGMaterialResourceItem;
import com.igteam.immersivegeology.common.items.IGMaterialYieldItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;

public class IGSlot extends IESlot {
    public IGSlot(Container container, IInventory inv, int id, int x, int y) {
        super(container, inv, id, x, y);
    }

    public boolean isItemValid(ItemStack itemStack) {
        return !itemStack.isEmpty() && itemStack.getItem() instanceof IGMaterialYieldItem;
    }
}