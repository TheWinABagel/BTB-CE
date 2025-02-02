package buildcraft.core.gui.slots;

import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;

public class SlotWorkbench extends SlotBase {

	public SlotWorkbench(IInventory iinventory, int slotIndex, int posX, int posY) {
		super(iinventory, slotIndex, posX, posY);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack != null && stack.isStackable() && !stack.getItem().hasContainerItem();
	}

	@Override
	public boolean canShift() {
		return false;
	}
}
