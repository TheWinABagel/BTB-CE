package buildcraft.core.gui.slots;

import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;

public class SlotOutput extends SlotBase {

	public SlotOutput(IInventory iinventory, int slotIndex, int posX, int posY) {
		super(iinventory, slotIndex, posX, posY);
	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		return false;
	}
}
