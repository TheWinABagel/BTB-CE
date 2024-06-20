package btw.community.example.mixin.accessors;

import net.minecraft.src.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemStack.class)
public interface ItemStackAccessor {
    @Accessor
    int getItemDamage();

    @Accessor
    void setItemDamage(int itemDamage);
}
