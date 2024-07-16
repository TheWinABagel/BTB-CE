package net.minecraftforge.client;

import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

import java.util.Random;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.ENTITY;
import static net.minecraftforge.client.IItemRenderer.ItemRenderType.INVENTORY;
import static net.minecraftforge.client.IItemRenderer.ItemRendererHelper.*;

public class ForgeHooksClient {
    static int renderPass = -1;
    static int stencilBits = 0;

    public static boolean renderEntityItem(EntityItem entity, ItemStack item, float bobing, float rotation, Random random, TextureManager engine, RenderBlocks renderBlocks) {
        IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(item, ENTITY);
        if (customRenderer == null) {
            return false;
        }

        if (customRenderer.shouldUseRenderHelper(ENTITY, item, ENTITY_ROTATION)) {
            GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
        }
        if (!customRenderer.shouldUseRenderHelper(ENTITY, item, ENTITY_BOBBING)) {
            GL11.glTranslatef(0.0F, -bobing, 0.0F);
        }
        boolean is3D = customRenderer.shouldUseRenderHelper(ENTITY, item, BLOCK_3D);

        engine.bindTexture(item.getItemSpriteNumber() == 0 ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
        Block block = (item.itemID < Block.blocksList.length ? Block.blocksList[item.itemID] : null);
        if (!is3D && (block == null || !RenderBlocks.doesRenderIDRenderItemIn3D(block.getRenderType()))) {
            int renderType = (block != null ? block.getRenderType() : 1);
            float scale = (renderType == 1 || renderType == 19 || renderType == 12 || renderType == 2 ? 0.5F : 0.25F);

            if (RenderItem.renderInFrame) {
                GL11.glScalef(1.25F, 1.25F, 1.25F);
                GL11.glTranslatef(0.0F, 0.05F, 0.0F);
                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            }

            GL11.glScalef(scale, scale, scale);

            int size = item.stackSize;
            int count = (size > 40 ? 5 : (size > 20 ? 4 : (size > 5 ? 3 : (size > 1 ? 2 : 1))));

            for (int j = 0; j < count; j++) {
                GL11.glPushMatrix();
                if (j > 0) {
                    GL11.glTranslatef(
                            ((random.nextFloat() * 2.0F - 1.0F) * 0.2F) / scale,
                            ((random.nextFloat() * 2.0F - 1.0F) * 0.2F) / scale,
                            ((random.nextFloat() * 2.0F - 1.0F) * 0.2F) / scale);
                }
                customRenderer.renderItem(ENTITY, item, renderBlocks, entity);
                GL11.glPopMatrix();
            }
        } else {
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            customRenderer.renderItem(ENTITY, item, renderBlocks, entity);
        }
        return true;
    }

    public static boolean renderInventoryItem(RenderBlocks renderBlocks, TextureManager engine, ItemStack item, boolean inColor, float zLevel, float x, float y) {
        IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(item, INVENTORY);
        if (customRenderer == null) {
            return false;
        }

        engine.bindTexture(item.getItemSpriteNumber() == 0 ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
        if (customRenderer.shouldUseRenderHelper(INVENTORY, item, INVENTORY_BLOCK)) {
            GL11.glPushMatrix();
            GL11.glTranslatef(x - 2, y + 3, -3.0F + zLevel);
            GL11.glScalef(10F, 10F, 10F);
            GL11.glTranslatef(1.0F, 0.5F, 1.0F);
            GL11.glScalef(1.0F, 1.0F, -1F);
            GL11.glRotatef(210F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);

            if (inColor) {
                int color = Item.itemsList[item.itemID].getColorFromItemStack(item, 0);
                float r = (float) (color >> 16 & 0xff) / 255F;
                float g = (float) (color >> 8 & 0xff) / 255F;
                float b = (float) (color & 0xff) / 255F;
                GL11.glColor4f(r, g, b, 1.0F);
            }

            GL11.glRotatef(-90F, 0.0F, 1.0F, 0.0F);
            renderBlocks.useInventoryTint = inColor;
            customRenderer.renderItem(INVENTORY, item, renderBlocks);
            renderBlocks.useInventoryTint = true;
            GL11.glPopMatrix();
        } else {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glPushMatrix();
            GL11.glTranslatef(x, y, -3.0F + zLevel);

            if (inColor) {
                int color = Item.itemsList[item.itemID].getColorFromItemStack(item, 0);
                float r = (float) (color >> 16 & 255) / 255.0F;
                float g = (float) (color >> 8 & 255) / 255.0F;
                float b = (float) (color & 255) / 255.0F;
                GL11.glColor4f(r, g, b, 1.0F);
            }

            customRenderer.renderItem(INVENTORY, item, renderBlocks);
            GL11.glPopMatrix();
            GL11.glEnable(GL11.GL_LIGHTING);
        }

        return true;
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
