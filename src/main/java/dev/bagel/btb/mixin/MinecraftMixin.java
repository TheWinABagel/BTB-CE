package dev.bagel.btb.mixin;

import cpw.mods.fml.client.registry.RenderingRegistry;
import dev.bagel.btb.injected.CustomDestroyEffectsBlock;
import dev.bagel.btb.mixin.accessors.RenderManagerAccessor;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow public MovingObjectPosition objectMouseOver;

    //todocore this could be extracted most likely
    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "net/minecraft/src/Minecraft.checkGLError (Ljava/lang/String;)V", ordinal = 2))
    private void btb$loadCustomEntityRenderers(CallbackInfo ci) {
        RenderingRegistry.instance().loadEntityRenderers(((RenderManagerAccessor) RenderManager.instance).getEntityRenderMap());
    }

    @Redirect(method = "sendClickBlockToController", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EffectRenderer;addBlockHitEffects(IIII)V"))
    private void btb$redirectBlockEffects(EffectRenderer instance, int x, int y, int z, int sideHit) {
        Block block = Block.blocksList[instance.worldObj.getBlockId(x, y, z)];
        if (block != null && !((CustomDestroyEffectsBlock) block).addBlockHitEffects(instance.worldObj, this.objectMouseOver, instance)) {
            instance.addBlockHitEffects(x, y, z, sideHit);
        }
    }
}
