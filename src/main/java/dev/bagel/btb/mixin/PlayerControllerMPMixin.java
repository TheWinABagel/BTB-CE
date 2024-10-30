package dev.bagel.btb.mixin;

import buildcraft.core.ItemBuildCraft;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Debug(export = true)
@Mixin(PlayerControllerMP.class)
public class PlayerControllerMPMixin {
    @Inject(method = "onPlayerRightClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityPlayer;isSneaking()Z", shift = At.Shift.BEFORE), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void btb$onItemUseFirstHook(EntityPlayer par1EntityPlayer, World par2World, ItemStack par3ItemStack, int par4, int par5, int par6, int par7, Vec3 par8Vec3, CallbackInfoReturnable<Boolean> cir, float f, float f1, float f2) {
        if (par3ItemStack != null && par3ItemStack.getItem() instanceof ItemBuildCraft itemBc && itemBc.onItemUseFirst(par3ItemStack, par1EntityPlayer, par2World, par4, par5, par6, par7, f, f1, f2)) {
            System.out.println("activate block PlayerControllerMP onItemFirstUse: TRUE");
            cir.setReturnValue(true);
        } else {
            System.out.println("activate block PlayerControllerMP onItemFirstUse: FALSE");
        }
    }

    @Redirect(method = "onPlayerRightClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityPlayer;isSneaking()Z"))
    private boolean btb$shouldPassSneakClickToBlockHook(EntityPlayer instance, EntityPlayer par1EntityPlayer, World par2World, ItemStack par3ItemStack, int par4, int par5, int par6, int par7, Vec3 par8Vec3) {
        boolean shouldNotPass = instance.isSneaking() || (par1EntityPlayer.getHeldItem() != null && instance.getHeldItem().getItem() instanceof ItemBuildCraft itemBc && !itemBc.shouldPassSneakingClickToBlock(par2World, par4, par5, par6));
        System.out.println("activate block PlayerControllerMP isSneaking: (inverted) " + shouldNotPass);
        return shouldNotPass;
    }
}
