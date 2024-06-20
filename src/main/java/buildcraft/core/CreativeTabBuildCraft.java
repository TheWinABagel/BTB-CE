package buildcraft.core;

import buildcraft.BuildCraftCore;
import buildcraft.core.utils.Localization;
import buildcraft.transport.ItemFacade;
import java.util.Locale;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public enum CreativeTabBuildCraft {

	MACHINES,
	FACADES;
	private final CreativeTabs tab;

	private CreativeTabBuildCraft() {
		tab = new Tab();
	}

	public CreativeTabs get() {
		return tab;
	}

	private String getLabel() {
		return "buildcraft." + name().toLowerCase(Locale.ENGLISH);
	}

	private String translate() {
		return Localization.get("tab." + name().toLowerCase(Locale.ENGLISH));
	}

	private ItemStack getItem() {
		switch (this) {
		case FACADES:
			return ItemFacade.getStack(Block.stoneBrick, 0);
		default:
			return new ItemStack(BuildCraftCore.diamondGearItem);
		}

	}

	private class Tab extends CreativeTabs {
		//todocore creative tab?
		private Tab() {
			super(20, getLabel());
		}

		@Override
		public Item getTabIconItem() {
			return getItem().getItem();
		}

		@Override
		public String getTranslatedTabLabel() {
			return translate();
		}
	}
}
