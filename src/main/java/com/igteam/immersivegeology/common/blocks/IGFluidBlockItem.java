package com.igteam.immersivegeology.common.blocks;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.client.ClientProxy;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import com.igteam.immersivegeology.ImmersiveGeology;
import com.igteam.immersivegeology.api.materials.Material;
import com.igteam.immersivegeology.client.menu.helper.IIGSubGroupContained;
import com.igteam.immersivegeology.client.menu.helper.ItemSubGroup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IGFluidBlockItem extends BlockItem implements IEItemInterfaces.IColouredItem
{

	protected ItemSubGroup subGroup;
	private int burnTime;

	private final Material material;

	public IGFluidBlockItem(Block b, Properties props, Material material)
	{
		super(b, props);
		this.material = material;
	}

    @Override
	public String getTranslationKey(ItemStack stack)
	{
		return getBlock().getTranslationKey();
	}

	@Override
	public boolean hasCustomItemColours()
	{
		return true;
	}

	@Override
	public int getColourForIEItem(ItemStack stack, int pass) {
		return this.material.getColor(0);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public FontRenderer getFontRenderer(ItemStack stack)
	{
		return ClientProxy.itemFont;
	}

	@Override
	public ITextComponent getDisplayName(ItemStack p_200295_1_) {
		ArrayList<String> localizedNames = new ArrayList<>();
		localizedNames.add(I18n.format("material."+this.material.getModID()+"."+this.material.getName()+".name"));
		TranslationTextComponent name = new TranslationTextComponent("item."+ ImmersiveGeology.MODID+".fluid.name", localizedNames.toArray(new Object[localizedNames.size()]));
		return name;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag advanced)
	{
		if(getBlock() instanceof IIGBlock)
		{
			IIGBlock igBlock = (IIGBlock)getBlock();
			if(igBlock.hasFlavour())
			{
				String flavourKey = Lib.DESC_FLAVOUR+igBlock.getNameForFlavour();
				tooltip.add(new TranslationTextComponent(flavourKey).setStyle(new Style().setColor(TextFormatting.GRAY)));
			}
		}
		super.addInformation(stack, world, tooltip, advanced);
		if(ItemNBTHelper.hasKey(stack, "energyStorage"))
			tooltip.add(new TranslationTextComponent(Lib.DESC_INFO+"energyStored",
					ItemNBTHelper.getInt(stack, "energyStorage")));
		if(ItemNBTHelper.hasKey(stack, "tank"))
		{
			FluidStack fs = FluidStack.loadFluidStackFromNBT(ItemNBTHelper.getTagCompound(stack, "tank"));
			if(fs!=null)
				tooltip.add(new TranslationTextComponent(Lib.DESC_INFO+"fluidStored",
						fs.getDisplayName(), fs.getAmount()));
		}
	}
}
