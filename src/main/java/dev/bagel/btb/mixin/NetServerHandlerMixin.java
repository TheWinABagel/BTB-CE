package dev.bagel.btb.mixin;

import net.minecraft.src.ItemStack;
import net.minecraft.src.NetServerHandler;
import net.minecraft.src.Packet102WindowClick;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(NetServerHandler.class)
public class NetServerHandlerMixin {

    @Inject(method = "handleWindowClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ItemStack;areItemStacksEqual(Lnet/minecraft/src/ItemStack;Lnet/minecraft/src/ItemStack;)Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void btb$testWhatStackIs(Packet102WindowClick par1Packet102WindowClick, CallbackInfo ci, ItemStack var2) {
        System.out.println("current stack recieved on server: " +var2 + " packet stack: "+par1Packet102WindowClick.itemStack);
    }
}
