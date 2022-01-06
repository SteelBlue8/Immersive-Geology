package com.igteam.immersive_geology.api.multiblock;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import com.igteam.immersive_geology.ImmersiveGeology;
import jdk.nashorn.internal.ir.annotations.Immutable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IGProcessingQueue {
    protected IGMachineInfo machineInfo;
    protected List<Dual<MultiblockRecipe, Integer>> recipeQueue = new ArrayList<>();
    protected boolean isFinished = false;

    public void setMachineInfo(IGMachineInfo machineInfo) {
        this.machineInfo = machineInfo;
    }

    public void doProcessingStep(){
        if(machineInfo.canProcessOutput()) {
            recipeQueue.iterator().forEachRemaining((element) -> {
                int currentProcess = element.getSecond();
                currentProcess++;
                if (currentProcess <= element.getFirst().getTotalProcessTime()) {
                    element.setSecond(currentProcess);
                }
            });

            onProcessFinish();
        }
    }

    public void onProcessFinish(){
        List<Integer> dirtyList = new ArrayList<>();
        int size = recipeQueue.size();
        for(int i = 0; i < size; i++){
            Dual<MultiblockRecipe, Integer> element = recipeQueue.get(i);
            int maxTicks = element.getFirst().getTotalProcessTime();
            int currentProcess = element.getSecond();
            if(currentProcess >= maxTicks) {
                machineInfo.doOutputFor(element.getFirst());
                dirtyList.add(i);
            }
        }

        for (int i : dirtyList) {
            recipeQueue.remove(i);
        }
    }

    MultiblockRecipe getRecipe(int queuePos) {
        try {
            return recipeQueue.get(queuePos).getFirst();
        } catch (Exception e) {
            ImmersiveGeology.getNewLogger().log(Level.ERROR, "Error in Processing Queue [" + queuePos + "]: " + e.getLocalizedMessage());
            return null;
        }
    }

    public IGMachineInfo getMachineInfo() {
        return machineInfo;
    }

    public boolean addProcessToQueue(MultiblockRecipe recipe){
        //TODO allow parameter distance for queue
        if(recipeQueue.stream().anyMatch((element) -> (element.getSecond() > 5)) || recipeQueue.isEmpty()) {
            recipeQueue.add(new Dual<MultiblockRecipe, Integer>(recipe, 0));
            return true;
        } else {
            return false;
        }
    }

    public boolean hasElements(){
        return !recipeQueue.isEmpty();
    }

    public List<Dual<MultiblockRecipe, Integer>> getElements(){
        return recipeQueue;
    }

    public void readFromNBT(CompoundNBT nbt) {
        //TODO Implement
    }

    public void writeToNBT(CompoundNBT nbt) {
        //TODO Implement
    }
}
