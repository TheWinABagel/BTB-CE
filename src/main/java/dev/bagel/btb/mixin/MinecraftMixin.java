package dev.bagel.btb.mixin;

import dev.bagel.btb.injected.CustomDestroyEffectsBlock;
import net.minecraft.src.Block;
import net.minecraft.src.EffectRenderer;
import net.minecraft.src.Minecraft;
import net.minecraft.src.MovingObjectPosition;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Debug(export = true)
@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow public MovingObjectPosition objectMouseOver;

    //todocore this could be extracted most likely
/*    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "net/minecraft/src/Minecraft.checkGLError (Ljava/lang/String;)V", ordinal = 2))
    private void btb$loadCustomEntityRenderers(CallbackInfo ci) {

        RenderingRegistry.instance().loadEntityRenderers(((RenderManagerAccessor) RenderManager.instance).getEntityRenderMap());
    }*/

    @Redirect(method = "sendClickBlockToController", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EffectRenderer;addBlockHitEffects(IIII)V"))
    private void btb$redirectBlockEffects(EffectRenderer instance, int x, int y, int z, int sideHit) {
        Block block = Block.blocksList[instance.worldObj.getBlockId(x, y, z)];
        if (block != null && !((CustomDestroyEffectsBlock) block).addBlockHitEffects(instance.worldObj, this.objectMouseOver, instance)) {
            instance.addBlockHitEffects(x, y, z, sideHit);
        }
    }
}
