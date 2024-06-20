/*
 * Copyright (c) SpaceToad, 2011-2012
 * http://www.mod-buildcraft.com
 * 
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.api.transport;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Locale;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public enum PipeWire {

	RED, BLUE, GREEN, YELLOW;
	public static Item item;
	public static final PipeWire[] VALUES = values();

	public PipeWire reverse() {
		switch (this) {
			case RED:
				return YELLOW;
			case BLUE:
				return GREEN;
			case GREEN:
				return BLUE;
			default:
				return RED;
		}
	}

	public String getTag() {
		return name().toLowerCase(Locale.ENGLISH) + "PipeWire";
	}

	public ItemStack getStack() {
		return getStack(1);
	}

	public ItemStack getStack(int qty) {
		if (item == null)
			return null;
		return new ItemStack(item, qty, ordinal());
	}

	public boolean isPipeWire(ItemStack stack) {
		if (stack == null)
			return false;
		if (stack.getItem() != item)
			return false;
		return stack.getItemDamage() == ordinal();
	}

	public static PipeWire fromOrdinal(int ordinal) {
		if (ordinal < 0 || ordinal >= VALUES.length)
			return RED;
		return VALUES[ordinal];
	}
}
