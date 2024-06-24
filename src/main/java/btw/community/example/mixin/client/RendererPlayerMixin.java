package btw.community.example.mixin.client;

import btw.community.example.injected.client.ItemRenderExtension;
import net.minecraft.src.*;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RenderPlayer.class)
public abstract class RendererPlayerMixin extends RendererLivingEntity {

    @Shadow
    private ModelBiped modelBipedMain;

    public RendererPlayerMixin(ModelBase par1ModelBase, float par2) {
        super(par1ModelBase, par2);
    }

    public void renderSpecials(AbstractClientPlayer clientPlayer, float par2) {
        float f1 = 1.0F;
        GL11.glColor3f(f1, f1, f1);
        super.renderEquippedItems(clientPlayer, par2);
        super.renderArrowsStuckInEntity(clientPlayer, par2);
        ItemStack itemstack = clientPlayer.inventory.armorItemInSlot(3);
        boolean flag2;
        if (itemstack != null) {
            GL11.glPushMatrix();
            this.modelBipedMain.bipedHead.postRender(0.0625F);
            float f2;
            if (itemstack != null && itemstack.getItem() instanceof ItemBlock) {
                IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemstack, IItemRenderer.ItemRenderType.EQUIPPED);
                flag2 = customRenderer != null && customRenderer.shouldUseRenderHelper(IItemRenderer.ItemRenderType.EQUIPPED, itemstack, IItemRenderer.ItemRendererHelper.BLOCK_3D);
                if (flag2 || RenderBlocks.doesRenderIDRenderItemIn3D(Block.blocksList[itemstack.itemID].getRenderType())) {
                    f2 = 0.625F;
                    GL11.glTranslatef(0.0F, -0.25F, 0.0F);
                    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glScalef(f2, -f2, -f2);
                }

                this.renderManager.itemRenderer.renderItem(clientPlayer, itemstack, 0);
            } else if (itemstack.getItem().itemID == Item.skull.itemID) {
                f2 = 1.0625F;
                GL11.glScalef(f2, -f2, -f2);
                String s = "";
                if (itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("SkullOwner")) {
                    s = itemstack.getTagCompound().getString("SkullOwner");
                }

                TileEntitySkullRenderer.skullRenderer.func_82393_a(-0.5F, 0.0F, -0.5F, 1, 180.0F, itemstack.getItemDamage(), s);
            }

            GL11.glPopMatrix();
        }

