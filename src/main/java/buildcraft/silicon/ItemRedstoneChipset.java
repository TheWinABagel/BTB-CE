package buildcraft.silicon;

import buildcraft.BuildCraftSilicon;
import buildcraft.core.ItemBuildCraft;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import java.util.List;
import java.util.Locale;
import net.minecraft.src.IconRegister;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Icon;

public class ItemRedstoneChipset extends ItemBuildCraft {

	public static enum Chipset {

		RED,
		IRON,
		GOLD,
		DIAMOND,
		PULSATING,
		QUARTZ,
		COMP;
		public static final Chipset[] VALUES = values();
		private Icon icon;

		public String getChipsetName() {
			return "redstone_" + name().toLowerCase(Locale.ENGLISH) + "_chipset";
		}

		public ItemStack getStack() {
			return getStack(1);
		}

		public ItemStack getStack(int qty) {
			return new ItemStack(BuildCraftSilicon.redstoneChipset, qty, ordinal());
		}

		public static Chipset fromOrdinal(int ordinal) {
			if (ordinal < 0 || ordinal >= VALUES.length)
				return RED;
			return VALUES[ordinal];
		}
	}

	public ItemRedstoneChipset(int i) {
		super(i);
		setHasSubtypes(true);
		setMaxDamage(0);
		setUnlocalizedName("redstoneChipset");
	}

	@Override
	public Icon getIconFromDamage(int damage) {
		return Chipset.fromOrdinal(damage).icon;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "item." + Chipset.fromOrdinal(stack.getItemDamage()).getChipsetName();
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	@Environment(EnvType.CLIENT)
	public void getSubItems(int id, CreativeTabs tab, List itemList) {
		for (Chipset chipset : Chipset.VALUES) {
			itemList.add(chipset.getStack());
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		for (Chipset chipset : Chipset.VALUES) {
			chipset.icon = par1IconRegister.registerIcon("buildcraft:" + chipset.getChipsetName());
		}
	}

	public void registerItemStacks() {
		for (Chipset chipset : Chipset.VALUES) {
//			GameRegistry.registerCustomItemStack(chipset.getChipsetName(), chipset.getStack());
		}
	}
}
