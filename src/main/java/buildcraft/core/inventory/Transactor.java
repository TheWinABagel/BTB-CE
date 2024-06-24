package buildcraft.core.inventory;

import buildcraft.api.inventory.ISpecialInventory;
import net.minecraft.src.IInventory;
import net.minecraft.src.ISidedInventory;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.ForgeDirection;

public abstract class Transactor implements ITransactor {

	@Override
	public ItemStack add(ItemStack stack, ForgeDirection orientation, boolean doAdd) {
		ItemStack added = stack.copy();
		added.stackSize = inject(stack, orientation, doAdd);
		return added;
	}

	public abstract int inject(ItemStack stack, ForgeDirection orientation, boolean doAdd);

	public static ITransactor getTransactorFor(Object object) {

		if (object instanceof ISpecialInventory special)
			return new TransactorSpecial(special);
		else if (object instanceof ISidedInventory sided)
			return new TransactorSimple(sided);
		else if (object instanceof IInventory inv)
			return new TransactorSimple(InvUtils.getInventory(inv));

		return null;
	}
}
