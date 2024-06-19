/**
 * Copyright (c) 2011-2017, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 * <p/>
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL. Please check the contents
 * of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.lib.inventory.filters;

import buildcraft.core.lib.inventory.StackHelper;
import net.minecraft.src.ItemStack;

/**
 * Returns true if the stack matches any one one of the filter stacks. Checks the OreDictionary and wildcards.
 */
public class CraftingFilter implements IStackFilter {

    private final ItemStack[] stacks;

    public CraftingFilter(ItemStack... stacks) {
        this.stacks = stacks;
    }

    @Override
    public boolean matches(ItemStack stack) {
        if (stacks.length == 0 || !hasFilter()) {
            return true;
        }
        for (ItemStack s : stacks) {
            if (StackHelper.isCraftingEquivalent(s, stack, true)) {
                return true;
            }
        }
        return false;
    }

    public ItemStack[] getStacks() {
        return stacks;
    }

    public boolean hasFilter() {
        for (ItemStack filter : stacks) {
            if (filter != null) {
                return true;
            }
        }
        return false;
    }
}
