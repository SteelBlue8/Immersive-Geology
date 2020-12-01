package com.igteam.immersivegeology.common.blocks;


import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.gui.GuiHandler;
import com.igteam.immersivegeology.ImmersiveGeology;
import com.igteam.immersivegeology.api.materials.Material;
import com.igteam.immersivegeology.api.materials.MaterialUseType;
import com.igteam.immersivegeology.client.menu.helper.ItemSubGroup;
import com.igteam.immersivegeology.common.gui.GuiAccessor;
import com.igteam.immersivegeology.common.gui.container.CrudeForgeContainer;
import com.igteam.immersivegeology.common.tileentity.entities.CrudeForgeTileEntity;
import com.igteam.immersivegeology.common.util.IGLogger;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IGTileBlock extends IGBaseBlock {

    protected Class<? extends TileEntity> linked_entity;

    public IGTileBlock(String name, MaterialUseType subtype, Material... materials) {
        super("tile_"+name, Properties.create(subtype.getMaterial()), null, ItemSubGroup.machines);
        this.itemBlock = new IGBlockItem(this, new Item.Properties().group(ImmersiveGeology.IG_ITEM_GROUP), ItemSubGroup.machines);
        this.itemBlock.setRegistryName(this.name);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        try {
            return linked_entity.newInstance();
        } catch (Exception e){
            IGLogger.error("Failed to link TileEntity("+linked_entity.getName()+") to " + this.name + " due to " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    public IGTileBlock setLinkedEntity(Class<? extends TileEntity> entity){
        this.linked_entity = entity;
        return this;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        TileEntity tile = world.getTileEntity(pos);
        if(player instanceof ServerPlayerEntity) {
            ScreenManager.openScreen(GuiAccessor.CONTAINER_INFO.get(0), Minecraft.getInstance(),0,new StringTextComponent("crude_forge"));
            return true;
        } else {
            return false;
        }
    }
}
