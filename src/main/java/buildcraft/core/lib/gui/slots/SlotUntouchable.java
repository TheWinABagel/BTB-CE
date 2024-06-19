/**
 * Copyright (c) 2011-2017, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 * <p/>
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL. Please check the contents
 * of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.lib.gui.slots;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;

public class SlotUntouchable extends SlotBase implements IPhantomSlot {

    public SlotUntouchable(IInventory contents, int id, int x, int y) {
        super(contents, id, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack itemstack) {
        return false;
    }

    @Override
    public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
        return false;
    }

    @Override
    public boolean canAdjust() {
        return false;
    }

    @Override
    public boolean canShift() {
        return false;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean func_111238_b() {
        return false;
    }
}
