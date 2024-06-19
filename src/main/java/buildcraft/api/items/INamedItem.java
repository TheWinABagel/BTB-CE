package buildcraft.api.items;

import net.minecraft.src.ItemStack;

public interface INamedItem {

    String getName(ItemStack stack);

    boolean setName(ItemStack stack, String name);
}