        float f6;
        if (clientPlayer.getCommandSenderName().equals("deadmau5") && clientPlayer.getTextureSkin().isTextureUploaded()) {
            this.bindTexture(clientPlayer.getLocationSkin());

            for (int i = 0; i < 2; ++i) {
                float f3 = clientPlayer.prevRotationYaw + (clientPlayer.rotationYaw - clientPlayer.prevRotationYaw) * par2 - (clientPlayer.prevRenderYawOffset + (clientPlayer.renderYawOffset - clientPlayer.prevRenderYawOffset) * par2);
                float f4 = clientPlayer.prevRotationPitch + (clientPlayer.rotationPitch - clientPlayer.prevRotationPitch) * par2;
                GL11.glPushMatrix();
                GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(f4, 1.0F, 0.0F, 0.0F);
                GL11.glTranslatef(0.375F * (float) (i * 2 - 1), 0.0F, 0.0F);
                GL11.glTranslatef(0.0F, -0.375F, 0.0F);
                GL11.glRotatef(-f4, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(-f3, 0.0F, 1.0F, 0.0F);
                f6 = 1.3333334F;
                GL11.glScalef(f6, f6, f6);
                this.modelBipedMain.renderEars(0.0625F);
                GL11.glPopMatrix();
            }
        }

        boolean flag = clientPlayer.getTextureCape().isTextureUploaded();
        boolean flag1 = !clientPlayer.isInvisible();
        flag2 = !clientPlayer.getHideCape();
        if (flag && flag1 && flag2) {
            this.bindTexture(clientPlayer.getLocationCape());
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
            double d0 = clientPlayer.field_71091_bM + (clientPlayer.field_71094_bP - clientPlayer.field_71091_bM) * (double) par2 - (clientPlayer.prevPosX + (clientPlayer.posX - clientPlayer.prevPosX) * (double) par2);
            double d1 = clientPlayer.field_71096_bN + (clientPlayer.field_71095_bQ - clientPlayer.field_71096_bN) * (double) par2 - (clientPlayer.prevPosY + (clientPlayer.posY - clientPlayer.prevPosY) * (double) par2);
            double d2 = clientPlayer.field_71097_bO + (clientPlayer.field_71085_bR - clientPlayer.field_71097_bO) * (double) par2 - (clientPlayer.prevPosZ + (clientPlayer.posZ - clientPlayer.prevPosZ) * (double) par2);
            f6 = clientPlayer.prevRenderYawOffset + (clientPlayer.renderYawOffset - clientPlayer.prevRenderYawOffset) * par2;
            double d3 = (double) MathHelper.sin(f6 * 3.1415927F / 180.0F);
            double d4 = (double) (-MathHelper.cos(f6 * 3.1415927F / 180.0F));
            float f7 = (float) d1 * 10.0F;
            if (f7 < -6.0F) {
                f7 = -6.0F;
            }

            if (f7 > 32.0F) {
                f7 = 32.0F;
            }

            float f8 = (float) (d0 * d3 + d2 * d4) * 100.0F;
            float f9 = (float) (d0 * d4 - d2 * d3) * 100.0F;
            if (f8 < 0.0F) {
                f8 = 0.0F;
            }

            float f10 = clientPlayer.prevCameraYaw + (clientPlayer.cameraYaw - clientPlayer.prevCameraYaw) * par2;
            f7 += MathHelper.sin((clientPlayer.prevDistanceWalkedModified + (clientPlayer.distanceWalkedModified - clientPlayer.prevDistanceWalkedModified) * par2) * 6.0F) * 32.0F * f10;
            if (clientPlayer.isSneaking()) {
                f7 += 25.0F;
            }

            GL11.glRotatef(6.0F + f8 / 2.0F + f7, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(f9 / 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-f9 / 2.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            this.modelBipedMain.renderCloak(0.0625F);
            GL11.glPopMatrix();
        }

        ItemStack itemstack1 = clientPlayer.inventory.getCurrentItem();
        if (itemstack1 != null) {
            GL11.glPushMatrix();
            this.modelBipedMain.bipedRightArm.postRender(0.0625F);
            GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);
            if (clientPlayer.fishEntity != null) {
                itemstack1 = new ItemStack(Item.stick);
            }

            EnumAction enumaction = null;
            if (clientPlayer.getItemInUseCount() > 0) {
                enumaction = itemstack1.getItemUseAction();
            }

            IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemstack1, IItemRenderer.ItemRenderType.EQUIPPED);
            boolean is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(IItemRenderer.ItemRenderType.EQUIPPED, itemstack1, IItemRenderer.ItemRendererHelper.BLOCK_3D);
            boolean isBlock = itemstack1.itemID < Block.blocksList.length && itemstack1.getItemSpriteNumber() == 0;
            float f11;
            if (!is3D && (!isBlock || !RenderBlocks.doesRenderIDRenderItemIn3D(Block.blocksList[itemstack1.itemID].getRenderType()))) {
                if (itemstack1.itemID == Item.bow.itemID) {
                    f11 = 0.625F;
                    GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
                    GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glScalef(f11, -f11, f11);
                    GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                } else if (Item.itemsList[itemstack1.itemID].isFull3D()) {
                    f11 = 0.625F;
                    if (Item.itemsList[itemstack1.itemID].shouldRotateAroundWhenRendering()) {
                        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                        GL11.glTranslatef(0.0F, -0.125F, 0.0F);
                    }

                    if (clientPlayer.getItemInUseCount() > 0 && enumaction == EnumAction.block) {
                        GL11.glTranslatef(0.05F, 0.0F, -0.1F);
                        GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
                        GL11.glRotatef(-10.0F, 1.0F, 0.0F, 0.0F);
                        GL11.glRotatef(-60.0F, 0.0F, 0.0F, 1.0F);
                    }

                    GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
                    GL11.glScalef(f11, -f11, f11);
                    GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                } else {
                    f11 = 0.375F;
                    GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
                    GL11.glScalef(f11, f11, f11);
                    GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
                    GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
                }
            } else {
                f11 = 0.5F;
                GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
                f11 *= 0.75F;
                GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(-f11, -f11, f11);
            }

            int j;
            float f12;
            float f13;
            if (itemstack1.getItem().requiresMultipleRenderPasses()) {
                for (j = 0; j < ((ItemRenderExtension) itemstack1.getItem()).getRenderPasses(itemstack1.getItemDamage()); ++j) {
                    int k = itemstack1.getItem().getColorFromItemStack(itemstack1, j);
                    f13 = (float) (k >> 16 & 255) / 255.0F;
                    f12 = (float) (k >> 8 & 255) / 255.0F;
                    f6 = (float) (k & 255) / 255.0F;
                    GL11.glColor4f(f13, f12, f6, 1.0F);
                    this.renderManager.itemRenderer.renderItem(clientPlayer, itemstack1, j);
                }
            } else {
                j = itemstack1.getItem().getColorFromItemStack(itemstack1, 0);
                float f14 = (float) (j >> 16 & 255) / 255.0F;
                f13 = (float) (j >> 8 & 255) / 255.0F;
                f12 = (float) (j & 255) / 255.0F;
                GL11.glColor4f(f14, f13, f12, 1.0F);
                this.renderManager.itemRenderer.renderItem(clientPlayer, itemstack1, 0);
            }

            GL11.glPopMatrix();
        }
    }

    @Override
    public void renderEquippedItems(EntityLivingBase entityLivingBase, float par2) {
        renderSpecials((AbstractClientPlayer) entityLivingBase, par2);
    }
}
