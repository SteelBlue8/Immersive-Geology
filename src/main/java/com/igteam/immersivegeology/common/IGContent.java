package com.igteam.immersivegeology.common;

import com.igteam.immersivegeology.api.materials.Material;
import com.igteam.immersivegeology.api.materials.MaterialUseType;
import com.igteam.immersivegeology.client.menu.helper.ItemSubGroup;
import com.igteam.immersivegeology.common.blocks.*;
import com.igteam.immersivegeology.common.items.IGBaseItem;
import com.igteam.immersivegeology.common.items.tools.IGToolHammer;
import com.igteam.immersivegeology.common.items.tools.IGToolPickaxe;
import com.igteam.immersivegeology.common.materials.EnumMaterials;
import com.igteam.immersivegeology.common.tileentity.IGRegisterTileEntityTypes;
import com.igteam.immersivegeology.common.tileentity.helper.TileEntityEnum;
import com.igteam.immersivegeology.common.util.IGLogger;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.gen.feature.Feature;
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
	public static Map<String, Item> registeredIGItems = new HashMap<String, Item>();
	public static List<Class<? extends TileEntity>> registeredIGTiles = new ArrayList<>();
	public static List<Fluid> registeredIGFluids = new ArrayList<>();
	public static Map<Block, SlabBlock> toSlab = new IdentityHashMap<>();

	public static IGBaseItem itemPickaxe = new IGToolPickaxe().setSubGroup(ItemSubGroup.tools);
	public static IGBaseItem itemGuidebook = new IGBaseItem("ig_guidebook").setSubGroup(ItemSubGroup.misc);
	
	public static void modConstruction()
	{
		// Item, blocks here
		// Cycle through item Types
		for(MaterialUseType materialItem : MaterialUseType.values())
		{
			// cycle through materials
			for(EnumMaterials m : EnumMaterials.values())
			{
				Material material = m.material;
				// Check if that material is allowed to make this item type.
				if(material.hasUsetype(materialItem))
				{
					switch(materialItem.getCategory())
					{
						case RESOURCE_ITEM:
							Arrays.stream(materialItem.getItems(material)).forEach(item -> registeredIGItems.put(item.itemName, item));
							break;
						case RESOURCE_BLOCK:
							Arrays.stream(materialItem.getBlocks(material)).forEach(block -> {
								registeredIGBlocks.put(block.name, block);
							});
							break;
						default:
							break;
					}
				}
			}
		}

		for(TileEntityEnum tee : TileEntityEnum.values()){
			try {
				registeredIGBlocks.put(tee.getBlock().getRegistryName().toString(), tee.getBlock());
			} catch (Exception e){
				IGLogger.logger.error("Failed to Register TileBlock due to " + e.getMessage());
			}
		}

		addItem(itemGuidebook);
		addItem(itemPickaxe);
		addItem(new IGToolHammer().setSubGroup(ItemSubGroup.tools));
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
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		// checkNonNullNames(registeredIGBlocks);
		for(Block block : IGContent.registeredIGBlocks.values())
			if(block!=null)
				event.getRegistry().register(block);
	}

	@SubscribeEvent
	public static void registerBlockItems(RegistryEvent.Register<Item> event)
	{
		for(Block b : registeredIGBlocks.values())
			if(b instanceof IIGBlock)
			{
				event.getRegistry().register(((IIGBlock)b).getItemBlock());
			}
	}

	@SubscribeEvent
	public static void registerFeatures(RegistryEvent.Register<Feature<?>> event)
	{

	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		// checkNonNullNames(registeredIGItems);
		for(Item item : registeredIGItems.values())
			if(item!=null)
				event.getRegistry().register(item);
	}

	@SubscribeEvent
	public static void registerFluids(RegistryEvent.Register<Fluid> event)
	{
		checkNonNullNames(registeredIGFluids);
		for(Fluid fluid : registeredIGFluids)
			event.getRegistry().register(fluid);
	}

	// Changed due to blocks being registered later on
	@Deprecated
	public static <T extends IGMaterialBlock & IIGBlock> BlockIGSlab addMaterialSlabFor(T b)
	{
		BlockIGSlab<T> ret = new BlockIGSlab<T>("slab_"+b.name, Block.Properties.from(b), b.itemBlock.getClass(), b, b.itemSubGroup);
		toSlab.put(b, ret);
		return ret;
	}

	@SubscribeEvent
	public static void registerTEs(RegistryEvent.Register<TileEntityType<?>> event)
	{
		for(TileEntityEnum tee : TileEntityEnum.values()){
			try {
				registerTile(tee.getTile(), event, tee.getBlock());
			} catch (Exception e){
				IGLogger.logger.error("Invalid TileEntity caused by " + e.getMessage());
			}
		}
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
		Set<Block> validSet = new HashSet<>(Arrays.asList(valid));
		TileEntityType<T> type = new TileEntityType<>(() -> {
			try
			{
				return tile.newInstance();
			} catch(InstantiationException|IllegalAccessException e)
			{
				e.printStackTrace();
			}
			return null;
		}, validSet, null);
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
	/**
	 * Use only when needed, default only for IGBaseBlock, please extend that
	 *
	 * @param itemBlock the item to be registered
	 */
	public static void addItemBlockForBlock(String name, BlockItem itemBlock)
	{
		registeredIGItems.put(name, itemBlock);
	}

	public static void addItem(IGBaseItem item)
	{
		registeredIGItems.put(item.itemName, item);
	}

	public static void addBlock(IGBaseBlock block)
	{
		registeredIGBlocks.put(block.name, block);
	}
}
