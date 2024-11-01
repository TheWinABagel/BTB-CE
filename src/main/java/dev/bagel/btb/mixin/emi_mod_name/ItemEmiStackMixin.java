package dev.bagel.btb.mixin.emi_mod_name;

import buildcraft.core.utils.BCItem;
import emi.dev.emi.emi.api.stack.ItemEmiStack;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemEmiStack.class, remap = false)
public abstract class ItemEmiStackMixin {
    @Shadow public abstract boolean isEmpty();

    @Shadow @Final private ItemStack stack;

    @Inject(method = "getId", at = @At("RETURN"), cancellable = true)
    private void btb$changeBcId(CallbackInfoReturnable<ResourceLocation> cir) {
        if (!this.isEmpty() && this.stack.getItem() instanceof BCItem) {
            cir.setReturnValue(new ResourceLocation("buildcraft:", this.stack.itemID + "/" + this.stack.getItemDamage()));
        }
    }
}
