package com.igteam.immersivegeology.common.fluid;

import com.igteam.immersivegeology.ImmersiveGeology;
import com.igteam.immersivegeology.common.blocks.property.IGVanillaMaterials;
import com.sun.org.apache.bcel.internal.generic.FADD;
import net.minecraft.client.resources.I18n;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class IGFluidAttributes extends FluidAttributes {
    private final IGFluid igFluid;

    protected IGFluidAttributes(Builder builder, IGFluid fluid) {
        super(builder, fluid);
        this.igFluid = fluid;
    }

    public static IGFluidAttributes.IGBuilder builder(ResourceLocation stillTexture, ResourceLocation flowingTexture) {
        return new IGFluidAttributes.IGBuilder(stillTexture, flowingTexture, IGFluidAttributes::new);
    }

    @Override
    public ITextComponent getDisplayName(FluidStack stack) {
        ArrayList<String> localizedNames = new ArrayList<>();
        localizedNames.add(I18n.format("material."+this.igFluid.matType.getModID()+"."+this.igFluid.matType.getName()+".name"));
        TranslationTextComponent name = new TranslationTextComponent(this.getTranslationKey(), localizedNames.toArray(new Object[localizedNames.size()]));
        return name;
    }

    public static class IGBuilder extends Builder {
        private final ResourceLocation stillTexture;
        private final ResourceLocation flowingTexture;
        private ResourceLocation overlayTexture;
        private int color = -1;
        private String translationKey;
        private SoundEvent fillSound;
        private SoundEvent emptySound;
        private int luminosity = 0;
        private int density = 1000;
        private int temperature = 300;
        private int viscosity = 1000;
        private boolean isGaseous;
        private Rarity rarity;
        private BiFunction<IGFluidAttributes.IGBuilder, IGFluid, IGFluidAttributes> factory;

        protected IGBuilder(ResourceLocation stillTexture, ResourceLocation flowingTexture, BiFunction<IGFluidAttributes.IGBuilder, IGFluid, IGFluidAttributes> factory) {
            super(stillTexture, flowingTexture, (BiFunction) factory);
            this.rarity = Rarity.COMMON;
            this.factory = factory;
            this.stillTexture = stillTexture;
            this.flowingTexture = flowingTexture;
        }

        public IGFluidAttributes build(IGFluid fluid) {
            return (IGFluidAttributes)this.factory.apply(this, fluid);
        }
    }
}
