package com.igteam.immersivegeology.common.data;

import com.igteam.immersivegeology.ImmersiveGeology;
import com.igteam.immersivegeology.api.materials.MaterialUseType;
import com.igteam.immersivegeology.api.util.IGRegistryGrabber;
import com.igteam.immersivegeology.common.IGContent;
import com.igteam.immersivegeology.common.blocks.IGBaseBlock;
import com.igteam.immersivegeology.common.blocks.IGMaterialBlock;
import com.igteam.immersivegeology.common.blocks.plant.IGLogBlock;
import com.igteam.immersivegeology.common.items.IGMaterialResourceItem;
import com.igteam.immersivegeology.common.materials.EnumMaterials;
import com.igteam.immersivegeology.common.util.IGLogger;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.util.function.Consumer;

public class IGRecipeProvider extends RecipeProvider
{

	public IGRecipeProvider(DataGenerator generatorIn)
	{
		super(generatorIn);
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
	{
		createBasicRecipes(consumer);
	}

	public void createBasicRecipes(Consumer<IFinishedRecipe> consumer)
	{
		for(Item item : IGContent.registeredIGItems.values())
		{
			try
			{
				if(item instanceof IGMaterialResourceItem)
				{
					IGMaterialResourceItem resourceItem = (IGMaterialResourceItem)item;
					if(resourceItem.subtype.equals(MaterialUseType.CHUNK))
					{
						IGBaseBlock block = IGRegistryGrabber.grabBlock(MaterialUseType.ROUGH_BRICKS, resourceItem.getMaterial());
						IGLogger.info("Rough Brick Recipes");
						ShapedRecipeBuilder.shapedRecipe(block)
								.patternLine("xx")
								.patternLine("xx")
								.key('x', ItemTags.getCollection().get(new ResourceLocation("forge", "chunks/" + resourceItem.getMaterial().getName())))
								.setGroup("bricks")
								.addCriterion("has_chunk", InventoryChangeTrigger.Instance.forItems(item))
								.build(consumer, new ResourceLocation(ImmersiveGeology.MODID, "craft_" + block.name));
					}
				}
			} catch(Exception e)
			{
				IGLogger.info("Failed to create Recipe: "+e);
			}
		}
		for(IGBaseBlock block : IGContent.registeredIGBlocks.values())
		{
			try
			{
				if(block instanceof IGLogBlock){
					IGLogBlock logBlock = (IGLogBlock)block;
					if(logBlock.subtype.equals(MaterialUseType.LOG)){
						IGLogger.info("Creating Log Recipes");
						Block result = IGRegistryGrabber.grabBlock(MaterialUseType.PLANKS, logBlock.getMaterial());
						Block input = IGRegistryGrabber.grabBlock(MaterialUseType.LOG, logBlock.getMaterial());
						ShapelessRecipeBuilder.shapelessRecipe(result, 4).addIngredient(input).setGroup("planks").addCriterion("has_logs", this.hasItem(input)).build(consumer);
					}
				}
			} catch(Exception e)
			{
				IGLogger.info("Failed to create Recipe: "+e);
			}
		}
	}
}
