package com.igteam.immersivegeology.common.util;

import static com.igteam.immersivegeology.ImmersiveGeology.GENERATE_MODELS;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.stream.JsonWriter;
import com.igteam.immersivegeology.api.materials.Material;
import com.igteam.immersivegeology.api.materials.MaterialUseType;
import com.igteam.immersivegeology.api.materials.material_bases.MaterialStoneBase.EnumStoneType;

public class BlockstateGenerator
{
	//NOTE: this generate runs at start up, minecraft looks for item and block models BEFORE this runs, 
	//which means you need to start minecraft up to generate the json, then close and start minecraft again to see it in game.
	public static void generateDefaultBlock(Material material, MaterialUseType type)
	{
		generateDefaultBlock("block_"+type.getName()+"_"+material.getName(), type.getName());
	}
	
	public static void generateDefaultBlock(Material material, MaterialUseType type, EnumStoneType rockType)
	{
		generateDefaultBlock("block_"+type.getName()+"_"+material.getName(), type.getName() + "_" + rockType.getName());
	}

	public static void generateDefaultSlabBlock(Material material,  MaterialUseType type) {
		generateDefaultSlabBlock("slab_block_"+type.getName()+"_"+material.getName(), type.getName());
	}
	

	
	private static void generateDefaultSlabBlock(String registryName, String baseType) {
		if(!GENERATE_MODELS)
			return;
		try
		{ 
			File file = new File("../src/main/resources/assets/immersivegeology/blockstates/"+registryName+".json");
			if(!file.exists())
			{
				JsonWriter jsonWriter = new JsonWriter(new FileWriter(file));
				jsonWriter.setIndent(" "); //this makes it more readable for humans!
				jsonWriter.beginObject();
					jsonWriter.name("variants");
					jsonWriter.beginObject();
						jsonWriter.name("type=top");
						jsonWriter.beginObject();
							jsonWriter.name("model");
							jsonWriter.value("immersivegeology:block/base/slab/slab_"+ baseType + "_top");
						jsonWriter.endObject();
						
						jsonWriter.name("type=bottom");
						jsonWriter.beginObject();
							jsonWriter.name("model");
							jsonWriter.value("immersivegeology:block/base/slab/slab_"+ baseType + "_bottom");
						jsonWriter.endObject();
						
						jsonWriter.name("type=double");
						jsonWriter.beginObject();
							jsonWriter.name("model");
							jsonWriter.value("immersivegeology:block/base/slab/slab_"+ baseType + "_double");
						jsonWriter.endObject();
						
					jsonWriter.endObject();
				jsonWriter.endObject();
				jsonWriter.close();
			}
		} catch(IOException e)
		{

		}
	}

	

	//NOTE: this generate runs at start up, minecraft looks for item and block models BEFORE this runs, 
	//which means you need to start minecraft up to generate the json, then close and start minecraft again to see it in game.
	public static void generateDefaultBlock(String registryName, String itemBaseType)
	{
		if(!GENERATE_MODELS)
			return;
		try
		{ 
			File file = new File("../src/main/resources/assets/immersivegeology/blockstates/"+registryName+".json");
			if(!file.exists())
			{
				JsonWriter jsonWriter = new JsonWriter(new FileWriter(file));
				jsonWriter.setIndent(" "); //this makes it more readable for humans!
				jsonWriter.beginObject();
				jsonWriter.name("variants");
				jsonWriter.beginObject();
				jsonWriter.name("");
				jsonWriter.beginObject();
				jsonWriter.name("model");
				jsonWriter.value("immersivegeology:block/base/"+itemBaseType);
				jsonWriter.endObject();
				jsonWriter.endObject();
				jsonWriter.endObject();
				jsonWriter.close();
			}
		} catch(IOException e)
		{

		}

	}

}
