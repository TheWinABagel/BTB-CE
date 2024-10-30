package dev.bagel.btb.mixin;

import buildcraft.core.ItemBuildCraft;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export = true)
@Mixin(ItemInWorldManager.class)
public abstract class ItemInWorldManagerMixin {

    @Inject(method = "activateBlockOrUseItem", at = @At(value = "HEAD"), cancellable = true)
    private void btb$onItemUseFirstHook(EntityPlayer entityPlayer, World world, ItemStack itemStack, int par4, int par5, int par6, int par7, float par8, float par9, float par10, CallbackInfoReturnable<Boolean> cir) {
        Item item = itemStack != null ? itemStack.getItem() : null;
        if (item instanceof ItemBuildCraft itemBc && itemBc.onItemUseFirst(itemStack, entityPlayer, world, par4, par5, par6, par7, par8, par9, par10)) {
            System.out.println("activate block ItemInWorldManager onItemFirstUse: TRUE");
            cir.setReturnValue(true);
        } else {
            System.out.println("activate block ItemInWorldManager onItemFirstUse: FALSE");
        }
    }

    @Redirect(method = "activateBlockOrUseItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityPlayer;isSneaking()Z"))
    private boolean btb$shouldPassSneakClickToBlockHook(EntityPlayer player, EntityPlayer entityPlayer, World world, ItemStack stack, int i, int j, int k, int par7, float par8, float par9, float par10) {
        boolean shouldNotPass = player.isSneaking() || (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemBuildCraft itemBc && !itemBc.shouldPassSneakingClickToBlock(world, i, j, k));
        System.out.println("activate block ItemInWorldManager isSneaking: (inverted) " + shouldNotPass);
        return shouldNotPass;
    }
}
