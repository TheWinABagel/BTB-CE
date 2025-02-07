package dev.bagel.btb.mixin;

import net.minecraft.src.ItemFood;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemFood.class)
public class test {
    @ModifyVariable(at = @At("HEAD"), method = "<init>(IIFZ)V", ordinal = 1, argsOnly = true)
    private static int modifyHealAmount(int healAmount) {
        return healAmount * 5;
    }

}
