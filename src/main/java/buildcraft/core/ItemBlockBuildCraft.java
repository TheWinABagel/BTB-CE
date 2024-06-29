package buildcraft.core;

import buildcraft.core.utils.StringUtils;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class ItemBlockBuildCraft extends ItemBlock {

	public ItemBlockBuildCraft(int id) {
		super(id);
		this.setFull3D();
	}

	@Override
	public int getMetadata(int i) {
		return i;
	}

	@Override
	public String getItemDisplayName(ItemStack itemstack) {
		return StringUtils.localize(getUnlocalizedName(itemstack));
	}

	@Override
	public String getItemStackDisplayName(ItemStack itemstack) {
		return StringUtils.localize(getUnlocalizedName(itemstack));
	}

}
