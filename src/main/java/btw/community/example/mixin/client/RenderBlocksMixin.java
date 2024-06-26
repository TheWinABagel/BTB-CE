package btw.community.example.mixin.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderBlocks.class)
public abstract class RenderBlocksMixin {
    @Shadow public IBlockAccess blockAccess;

    @Inject(method = "doesRenderIDRenderItemIn3D", at = @At("HEAD"), cancellable = true)
    private static void custom3dBlockRendering(int par0, CallbackInfoReturnable<Boolean> cir) {
/*        switch (par0) {
            case 0, 31, 39, 13, 10, 11, 27, 22, 21, 16, 26, 32, 34, 35 -> cir.setReturnValue(cir.getReturnValue());
            default -> RenderingRegistry.instance().renderItemAsFull3DBlock(par0);
        }*/

    }

    @Inject(method = "renderBlockByRenderType", at = @At(value = "HEAD", shift = At.Shift.AFTER), cancellable = true)
    private void test(Block block, int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
        int renderType = block.getRenderType();
        if (renderType == 1) return;
        if (RenderingRegistry.instance().renderWorldBlock(((RenderBlocks)(Object) this), this.blockAccess, i, j, k, block, renderType)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "renderBlockAsItem", at = @At(value = "HEAD"), cancellable = true)
    private void test2(Block block, int iItemDamage, float fBrightness, CallbackInfo ci) {
        if (RenderingRegistry.instance().renderInventoryBlock((RenderBlocks)(Object) this, block, iItemDamage, block.getRenderType())) {
            ci.cancel();
            System.out.println(block);
        }
    }
}
