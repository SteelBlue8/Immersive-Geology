package com.igteam.immersive_geology.api.multiblock;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.common.blocks.generic.PoweredMultiblockTileEntity;
import blusunrize.immersiveengineering.common.util.Utils;
import com.igteam.immersive_geology.ImmersiveGeology;
import com.igteam.immersive_geology.api.crafting.IGMultiblockRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IGProcessingQueue<T extends IGMultiblockRecipe> {
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
            if(recipeQueue.size() > i) {
                recipeQueue.remove(i);
            }
        }
        dirtyList.clear();
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

    public void readFromNBT(CompoundNBT nbt, boolean descPacket) {
        if(!descPacket) {
            ListNBT processNBT = nbt.getList("recipeQueue", 10);
            this.recipeQueue.clear();

            for (int i = 0; i < processNBT.size(); ++i) {
                CompoundNBT tag = processNBT.getCompound(i);
                if (tag.contains("recipe")) {
                    int processTick = tag.getInt("process_processTick");
                    Dual<MultiblockRecipe, Integer> process = this.loadProcessFromNBT(tag, processTick);
                    if (process != null) {
                        this.recipeQueue.add(process);
                    }
                }
            }
        }
    }

    public void writeToNBT(CompoundNBT nbt, boolean descPacket) {
        if (!descPacket) {
            ListNBT processNBT = new ListNBT();
            Iterator<Dual<MultiblockRecipe, Integer>> var4 = this.recipeQueue.iterator();

            while (var4.hasNext()) {
                Dual<MultiblockRecipe, Integer> process = var4.next();
                processNBT.add(this.writeProcessToNBT(process));
            }

            nbt.put("recipeQueue", processNBT);
        }
    }

    protected CompoundNBT writeProcessToNBT(Dual<MultiblockRecipe, Integer> process) {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("recipe", process.getFirst().getId().toString());
        tag.putInt("process_processTick", process.getSecond());
        return tag;
    }

    protected Dual<MultiblockRecipe, Integer> loadProcessFromNBT(CompoundNBT tag, Integer tick) {
        String id = tag.getString("recipe");
        MultiblockRecipe recipe = this.machineInfo.getRecipeForId(new ResourceLocation(id));
        if (recipe != null) {
            return new Dual<MultiblockRecipe, Integer>(recipe, tick);
        } else {
            return null;
        }
    }
}



