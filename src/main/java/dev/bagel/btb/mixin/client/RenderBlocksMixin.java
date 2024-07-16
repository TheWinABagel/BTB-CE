package dev.bagel.btb.mixin.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderBlocks.class)
public abstract class RenderBlocksMixin {
    @Shadow public IBlockAccess blockAccess;

    @Inject(method = "renderBlockByRenderType", at = @At(value = "HEAD", shift = At.Shift.AFTER), cancellable = true)
    private void btb$renderWorldBlock(Block block, int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
        int renderType = block.getRenderType();
        if (renderType == 1) return;
        if (RenderingRegistry.instance().renderWorldBlock(((RenderBlocks)(Object) this), this.blockAccess, i, j, k, block, renderType)) {
            cir.setReturnValue(true);
        }
    }
}
