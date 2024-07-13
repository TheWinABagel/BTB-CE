package dev.bagel.btb.mixin.client;

import net.minecraft.src.Block;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBiped;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderBiped.class)
public class RenderBipedMixin {

    @Redirect(method = "func_130005_c", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Block;doesItemRenderAsBlock(I)Z", ordinal = 0))
    private boolean test(Block instance, int iItemDamage, EntityLiving clientPlayer, float par2) {
        ItemStack itemstack = clientPlayer.func_130225_q(3);
        IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemstack, IItemRenderer.ItemRenderType.EQUIPPED);
        boolean is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(IItemRenderer.ItemRenderType.EQUIPPED, itemstack, IItemRenderer.ItemRendererHelper.BLOCK_3D);
        return instance.doesItemRenderAsBlock(iItemDamage) || is3D;
    }

    @Redirect(method = "func_130005_c", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Block;doesItemRenderAsBlock(I)Z", ordinal = 1))
    private boolean test2(Block instance, int iItemDamage, EntityLiving clientPlayer, float par2) {
        ItemStack itemstack = clientPlayer.getHeldItem();
        IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemstack, IItemRenderer.ItemRenderType.EQUIPPED);
        boolean is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(IItemRenderer.ItemRenderType.EQUIPPED, itemstack, IItemRenderer.ItemRendererHelper.BLOCK_3D);
        return instance.doesItemRenderAsBlock(iItemDamage) || is3D;
    }
}
