package com.igteam.immersive_geology.common.crafting.serializers;

import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import com.google.gson.JsonObject;
import com.igteam.immersive_geology.api.crafting.recipes.recipe.BloomeryRecipe;
import com.igteam.immersive_geology.api.crafting.recipes.recipe.ReverberationRecipe;
import com.igteam.immersive_geology.core.registration.IGMultiblockRegistrationHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class ReverberationRecipeSerializer extends IERecipeSerializer<ReverberationRecipe>
{

    @Override
    public ItemStack getIcon() {
        return new ItemStack(IGMultiblockRegistrationHolder.Multiblock.bloomery);
    }

    @Override
    public ReverberationRecipe readFromJson(ResourceLocation recipeId, JsonObject json) {
        ItemStack output = readOutput(json.get("result"));
        IngredientWithSize input = IngredientWithSize.deserialize(JSONUtils.getJsonObject(json, "item_input"));
        Integer time = JSONUtils.getInt(json, "time");
        ReverberationRecipe recipe = new ReverberationRecipe(recipeId, output, input, time);
        return recipe;
    }

    @Override
    public ReverberationRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
        ItemStack output = buffer.readItemStack();
        IngredientWithSize input = IngredientWithSize.read(buffer);
        Integer time = buffer.readInt();
        ReverberationRecipe recipe = new ReverberationRecipe(recipeId, output, input, time);
        return recipe;
    }

    @Override
    public void write(PacketBuffer buffer, ReverberationRecipe recipe) {
        buffer.writeItemStack(recipe.output);
        recipe.input.write(buffer);
        buffer.writeInt(recipe.getTime());
    }
}
