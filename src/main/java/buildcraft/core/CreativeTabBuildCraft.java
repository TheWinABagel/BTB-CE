package buildcraft.core;

import buildcraft.BuildCraftCore;
import java.util.Locale;

import buildcraft.transport.ItemFacade;
import net.minecraft.src.*;

public enum CreativeTabBuildCraft {

	MACHINES,
	FACADES;
	private final CreativeTabs tab;

	private CreativeTabBuildCraft() {
//		tab = new Tab();
		tab = null;
	}

	public CreativeTabs get() {
		return tab;
	}

	private String getLabel() {
		return "buildcraft." + name().toLowerCase(Locale.ENGLISH);
	}

	private String translate() {
		return I18n.getString("tab." + name().toLowerCase(Locale.ENGLISH));
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
