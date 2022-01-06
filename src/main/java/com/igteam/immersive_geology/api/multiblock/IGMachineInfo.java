package com.igteam.immersive_geology.api.multiblock;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import com.igteam.immersive_geology.ImmersiveGeology;
import com.igteam.immersive_geology.api.crafting.IGMultiblockRecipe;
import com.igteam.immersive_geology.common.block.helpers.IIGMultiblockProcess;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;

import javax.management.StringValueExp;
import java.util.LinkedHashSet;

public class IGMachineInfo {
    protected IIGMultiblockProcess multiblock;

    protected Class<? extends IGMultiblockRecipe> recipeClass;

    protected LinkedHashSet<Integer> outputSlotInfo = new LinkedHashSet<>();

    public IGMachineInfo(IIGMultiblockProcess multiblock){
        this.multiblock = multiblock;
        this.recipeClass = multiblock.getRecipeClass();
    }

    public boolean canProcessOutput(){
        return multiblock.canProcessOutput();
    }

    public void doOutputFor(MultiblockRecipe recipe){
        multiblock.doOutputFor(recipe, this);
    }

    public IGMultiblockRecipe getRecipeForId(ResourceLocation id) {
        try {
            return recipeClass.newInstance().getRecipes().get(id);
        } catch (ReflectiveOperationException e) {
            ImmersiveGeology.getNewLogger().log(Level.ERROR, "Error in get Recipe for ID: " + e.getMessage());
            return null;
        }
    }
}
