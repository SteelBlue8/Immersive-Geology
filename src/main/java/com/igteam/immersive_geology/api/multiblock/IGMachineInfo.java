package com.igteam.immersive_geology.api.multiblock;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import com.igteam.immersive_geology.common.block.helpers.IGMetalMultiblock;
import com.igteam.immersive_geology.common.block.helpers.IIGMultiblockProcess;

import java.util.LinkedHashSet;

public class IGMachineInfo {
    protected IIGMultiblockProcess multiblock;

    protected LinkedHashSet<Integer> outputSlotInfo = new LinkedHashSet<>();

    public IGMachineInfo(IIGMultiblockProcess multiblock){
        this.multiblock = multiblock;
    }


    public boolean canProcessOutput(){
        return multiblock.canProcessOutput();
    }

    public void doOutputFor(MultiblockRecipe recipe){
        multiblock.doOutputFor(recipe, this);
    }
}
