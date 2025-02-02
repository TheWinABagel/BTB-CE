package dev.bagel.btb.mixin.client;

import net.minecraft.src.*;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(print = true)
@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Shadow private Minecraft mc;

    @Shadow private RenderBlocks renderBlocksInstance;
    @Shadow private ItemStack itemToRender;

    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    private void btb$renderItemFirstPerson(EntityLivingBase par1EntityLivingBase, ItemStack par2ItemStack, int par3, CallbackInfo ci) {
        IItemRenderer.ItemRenderType type = IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON;
        TextureManager texturemanager = this.mc.getTextureManager();
        IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(par2ItemStack, type);
        if (customRenderer != null) {
            GL11.glPushMatrix();
            texturemanager.bindTexture(texturemanager.getResourceLocation(par2ItemStack.getItemSpriteNumber()));
            ForgeHooksClient.renderEquippedItem(type, customRenderer, this.renderBlocksInstance, par1EntityLivingBase, par2ItemStack);
            GL11.glPopMatrix();
            ci.cancel();
        }
    }
}
