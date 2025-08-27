package dev.bagel.btb.mixin;

import net.minecraft.src.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/io/PrintStream;println(Ljava/lang/String;)V"))
    private void method(int par1, CallbackInfo ci) {
        Item replaced = Item.itemsList[256 + par1];

        System.out.println("CONFLICT @ " + par1 + ". Old item: { unlocalized name: " + replaced.getUnlocalizedName() + ", class name: " + replaced.getClass().getName() + "}" +
                "\n New Class name: " + this.getClass().getName() + ", caller info:\n" + new Exception().getStackTrace()[1].toString());

    }
}
