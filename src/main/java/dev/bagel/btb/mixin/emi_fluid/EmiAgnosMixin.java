package dev.bagel.btb.mixin.emi_fluid;

import emi.dev.emi.emi.platform.EmiAgnos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EmiAgnos.class)
public class EmiAgnosMixin {

    /**
     * @author TheWinABagel
     * @reason Make sure default fluid rendering is in Milli buckets
     */
    @Overwrite(remap = false)
    public static boolean isForge() {
        return true;
    }
}
