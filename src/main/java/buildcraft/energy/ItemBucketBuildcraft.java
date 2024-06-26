/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.energy;

import buildcraft.core.CreativeTabBuildCraft;
import buildcraft.core.utils.StringUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;

public class ItemBucketBuildcraft extends ItemBucket {

	private String iconName;

	public ItemBucketBuildcraft(int i, int blockId) {
		super(i, blockId);
		setCreativeTab(CreativeTabs.tabRedstone);
		setContainerItem(Item.bucketEmpty);
	}

	@Override
	public String getItemDisplayName(ItemStack itemstack) {
		return StringUtils.localize(getUnlocalizedName(itemstack));
	}

	@Override
	public Item setUnlocalizedName(String par1Str) {
		iconName = par1Str;
		return super.setUnlocalizedName(par1Str);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		this.itemIcon = par1IconRegister.registerIcon("buildcraft:" + iconName);
	}
}
