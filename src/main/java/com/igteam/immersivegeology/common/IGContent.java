package com.igteam.immersivegeology.common;

import com.igteam.immersivegeology.api.materials.Material;
import com.igteam.immersivegeology.api.materials.MaterialUseType;
import com.igteam.immersivegeology.common.blocks.BlockIGSlab;
import com.igteam.immersivegeology.common.blocks.EnumMaterials;
import com.igteam.immersivegeology.common.blocks.IGBaseBlock;
import com.igteam.immersivegeology.common.blocks.IIGBlock;
import com.igteam.immersivegeology.common.util.IGBlockGrabber;
import com.igteam.immersivegeology.common.world.WorldTypeImmersive;
import com.igteam.immersivegeology.common.world.biome.WorldLayerData;

import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.Field;
import java.util.*;

import static com.igteam.immersivegeology.ImmersiveGeology.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IGContent
{
	public static Map<String, IGBaseBlock> registeredIGBlocks = new HashMap<String, IGBaseBlock>();
	public static Map<String, Block> registeredIGSlabBlocks = new HashMap<String, Block>();
	public static List<Item> registeredIGItems = new ArrayList<>();
	public static List<Class<? extends TileEntity>> registeredIGTiles = new ArrayList<>();
	public static List<Fluid> registeredIGFluids = new ArrayList<>();
	public static Map<Block, SlabBlock> toSlab = new IdentityHashMap<>();
	
	public static WorldTypeImmersive worldType;
	
	public static void modConstruction()
	{
		
		//setup worldtype
		worldType = new WorldTypeImmersive();
		
		//Item, blocks here
		// cycle through item Types
		for(MaterialUseType materialItem : MaterialUseType.values())
		{
			//cycle through materials
			for(EnumMaterials m : EnumMaterials.values())
			{
				Material material = m.material;
				//check if that material is allowed to make this item type.
				if(material.hasSubtype(materialItem)) {
					//check if this type is an ITEM not a BLOCK type.

					switch(materialItem.getCategory())
					{
						case RESOURCE_ITEM:
							registeredIGItems.add(materialItem.getItem(material));
							break;
						case RESOURCE_BLOCK:
						case BLOCK:
							registeredIGBlocks.put(materialItem.getName() + "_" + material.getName(),materialItem.getBlock(material));
						break;
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		//checkNonNullNames(registeredIGBlocks);
		for(Block block : registeredIGBlocks.values())
			if(block!=null)
				event.getRegistry().register(block);
	}
	
	@SubscribeEvent
	public static void registerBlockItems(RegistryEvent.Register<Item> event)
	{
		//checkNonNullNames(registeredIGItems);
		for(Block b : registeredIGBlocks.values()) {
			if(b!=null) {
				if (b instanceof IIGBlock) {
				event.getRegistry().register(((IIGBlock) b).getItemBlock());
				}
			}
		}
		
		//slabs only!
		for(Block b : registeredIGSlabBlocks.values()) {
			if(b!=null) {
				if (b instanceof IIGBlock) {
				event.getRegistry().register(((IIGBlock) b).getItemBlock());
				}
			}
		}
	}

	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		//checkNonNullNames(registeredIGItems);
		for(Item item : registeredIGItems)
			if(item!=null)
				event.getRegistry().register(item);
		registerOres();
	}

	private static <T extends IForgeRegistryEntry<T>> void checkNonNullNames(Collection<T> coll)
	{
		int numNull = 0;
		for(T b : coll)
			if(b.getRegistryName()==null)
			{
				++numNull;
			}
		if(numNull > 0)
			System.exit(1);
	}

	@SubscribeEvent
	public static void registerFluids(RegistryEvent.Register<Fluid> event)
	{
		checkNonNullNames(registeredIGFluids);
		for(Fluid fluid : registeredIGFluids)
			event.getRegistry().register(fluid);
	}

	//Changed due to blocks being registered later on
	public static <T extends IGBaseBlock & IIGBlock> BlockIGSlab addMaterialSlabFor(T b)
	{
		BlockIGSlab<T> ret = new BlockIGSlab<T>(
				"slab_"+b.name,
				Block.Properties.from(b),
				b.itemBlock.getClass(),
				b,
				b.itemSubGroup
		);
		toSlab.put(b, ret);
		return ret;
	}

	@SubscribeEvent
	public static void registerTEs(RegistryEvent.Register<TileEntityType<?>> event)
	{

	}

	public static void registerRecipes()
	{

	}

	public static void registerOres()
	{

	}

	public static void init()
	{

	}

	public static void postInit()
	{

	}

	public static <T extends TileEntity> void registerTile(Class<T> tile, RegistryEvent.Register<TileEntityType<?>> event, Block... valid)
	{
		String s = tile.getSimpleName();
		s = s.substring(0, s.indexOf("TileEntity")).toLowerCase(Locale.ENGLISH);
		Set<Block> validSet = new LinkedHashSet<>(Arrays.asList(valid));
		TileEntityType<T> type = new TileEntityType<>(() -> {
			try
			{
				return tile.newInstance();
			} catch(InstantiationException|IllegalAccessException e)
			{
				e.printStackTrace();
			}
			return null;
		}, validSet, null); //TODO where do I get a Type<T> from?
		type.setRegistryName(MODID, s);
		event.getRegistry().register(type);
		try
		{
			Field typeField = tile.getField("TYPE");
			typeField.set(null, type);
		} catch(NoSuchFieldException|IllegalAccessException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		registeredIGTiles.add(tile);
	}
}
