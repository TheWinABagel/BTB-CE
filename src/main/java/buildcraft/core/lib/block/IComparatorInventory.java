package buildcraft.core.lib.block;

import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;

/**
 * Implemented by Blocks which have an inventory Comparator override.
 */
public interface IComparatorInventory {

    boolean doesSlotCountComparator(TileEntity tile, int slot, ItemStack stack);
}
