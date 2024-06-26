/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core;

import buildcraft.core.utils.StringUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;

public class ItemBuildCraft extends Item {

	private boolean passSneakClick = false;

	public ItemBuildCraft(int i) {
		super(i);
		setCreativeTab(CreativeTabs.tabRedstone);
	}

	@Override
	public String getItemDisplayName(ItemStack itemstack) {
//		return StatCollector.translateToLocal(this.getUnlocalizedName(itemstack));
		return StringUtils.localize(getUnlocalizedName(itemstack));
	}

	@Override
	public Item setUnlocalizedName(String par1Str) {
		iconString = par1Str;
		return super.setUnlocalizedName(par1Str);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		this.itemIcon = par1IconRegister.registerIcon("buildcraft:" + iconString);
	}

	public Item setPassSneakClick(boolean passClick) {
		this.passSneakClick = passClick;
		return this;
	}

	//todocore implement shouldPassSneakingClickToBlock and onItemUseFirst

	@Override
	public String getItemStackDisplayName(ItemStack itemstack) {
		return StatCollector.translateToLocal(this.getUnlocalizedName(itemstack));
	}


	//	@Override
	public boolean shouldPassSneakingClickToBlock(World par2World, int par4, int par5, int par6) {
		return passSneakClick;
	}

	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		return false;
	}
}
