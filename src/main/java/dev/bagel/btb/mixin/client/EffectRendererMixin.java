package dev.bagel.btb.mixin.client;

import dev.bagel.btb.injected.CustomDestroyEffectsBlock;
import net.minecraft.src.Block;
import net.minecraft.src.EffectRenderer;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EffectRenderer.class)
public abstract class EffectRendererMixin {

    @Shadow protected World worldObj;

    @Inject(method = "addBlockDestroyEffects", at = @At("HEAD"), cancellable = true)
    private void test(int par1, int par2, int par3, int par4, int par5, CallbackInfo ci) {
        Block block = Block.blocksList[par4];
        if (block != null && ((CustomDestroyEffectsBlock) block).addBlockDestroyEffects(this.worldObj, par1, par2, par3, par5, (EffectRenderer) (Object) this)) {
            ci.cancel();
        }
    }
}
