package net.minecraftforge.client;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

import java.util.BitSet;

public class MinecraftForgeClient {
    private static final IItemRenderer[] customItemRenderers = new IItemRenderer[Item.itemsList.length];

    public static void registerItemRenderer(int itemID, IItemRenderer renderer) {
        customItemRenderers[itemID] = renderer;
    }

    public static IItemRenderer getItemRenderer(ItemStack item, IItemRenderer.ItemRenderType type) {
        IItemRenderer renderer = customItemRenderers[item.itemID];
        return renderer != null && renderer.handleRenderType(item, type) ? customItemRenderers[item.itemID] : null;
    }
}
