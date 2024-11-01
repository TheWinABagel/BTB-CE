package dev.bagel.btb.mixin.emi_mod_name;

import buildcraft.core.utils.BCItem;
import emi.dev.emi.emi.EmiUtil;
import emi.dev.emi.emi.Prototype;
import emi.shims.java.com.unascribed.retroemi.RetroEMI;
import net.minecraft.src.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = RetroEMI.class, remap = false)
public class RetroEmiMixin {

    @Inject(method = "getMod", at = @At("RETURN"), cancellable = true)
    private static void btb$fixModName(Object o, CallbackInfoReturnable<String> cir) {
        if (o instanceof ResourceLocation id) {
            cir.setReturnValue(EmiUtil.getModName(id.getResourceDomain()));
        } else if (o instanceof Prototype proto) {
            if (proto.toStack() != null && proto.toStack().getItem() instanceof BCItem) {
                cir.setReturnValue(EmiUtil.getModName("buildcraft"));
            }
        }
    }
}
