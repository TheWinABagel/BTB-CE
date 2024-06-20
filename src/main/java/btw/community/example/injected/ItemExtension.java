package btw.community.example.injected;

import btw.community.example.mixin.accessors.ItemStackAccessor;
import net.minecraft.src.ItemStack;

public interface ItemExtension {

    default int getDamage(ItemStack stack) {
        return 0;
    }

    default void setDamage(ItemStack stack, int damage){
        ((ItemStackAccessor) (Object) stack).setItemDamage(damage);

        if (((ItemStackAccessor) (Object) stack).getItemDamage() < 0)
        {
            ((ItemStackAccessor) (Object) stack).setItemDamage(0);
        }
    }
}
