package dev.bagel.btb.mixin;

import dev.bagel.btb.injected.ItemExtension;
import buildcraft.transport.gates.ItemGate;
import net.minecraft.src.Icon;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin implements ItemExtension {

    @Inject(method = "getIconIndex", at = @At("HEAD"), cancellable = true)
    private void buildcraft$changeIconIndex(ItemStack stack, CallbackInfoReturnable<Icon> cir) {
        if (((Item) (Object) this) instanceof ItemGate) {
            cir.setReturnValue(ItemGate.getLogic(stack).getIconItem());
        }
    }

}
