package buildcraft.core.inventory.filters;

import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntityFurnace;

/**
 * This interface is used with several of the functions in IItemTransfer to
 * provide a convenient means of dealing with entire classes of items without
 * having to specify each item individually.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public enum StackFilter implements IStackFilter {

	ALL {
		@Override
		public boolean matches(ItemStack stack) {
			return true;
		}
	},
	FUEL {
		@Override
		public boolean matches(ItemStack stack) {
			return new TileEntityFurnace().getItemBurnTime(stack) > 0;
		}
	};

	@Override
	public abstract boolean matches(ItemStack stack);
}
