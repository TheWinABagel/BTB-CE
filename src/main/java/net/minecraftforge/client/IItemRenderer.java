package net.minecraftforge.client;

import net.minecraft.src.ItemStack;

public interface IItemRenderer { //todotransport HIGH implement item rendering
    boolean handleRenderType(ItemStack itemStack, IItemRenderer.ItemRenderType itemRenderType);

    boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType itemRenderType, ItemStack itemStack, IItemRenderer.ItemRendererHelper itemRendererHelper);

    /**
     * Renders the item stack for being in an entity's hand Args: itemStack
     */
    void renderItem(IItemRenderer.ItemRenderType itemRenderType, ItemStack itemStack, Object... objects);

    public static enum ItemRendererHelper {
        ENTITY_ROTATION,
        ENTITY_BOBBING,
        EQUIPPED_BLOCK,
        BLOCK_3D,
        INVENTORY_BLOCK;
    }

    public static enum ItemRenderType {
        ENTITY,
        EQUIPPED,
        EQUIPPED_FIRST_PERSON,
        INVENTORY,
        FIRST_PERSON_MAP;
    }
}
