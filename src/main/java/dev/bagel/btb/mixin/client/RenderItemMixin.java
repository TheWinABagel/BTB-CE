package dev.bagel.btb.mixin.client;

import net.minecraft.src.*;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(RenderItem.class)
public abstract class RenderItemMixin extends Render {
    @Shadow private Random random;
    @Shadow public boolean renderWithColor;

    @Shadow public float zLevel;

    @Inject(method = "renderItemAndEffectIntoGUI", at = @At(value = "HEAD"), cancellable = true)
    private void btb$renderCustomInvItem(FontRenderer fontRenderer, TextureManager textureManager, ItemStack stack, int x, int y, CallbackInfo ci) {
        if (stack != null && ForgeHooksClient.renderInventoryItem(this.renderBlocks, textureManager, stack, this.renderWithColor, this.zLevel, (float) x, (float) y)) {
            ci.cancel();
        }
    }

    //this is fine mcdev just is dumb
    @Inject(method = "doRenderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ItemStack;getItemSpriteNumber()I"),
            locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void btb$renderCustomItemEntity(EntityItem par1EntityItem, double par2, double par4, double par6, float par8, float par9, CallbackInfo ci, ItemStack stack, float bobbing, float rotation) {
        if (ForgeHooksClient.renderEntityItem(par1EntityItem, stack, bobbing, rotation, this.random, this.renderManager.renderEngine, this.renderBlocks)) {
            //these are called normally afterwards
            GL11.glDisable(32826);
            GL11.glPopMatrix();
            ci.cancel();
        }
    }
}
