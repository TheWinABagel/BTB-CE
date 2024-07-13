package net.minecraftforge.fluids;

import dev.bagel.btb.mixin.accessors.RenderBlocksAccessor;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.src.Block;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.Icon;
import net.minecraft.src.MathHelper;
import net.minecraft.src.IBlockAccess;

public class RenderBlockFluid implements ISimpleBlockRenderingHandler {
   /**
    * The static instance of TileEntityRenderer
    */
   public static RenderBlockFluid instance = new RenderBlockFluid();
   static final float LIGHT_Y_NEG = 0.5F;
   static final float LIGHT_Y_POS = 1.0F;
   static final float LIGHT_XZ_NEG = 0.8F;
   static final float LIGHT_XZ_POS = 0.6F;
   static final double RENDER_OFFSET = 0.0010000000474974513D;

   public float getFluidHeightAverage(float[] flow) {
      float total = 0.0F;
      int count = 0;
      float end = 0.0F;

      for(int i = 0; i < flow.length; ++i) {
         if (flow[i] >= 0.875F && end != 1.0F) {
            end = flow[i];
         }

         if (flow[i] >= 0.0F) {
            total += flow[i];
            ++count;
         }
      }

      if (end == 0.0F) {
         end = total / (float)count;
      }

      return end;
   }

   public float getFluidHeightForRender(IBlockAccess world, int x, int y, int z, BlockFluidBase block) {
      if (world.getBlockId(x, y, z) == block.blockID) {
         if (world.getBlockMaterial(x, y - block.densityDir, z).isLiquid()) {
            return 1.0F;
         }

         if (world.getBlockMetadata(x, y, z) == block.getMaxRenderHeightMeta()) {
            return 0.875F;
         }
      }

      return !world.getBlockMaterial(x, y, z).isSolid() && world.getBlockId(x, y - block.densityDir, z) == block.blockID ? 1.0F : block.getQuantaPercentage(world, x, y, z) * 0.875F;
   }

