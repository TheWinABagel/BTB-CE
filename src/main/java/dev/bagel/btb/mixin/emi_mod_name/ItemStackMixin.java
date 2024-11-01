package dev.bagel.btb.mixin.emi_mod_name;

import buildcraft.core.utils.BCItem;
import net.minecraft.src.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @ModifyArgs(method = "getTooltip", at = @At(value = "INVOKE", target = "Lemi/dev/emi/emi/EmiUtil;getModName(Ljava/lang/String;)Ljava/lang/String;", remap = false))
    private void btb$makeBcItemsHaveName(Args args) {
        if (((ItemStack)(Object) this).getItem() instanceof BCItem) {
            args.set(0, "buildcraft");
        }
    }
}
