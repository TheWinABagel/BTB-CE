package dev.bagel.btb.mixin;

import dev.bagel.btb.mixin.accessors.RenderManagerAccessor;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.src.Minecraft;
import net.minecraft.src.RenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    //todocore this could be extracted most likely
    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "net/minecraft/src/Minecraft.checkGLError (Ljava/lang/String;)V", ordinal = 2))
    private void btb$loadCustomEntityRenderers(CallbackInfo ci) {
        RenderingRegistry.instance().loadEntityRenderers(((RenderManagerAccessor) RenderManager.instance).getEntityRenderMap());
    }
}
