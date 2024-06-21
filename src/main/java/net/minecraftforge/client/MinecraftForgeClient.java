package net.minecraftforge.client;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

import java.util.BitSet;

public class MinecraftForgeClient {
    private static IItemRenderer[] customItemRenderers;
    private static BitSet stencilBits;

    public static void registerItemRenderer(int itemID, IItemRenderer renderer) {
        customItemRenderers[itemID] = renderer;
    }

    public static IItemRenderer getItemRenderer(ItemStack item, IItemRenderer.ItemRenderType type) {
        IItemRenderer renderer = customItemRenderers[item.itemID];
        return renderer != null && renderer.handleRenderType(item, type) ? customItemRenderers[item.itemID] : null;
    }

    public static int getRenderPass() {
        return ForgeHooksClient.renderPass;
    }

    public static int getStencilBits() {
        return ForgeHooksClient.stencilBits;
    }

    public static int reserveStencilBit() {
        int bit = stencilBits.nextSetBit(0);
        if (bit >= 0) {
            stencilBits.clear(bit);
        }

        return bit;
    }

    public static void releaseStencilBit(int bit) {
        if (bit >= 0 && bit < getStencilBits()) {
            stencilBits.set(bit);
        }

    }

    static {
        customItemRenderers = new IItemRenderer[Item.itemsList.length];
        stencilBits = new BitSet(getStencilBits());
        stencilBits.set(0, getStencilBits());
    }
}
