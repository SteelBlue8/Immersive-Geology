package com.igteam.immersivegeology.common.gui.container;

import blusunrize.immersiveengineering.api.crafting.CokeOvenRecipe;
import blusunrize.immersiveengineering.common.gui.IEBaseContainer;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.gui.TileInventory;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import com.igteam.immersivegeology.common.gui.GuiAccessor;
import com.igteam.immersivegeology.common.gui.GuiLib;
import com.igteam.immersivegeology.common.tileentity.entities.CrudeForgeTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CrudeForgeContainer extends Container {

    public CrudeForgeTileEntity.CrudeForgeData data;

    public int slotCount;

    public CrudeForgeContainer(int id, PlayerInventory inv) {
        this(id, inv, new CrudeForgeTileEntity());
    }

    public final CrudeForgeTileEntity tile;

    public CrudeForgeContainer(int id, PlayerInventory inventoryPlayer, CrudeForgeTileEntity tile) {
        super(GuiAccessor.CONTAINER_INFO.get(GuiLib.CRUDE_FORGE_GUI_ID), id);
        this.tile = tile;

        addSlot( new IESlot.BlastFuel(this, tile.inventory, tile.SLOT_FUEL, 35,54));
        addSlot( new IGSlot(this, tile.inventory, tile.SLOT_INPUT, 35,22));

        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 9; j++)
                addSlot(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 84+i*18));
        for(int i = 0; i < 9; i++)
            addSlot(new Slot(inventoryPlayer, i, 8+i*18, 142));


        slotCount = 2;

        data = tile.guiData;
        trackIntArray(data);
    }

    public boolean canInteractWith(@Nonnull PlayerEntity player) {
        return true;
    }

    @Nonnull
    public ItemStack transferStackInSlot(PlayerEntity player, int slot) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slotObject = (Slot)this.inventorySlots.get(slot);
        if (slotObject != null && slotObject.getHasStack()) {
            ItemStack itemstack1 = slotObject.getStack();
            itemstack = itemstack1.copy();
            if (slot < this.slotCount) {
                if (!this.mergeItemStack(itemstack1, this.slotCount, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, this.slotCount, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slotObject.putStack(ItemStack.EMPTY);
            } else {
                slotObject.onSlotChanged();
            }
        }

        return itemstack;
    }

    protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        return super.mergeItemStack(stack, startIndex, endIndex, reverseDirection);
    }

    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        if (tile.inventory != null) {
            tile.inventory.closeInventory(playerIn);
        }
    }
}