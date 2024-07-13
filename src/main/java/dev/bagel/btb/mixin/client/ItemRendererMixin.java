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
//    private boolean btb$alreadyRendered = false;
    @Shadow private Minecraft mc;

    @Shadow private RenderBlocks renderBlocksInstance;
    @Shadow private ItemStack itemToRender;

    @Inject(method = "renderItemInFirstPerson", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ItemMap;getMapData(Lnet/minecraft/src/ItemStack;Lnet/minecraft/src/World;)Lnet/minecraft/src/MapData;", shift = At.Shift.BEFORE), cancellable = true)
    private void test2(float par1, CallbackInfo ci) {
        System.out.println("call plz");
        IItemRenderer custom = MinecraftForgeClient.getItemRenderer(this.itemToRender, IItemRenderer.ItemRenderType.FIRST_PERSON_MAP);
        if (custom != null) {

            custom.renderItem(IItemRenderer.ItemRenderType.FIRST_PERSON_MAP, this.itemToRender, this.mc.thePlayer, this.mc.getTextureManager(), Item.map.getMapData(this.itemToRender, this.mc.theWorld));
            GL11.glPopMatrix();

            GL11.glDisable(32826);
            RenderHelper.disableStandardItemLighting();

            ci.cancel();
        }
    }

    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    private void test(EntityLivingBase par1EntityLivingBase, ItemStack par2ItemStack, int par3, CallbackInfo ci) {
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
