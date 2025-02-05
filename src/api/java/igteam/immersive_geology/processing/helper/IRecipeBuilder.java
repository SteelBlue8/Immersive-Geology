package igteam.immersive_geology.processing.helper;

import igteam.immersive_geology.materials.data.MaterialBase;
import igteam.immersive_geology.processing.IGProcessingStage;
import igteam.immersive_geology.processing.methods.IGCraftingMethod;
import igteam.immersive_geology.processing.methods.IGSeparatorMethod;

public interface IRecipeBuilder {
    static IGCraftingMethod crafting(IGProcessingStage parentStage) { return new IGCraftingMethod(parentStage);}
    static IGSeparatorMethod separating(IGProcessingStage parentStage) { return new IGSeparatorMethod(parentStage);}

}
