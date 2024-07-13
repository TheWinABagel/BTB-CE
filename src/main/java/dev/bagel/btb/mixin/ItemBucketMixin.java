package dev.bagel.btb.mixin;

import buildcraft.energy.BucketHandler;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemBucket.class)
public abstract class ItemBucketMixin extends Item {

    public ItemBucketMixin(int par1) {
        super(par1);
    }

    @Inject(method = "onItemRightClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ItemBucket;getMovingObjectPositionFromPlayer(Lnet/minecraft/src/World;Lnet/minecraft/src/EntityPlayer;Z)Lnet/minecraft/src/MovingObjectPosition;"),
            locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void test(ItemStack stack, World world, EntityPlayer player, CallbackInfoReturnable<ItemStack> cir, boolean isFull) {
        if (stack == null) return;
        MovingObjectPosition pos = this.getMovingObjectPositionFromPlayer(world, player, isFull);
        ItemStack eventStack = BucketHandler.INSTANCE.onBucketFill(player, stack, world, pos);
        if (eventStack != null) {
            cir.setReturnValue(eventStack);
        }
    }
}
