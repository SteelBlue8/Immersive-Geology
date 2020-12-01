package com.igteam.immersivegeology.common.blocks.machines;

import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import com.igteam.immersivegeology.api.materials.Material;
import com.igteam.immersivegeology.api.materials.MaterialUseType;
import com.igteam.immersivegeology.client.gui.CrudeForgeScreen;
import com.igteam.immersivegeology.common.blocks.IGTileBlock;
import com.igteam.immersivegeology.common.gui.GuiAccessor;
import com.igteam.immersivegeology.common.gui.GuiLib;
import com.igteam.immersivegeology.common.gui.container.CrudeForgeContainer;
import com.igteam.immersivegeology.common.materials.EnumMaterials;
import com.igteam.immersivegeology.common.tileentity.entities.CrudeForgeTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class IGCrudeForgeCore extends IGTileBlock {
    public IGCrudeForgeCore() {
        super("crude_forge", MaterialUseType.ROCK, EnumMaterials.Marble.material);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ScreenManager.openScreen(GuiAccessor.CONTAINER_INFO.get(GuiLib.CRUDE_FORGE_GUI_ID),Minecraft.getInstance(),0,new StringTextComponent("crude_forge"));
        return true;
    }
}
