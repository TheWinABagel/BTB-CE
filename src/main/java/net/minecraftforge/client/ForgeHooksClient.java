package net.minecraftforge.client;

import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class ForgeHooksClient {
    static int renderPass = -1;
    static int stencilBits = 0;

    public static boolean renderEntityItem(EntityItem entity, ItemStack item, float bobing, float rotation, Random random, TextureManager engine, RenderBlocks renderBlocks) {
        IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(item, IItemRenderer.ItemRenderType.ENTITY);
        if (customRenderer == null) {
            return false;
        } else {
            if (customRenderer.shouldUseRenderHelper(IItemRenderer.ItemRenderType.ENTITY, item, IItemRenderer.ItemRendererHelper.ENTITY_ROTATION)) {
                GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
            }

            if (!customRenderer.shouldUseRenderHelper(IItemRenderer.ItemRenderType.ENTITY, item, IItemRenderer.ItemRendererHelper.ENTITY_BOBBING)) {
                GL11.glTranslatef(0.0F, -bobing, 0.0F);
            }

            boolean is3D = customRenderer.shouldUseRenderHelper(IItemRenderer.ItemRenderType.ENTITY, item, IItemRenderer.ItemRendererHelper.BLOCK_3D);
            engine.bindTexture(item.getItemSpriteNumber() == 0 ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
            Block block = item.itemID < Block.blocksList.length ? Block.blocksList[item.itemID] : null;
            if (!is3D && (block == null || !RenderBlocks.doesRenderIDRenderItemIn3D(block.getRenderType()))) {
                GL11.glScalef(0.5F, 0.5F, 0.5F);
                customRenderer.renderItem(IItemRenderer.ItemRenderType.ENTITY, item, renderBlocks, entity);
            } else {
                int renderType = block != null ? block.getRenderType() : 1;
                float scale = renderType != 1 && renderType != 19 && renderType != 12 && renderType != 2 ? 0.25F : 0.5F;
                if (RenderItem.renderInFrame) {
                    GL11.glScalef(1.25F, 1.25F, 1.25F);
                    GL11.glTranslatef(0.0F, 0.05F, 0.0F);
                    GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                GL11.glScalef(scale, scale, scale);
                int size = item.stackSize;
                int count = size > 40 ? 5 : (size > 20 ? 4 : (size > 5 ? 3 : (size > 1 ? 2 : 1)));

                for(int j = 0; j < count; ++j) {
                    GL11.glPushMatrix();
                    if (j > 0) {
                        GL11.glTranslatef((random.nextFloat() * 2.0F - 1.0F) * 0.2F / scale, (random.nextFloat() * 2.0F - 1.0F) * 0.2F / scale, (random.nextFloat() * 2.0F - 1.0F) * 0.2F / scale);
                    }

                    customRenderer.renderItem(IItemRenderer.ItemRenderType.ENTITY, item, renderBlocks, entity);
                    GL11.glPopMatrix();
                }
            }

            return true;
        }
    }

    public static boolean renderInventoryItem(RenderBlocks renderBlocks, TextureManager engine, ItemStack item, boolean inColor, float zLevel, float x, float y) {
        IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(item, IItemRenderer.ItemRenderType.INVENTORY);
        if (customRenderer == null) {
            return false;
        } else {
            engine.bindTexture(item.getItemSpriteNumber() == 0 ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
            int color;
            float r;
            float g;
            float b;
            if (customRenderer.shouldUseRenderHelper(IItemRenderer.ItemRenderType.INVENTORY, item, IItemRenderer.ItemRendererHelper.INVENTORY_BLOCK)) {
                GL11.glPushMatrix();
                GL11.glTranslatef(x - 2.0F, y + 3.0F, -3.0F + zLevel);
                GL11.glScalef(10.0F, 10.0F, 10.0F);
                GL11.glTranslatef(1.0F, 0.5F, 1.0F);
                GL11.glScalef(1.0F, 1.0F, -1.0F);
                GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                if (inColor) {
                    color = Item.itemsList[item.itemID].getColorFromItemStack(item, 0);
                    r = (float)(color >> 16 & 255) / 255.0F;
                    g = (float)(color >> 8 & 255) / 255.0F;
                    b = (float)(color & 255) / 255.0F;
                    GL11.glColor4f(r, g, b, 1.0F);
                }

                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                renderBlocks.useInventoryTint = inColor;
                customRenderer.renderItem(IItemRenderer.ItemRenderType.INVENTORY, item, renderBlocks);
                renderBlocks.useInventoryTint = true;
                GL11.glPopMatrix();
            } else {
                GL11.glDisable(2896);
                GL11.glPushMatrix();
                GL11.glTranslatef(x, y, -3.0F + zLevel);
                if (inColor) {
                    color = Item.itemsList[item.itemID].getColorFromItemStack(item, 0);
                    r = (float)(color >> 16 & 255) / 255.0F;
                    g = (float)(color >> 8 & 255) / 255.0F;
                    b = (float)(color & 255) / 255.0F;
                    GL11.glColor4f(r, g, b, 1.0F);
                }

                customRenderer.renderItem(IItemRenderer.ItemRenderType.INVENTORY, item, renderBlocks);
                GL11.glPopMatrix();
                GL11.glEnable(2896);
            }

            return true;
        }
    }

    public static void renderEquippedItem(IItemRenderer.ItemRenderType type, IItemRenderer customRenderer, RenderBlocks renderBlocks, EntityLivingBase entity, ItemStack item) {
        if (customRenderer.shouldUseRenderHelper(type, item, IItemRenderer.ItemRendererHelper.EQUIPPED_BLOCK)) {
            GL11.glPushMatrix();
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            customRenderer.renderItem(type, item, renderBlocks, entity);
            GL11.glPopMatrix();
        } else {
            GL11.glPushMatrix();
            GL11.glEnable(32826);
            GL11.glTranslatef(0.0F, -0.3F, 0.0F);
            GL11.glScalef(1.5F, 1.5F, 1.5F);
            GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
            customRenderer.renderItem(type, item, renderBlocks, entity);
            GL11.glDisable(32826);
            GL11.glPopMatrix();
        }

    }
}