   public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
   }

   public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
      if (!(block instanceof BlockFluidBase)) {
         return false;
      } else {
         Tessellator tessellator = Tessellator.instance;
         int color = block.colorMultiplier(world, x, y, z);
         float red = (float)(color >> 16 & 255) / 255.0F;
         float green = (float)(color >> 8 & 255) / 255.0F;
         float blue = (float)(color & 255) / 255.0F;
         BlockFluidBase theFluid = (BlockFluidBase)block;
         int bMeta = world.getBlockMetadata(x, y, z);
         boolean renderTop = world.getBlockId(x, y - theFluid.densityDir, z) != theFluid.blockID;
         boolean renderBottom = block.shouldSideBeRendered(world, x, y + theFluid.densityDir, z, 0) && world.getBlockId(x, y + theFluid.densityDir, z) != theFluid.blockID;
         boolean[] renderSides = new boolean[]{block.shouldSideBeRendered(world, x, y, z - 1, 2), block.shouldSideBeRendered(world, x, y, z + 1, 3), block.shouldSideBeRendered(world, x - 1, y, z, 4), block.shouldSideBeRendered(world, x + 1, y, z, 5)};
         if (!renderTop && !renderBottom && !renderSides[0] && !renderSides[1] && !renderSides[2] && !renderSides[3]) {
            return false;
         } else {
            boolean rendered = false;
            float flow11 = this.getFluidHeightForRender(world, x, y, z, theFluid);
            double heightNW;
            double heightSW;
            double heightSE;
            double heightNE;
            float flowDir;
            if (flow11 != 1.0F) {
               float flow00 = this.getFluidHeightForRender(world, x - 1, y, z - 1, theFluid);
               float flow01 = this.getFluidHeightForRender(world, x - 1, y, z, theFluid);
               flowDir = this.getFluidHeightForRender(world, x - 1, y, z + 1, theFluid);
               float flow10 = this.getFluidHeightForRender(world, x, y, z - 1, theFluid);
               float flow12 = this.getFluidHeightForRender(world, x, y, z + 1, theFluid);
               float flow20 = this.getFluidHeightForRender(world, x + 1, y, z - 1, theFluid);
               float flow21 = this.getFluidHeightForRender(world, x + 1, y, z, theFluid);
               float flow22 = this.getFluidHeightForRender(world, x + 1, y, z + 1, theFluid);
               heightNW = (double)this.getFluidHeightAverage(new float[]{flow00, flow01, flow10, flow11});
               heightSW = (double)this.getFluidHeightAverage(new float[]{flow01, flowDir, flow12, flow11});
               heightSE = (double)this.getFluidHeightAverage(new float[]{flow12, flow21, flow22, flow11});
               heightNE = (double)this.getFluidHeightAverage(new float[]{flow10, flow20, flow21, flow11});
            } else {
               heightNW = (double)flow11;
               heightSW = (double)flow11;
               heightSE = (double)flow11;
               heightNE = (double)flow11;
            }

            boolean rises = theFluid.densityDir == 1;
            double ty2;
            double tx2;
            double tz1;
            double tz2;
            float v1Flow;
            float v2Flow;
            double ty1;
            double tx1;
            if (((RenderBlocksAccessor) renderer).isRenderAllFaces() || renderTop) {
               rendered = true;
               Icon iconStill = block.getIcon(1, bMeta);
               flowDir = (float)BlockFluidBase.getFlowDirection(world, x, y, z);
               if (flowDir > -999.0F) {
                  iconStill = block.getIcon(2, bMeta);
               }

               heightNW -= 0.0010000000474974513D;
               heightSW -= 0.0010000000474974513D;
               heightSE -= 0.0010000000474974513D;
               heightNE -= 0.0010000000474974513D;
               double v4;
               double u1;
               if (flowDir < -999.0F) {
                  ty1 = (double)iconStill.getInterpolatedU(0.0D);
                  tz1 = (double)iconStill.getInterpolatedV(0.0D);
                  u1 = ty1;
                  tx2 = (double)iconStill.getInterpolatedV(16.0D);
                  ty2 = (double)iconStill.getInterpolatedU(16.0D);
                  v4 = tx2;
                  tx1 = ty2;
                  tz2 = tz1;
               } else {
                  v1Flow = MathHelper.sin(flowDir) * 0.25F;
                  v2Flow = MathHelper.cos(flowDir) * 0.25F;
                  ty1 = (double)iconStill.getInterpolatedU((double)(8.0F + (-v2Flow - v1Flow) * 16.0F));
                  tz1 = (double)iconStill.getInterpolatedV((double)(8.0F + (-v2Flow + v1Flow) * 16.0F));
                  u1 = (double)iconStill.getInterpolatedU((double)(8.0F + (-v2Flow + v1Flow) * 16.0F));
                  tx2 = (double)iconStill.getInterpolatedV((double)(8.0F + (v2Flow + v1Flow) * 16.0F));
                  ty2 = (double)iconStill.getInterpolatedU((double)(8.0F + (v2Flow + v1Flow) * 16.0F));
                  v4 = (double)iconStill.getInterpolatedV((double)(8.0F + (v2Flow - v1Flow) * 16.0F));
                  tx1 = (double)iconStill.getInterpolatedU((double)(8.0F + (v2Flow - v1Flow) * 16.0F));
                  tz2 = (double)iconStill.getInterpolatedV((double)(8.0F + (-v2Flow - v1Flow) * 16.0F));
               }

               tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
               tessellator.setColorOpaque_F(1.0F * red, 1.0F * green, 1.0F * blue);
               if (!rises) {
                  tessellator.addVertexWithUV((double)(x + 0), (double)y + heightNW, (double)(z + 0), ty1, tz1);
                  tessellator.addVertexWithUV((double)(x + 0), (double)y + heightSW, (double)(z + 1), u1, tx2);
                  tessellator.addVertexWithUV((double)(x + 1), (double)y + heightSE, (double)(z + 1), ty2, v4);
                  tessellator.addVertexWithUV((double)(x + 1), (double)y + heightNE, (double)(z + 0), tx1, tz2);
               } else {
                  tessellator.addVertexWithUV((double)(x + 1), (double)(y + 1) - heightNE, (double)(z + 0), tx1, tz2);
                  tessellator.addVertexWithUV((double)(x + 1), (double)(y + 1) - heightSE, (double)(z + 1), ty2, v4);
                  tessellator.addVertexWithUV((double)(x + 0), (double)(y + 1) - heightSW, (double)(z + 1), u1, tx2);
                  tessellator.addVertexWithUV((double)(x + 0), (double)(y + 1) - heightNW, (double)(z + 0), ty1, tz1);
               }
            }

            if (((RenderBlocksAccessor) renderer).isRenderAllFaces() || renderBottom) {
               rendered = true;
               tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y - 1, z));
               if (!rises) {
                  tessellator.setColorOpaque_F(0.5F * red, 0.5F * green, 0.5F * blue);
                  renderer.renderFaceYNeg(block, (double)x, (double)y + 0.0010000000474974513D, (double)z, block.getIcon(0, bMeta));
               } else {
                  tessellator.setColorOpaque_F(1.0F * red, 1.0F * green, 1.0F * blue);
                  renderer.renderFaceYPos(block, (double)x, (double)y + 0.0010000000474974513D, (double)z, block.getIcon(1, bMeta));
               }
            }

            for(int side = 0; side < 4; ++side) {
               int x2 = x;
               int z2 = z;
               switch(side) {
               case 0:
                  z2 = z - 1;
                  break;
               case 1:
                  z2 = z + 1;
                  break;
               case 2:
                  x2 = x - 1;
                  break;
               case 3:
                  x2 = x + 1;
               }

               Icon iconFlow = block.getIcon(side + 2, bMeta);
               if (((RenderBlocksAccessor) renderer).isRenderAllFaces() || renderSides[side]) {
                  rendered = true;
                  if (side == 0) {
                     ty1 = heightNW;
                     ty2 = heightNE;
                     tx1 = (double)x;
                     tx2 = (double)(x + 1);
                     tz1 = (double)z + 0.0010000000474974513D;
                     tz2 = (double)z + 0.0010000000474974513D;
                  } else if (side == 1) {
                     ty1 = heightSE;
                     ty2 = heightSW;
                     tx1 = (double)(x + 1);
                     tx2 = (double)x;
                     tz1 = (double)(z + 1) - 0.0010000000474974513D;
                     tz2 = (double)(z + 1) - 0.0010000000474974513D;
                  } else if (side == 2) {
                     ty1 = heightSW;
                     ty2 = heightNW;
                     tx1 = (double)x + 0.0010000000474974513D;
                     tx2 = (double)x + 0.0010000000474974513D;
                     tz1 = (double)(z + 1);
                     tz2 = (double)z;
                  } else {
                     ty1 = heightNE;
                     ty2 = heightSE;
                     tx1 = (double)(x + 1) - 0.0010000000474974513D;
                     tx2 = (double)(x + 1) - 0.0010000000474974513D;
                     tz1 = (double)z;
                     tz2 = (double)(z + 1);
                  }

                  float u1Flow = iconFlow.getInterpolatedU(0.0D);
                  float u2Flow = iconFlow.getInterpolatedU(8.0D);
                  v1Flow = iconFlow.getInterpolatedV((1.0D - ty1) * 16.0D * 0.5D);
                  v2Flow = iconFlow.getInterpolatedV((1.0D - ty2) * 16.0D * 0.5D);
                  float v3Flow = iconFlow.getInterpolatedV(8.0D);
                  tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x2, y, z2));
                  float sideLighting = 1.0F;
                  if (side < 2) {
                     sideLighting = 0.8F;
                  } else {
                     sideLighting = 0.6F;
                  }

                  tessellator.setColorOpaque_F(1.0F * sideLighting * red, 1.0F * sideLighting * green, 1.0F * sideLighting * blue);
                  if (!rises) {
                     tessellator.addVertexWithUV(tx1, (double)y + ty1, tz1, (double)u1Flow, (double)v1Flow);
                     tessellator.addVertexWithUV(tx2, (double)y + ty2, tz2, (double)u2Flow, (double)v2Flow);
                     tessellator.addVertexWithUV(tx2, (double)(y + 0), tz2, (double)u2Flow, (double)v3Flow);
                     tessellator.addVertexWithUV(tx1, (double)(y + 0), tz1, (double)u1Flow, (double)v3Flow);
                  } else {
                     tessellator.addVertexWithUV(tx1, (double)(y + 1 - 0), tz1, (double)u1Flow, (double)v3Flow);
                     tessellator.addVertexWithUV(tx2, (double)(y + 1 - 0), tz2, (double)u2Flow, (double)v3Flow);
                     tessellator.addVertexWithUV(tx2, (double)(y + 1) - ty2, tz2, (double)u2Flow, (double)v2Flow);
                     tessellator.addVertexWithUV(tx1, (double)(y + 1) - ty1, tz1, (double)u1Flow, (double)v1Flow);
                  }
               }
            }

            ((RenderBlocksAccessor) renderer).setRenderMinY(0.0D);
            ((RenderBlocksAccessor) renderer).setRenderMaxY(1.0D);
            return rendered;
         }
      }
   }

   public boolean shouldRender3DInInventory() {
      return false;
   }

   public int getRenderId() {
      return FluidRegistry.renderIdFluid;
   }
}
