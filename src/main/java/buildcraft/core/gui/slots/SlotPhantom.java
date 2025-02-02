/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.gui.slots;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class SlotPhantom extends SlotBase implements IPhantomSlot {

	public SlotPhantom(IInventory iinventory, int slotIndex, int posX, int posY) {
		super(iinventory, slotIndex, posX, posY);
	}

	@Override
	public boolean canAdjust() {
		return true;
	}
	
	@Override
    public boolean canTakeStack(EntityPlayer par1EntityPlayer)
    {
        return false;
    }

	@Override
	public void putStack(ItemStack par1ItemStack) {
		super.putStack(par1ItemStack);
	}
}
