package com.igteam.immersive_geology.common.block.helpers;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import com.igteam.immersive_geology.api.crafting.IGMultiblockRecipe;
import com.igteam.immersive_geology.api.multiblock.IGMachineInfo;

public interface IIGMultiblockProcess {
    boolean canProcessOutput();
    void doOutputFor(MultiblockRecipe recipe, IGMachineInfo info);
    Class<? extends IGMultiblockRecipe> getRecipeClass();
}
