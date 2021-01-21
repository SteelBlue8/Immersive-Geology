package com.igteam.immersivegeology.common.data;

import com.igteam.immersivegeology.ImmersiveGeology;
import com.igteam.immersivegeology.api.materials.MaterialUseType;
import com.igteam.immersivegeology.common.IGContent;
import com.igteam.immersivegeology.common.fluid.IGFluid;
import com.igteam.immersivegeology.common.materials.EnumMaterials;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * @author Pabilo8
 * @since 14.07.2020
 */

@EventBusSubscriber(
		modid = ImmersiveGeology.MODID,
		bus = Bus.MOD
)
public class IGDataGenerator
{
		public IGDataGenerator() {
		}

		/*
		Work in progress, currently supports only itemtags
		Naming convention (different from IE): IG+Thingies+Provider
		 */
		@SubscribeEvent
		public static void gatherData(GatherDataEvent event) {
			DataGenerator gen = event.getGenerator();
			if (event.includeServer()) {
				gen.addProvider(new IGItemTagsProvider(gen));
				gen.addProvider(new IGRecipeProvider(gen));
				gen.addProvider(new IGBlockTagProvider(gen));
			}
			if(event.includeClient())
			{
				gen.addProvider(new IGBlockStateProvider(gen, event.getExistingFileHelper()));
				gen.addProvider(new IGItemModelProvider(gen, event.getExistingFileHelper()));
			}
		}

		public static ResourceLocation rl(String path) {
			return new ResourceLocation(ImmersiveGeology.MODID, path);
		}
}