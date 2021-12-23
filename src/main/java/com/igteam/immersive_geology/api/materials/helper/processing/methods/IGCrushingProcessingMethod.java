package com.igteam.immersive_geology.api.materials.helper.processing.methods;

import com.igteam.immersive_geology.api.materials.helper.processing.IGProcessingMethod;
import com.igteam.immersive_geology.api.materials.helper.processing.ProcessingMethod;
import net.minecraft.item.ItemStack;

public class IGCrushingProcessingMethod extends IGProcessingMethod {

    private final int energyCost, processingTime;
    private ItemStack itemInput, itemOutput;

    public IGCrushingProcessingMethod(int energy, int time){
        this.energyCost = energy;
        this.processingTime = time;
    }

    public IGCrushingProcessingMethod inputItem(ItemStack input){
        this.itemInput = input;
        return this;
    }

    public IGCrushingProcessingMethod outputItem(ItemStack output){
        this.itemOutput = output;
        return this;
    }

    public ItemStack getItemInput() {
        return itemInput;
    }

    public ItemStack getItemOutput() {
        return itemOutput;
    }

    @Override
    public ProcessingMethod getKey() {
        return ProcessingMethod.CRUSHING;
    }

    @Override
    public int getEnergyCost() {
        return 0;
    }

    @Override
    public int getProcessingTime() {
        return 0;
    }
}
