package com.igteam.immersivegeology.common.blocks;

import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IColouredItem;
import com.igteam.immersivegeology.ImmersiveGeology;
import com.igteam.immersivegeology.api.materials.Material;
import com.igteam.immersivegeology.api.materials.MaterialUseType;
import com.igteam.immersivegeology.client.menu.helper.ItemSubGroup;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

import static com.igteam.immersivegeology.common.items.IGMaterialResourceItem.hasShiftDown;

public class IGBlockMaterialItem extends IGBlockItem implements IColouredItem
{

	public MaterialUseType subtype=MaterialUseType.STORAGE;
	public Material material=EnumMaterials.Empty.material;

	public IGBlockMaterialItem(Block b, Properties props, ItemSubGroup sub) {
		super(b, props.group(ImmersiveGeology.IG_ITEM_GROUP), sub);
	}
	
	@Override
	public ITextComponent getDisplayName(ItemStack stack)
	{
		String matName = I18n.format("material."+material.getModID()+"."+material.getName()+".name");
		return new TranslationTextComponent("block."+ImmersiveGeology.MODID+"."+ subtype.getName().toLowerCase(Locale.ENGLISH)+".name", matName);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		StringTextComponent text = new StringTextComponent("");
		if (hasShiftDown() || Minecraft.getInstance().gameSettings.advancedItemTooltips)
		{
			material.getElements().forEach(elementProportion -> text
					.appendText("<hexcol="+elementProportion.getElement().getColor()+":"+elementProportion.getElement().getSymbol()+">")
					.appendText(String.valueOf(elementProportion.getQuantity() > 1?elementProportion.getQuantity(): "")));
			tooltip.add(text);
		}
	}

	@Override
	public boolean hasCustomItemColours()
	{
		return true;
	}

	@Override
	public int getColourForIEItem(ItemStack stack, int pass)
	{
		return material.getColor(0);
	}
}
