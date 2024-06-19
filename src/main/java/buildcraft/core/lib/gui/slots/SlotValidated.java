/**
 * Copyright (c) 2011-2017, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 * <p/>
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL. Please check the contents
 * of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.lib.gui.slots;

import net.minecraft.src.IInventory;
import net.minecraft.src.Slot;
import net.minecraft.src.ItemStack;

public class SlotValidated extends Slot {

    public SlotValidated(IInventory inv, int id, int x, int y) {
        super(inv, id, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return inventory.isItemValidForSlot(this.getSlotIndex(), itemStack);
    }
}
