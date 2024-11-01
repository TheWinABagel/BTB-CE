package dev.bagel.btb.mixin.emi_mod_name;

import emi.dev.emi.emi.EmiUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EmiUtil.class, remap = false)
public class EmiUtilMixin {

    @Inject(method = "getModName", at = @At("RETURN"), cancellable = true)
    private static void btb$makeBcItemsHaveName(String namespace, CallbackInfoReturnable<String> cir) {
        if (namespace.equals("buildcraft") || namespace.equals("btb")) {
            cir.setReturnValue("Better Then Buildcraft");
        }
    }
}
