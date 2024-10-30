/**
 * Copyright (c) SpaceToad, 2011
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package buildcraft.silicon;

import dev.bagel.btb.mixin.accessors.BlockAccessor;
import dev.bagel.btb.mixin.accessors.RenderBlocksAccessor;
import buildcraft.core.CoreConstants;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.src.Block;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class SiliconRenderBlock implements ISimpleBlockRenderingHandler {
	@Override
	public int getRenderId() {
		return SiliconProxy.laserBlockModel;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		return true;
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess iblockaccess, int x, int y, int z, Block block, int l, RenderBlocks renderblocks) {

		int meta = iblockaccess.getBlockMetadata(x, y, z);

		if (meta == ForgeDirection.EAST.ordinal()) {
			((RenderBlocksAccessor) renderblocks).setUvRotateEast(2);
			((RenderBlocksAccessor) renderblocks).setUvRotateWest(1);
			((RenderBlocksAccessor) renderblocks).setUvRotateTop(1);
			((RenderBlocksAccessor) renderblocks).setUvRotateBottom(2);

			((BlockAccessor) block).getFixedBlockBounds().setBounds(0.0F, 0.0F, 0.0F, 4F / 16F, 1, 1);
			renderblocks.setRenderBoundsFromBlock(block);
			renderblocks.renderStandardBlock(block, x, y, z);

			((BlockAccessor) block).getFixedBlockBounds().setBounds(4F / 16F, 5F / 16F, 5F / 16F, 13F / 16F, 11F / 16F, 11F / 16F);
			renderblocks.setRenderBoundsFromBlock(block);
			renderblocks.renderStandardBlock(block, x, y, z);
		} else if (meta == ForgeDirection.WEST.ordinal()) {
			((RenderBlocksAccessor) renderblocks).setUvRotateEast(1);
			((RenderBlocksAccessor) renderblocks).setUvRotateWest(2);
			((RenderBlocksAccessor) renderblocks).setUvRotateTop(2);
			((RenderBlocksAccessor) renderblocks).setUvRotateBottom(1);

			((BlockAccessor) block).getFixedBlockBounds().setBounds(1F - 4F / 16F, 0.0F, 0.0F, 1, 1, 1);
			renderblocks.setRenderBoundsFromBlock(block);
			renderblocks.renderStandardBlock(block, x, y, z);

			((BlockAccessor) block).getFixedBlockBounds().setBounds(1F - 13F / 16F, 5F / 16F, 5F / 16F, 1F - 4F / 16F, 11F / 16F, 11F / 16F);
			renderblocks.setRenderBoundsFromBlock(block);
			renderblocks.renderStandardBlock(block, x, y, z);
		} else if (meta == ForgeDirection.NORTH.ordinal()) {
			((RenderBlocksAccessor) renderblocks).setUvRotateSouth(1);
			((RenderBlocksAccessor) renderblocks).setUvRotateNorth(2);

			((BlockAccessor) block).getFixedBlockBounds().setBounds(0.0F, 0.0F, 1F - 4F / 16F, 1, 1, 1);
			renderblocks.setRenderBoundsFromBlock(block);
			renderblocks.renderStandardBlock(block, x, y, z);

			((BlockAccessor) block).getFixedBlockBounds().setBounds(5F / 16F, 5F / 16F, 1F - 13F / 16F, 11F / 16F, 11F / 16F, 1F - 4F / 16F);
			renderblocks.setRenderBoundsFromBlock(block);
			renderblocks.renderStandardBlock(block, x, y, z);
		} else if (meta == ForgeDirection.SOUTH.ordinal()) {
			((RenderBlocksAccessor) renderblocks).setUvRotateSouth(2);
			((RenderBlocksAccessor) renderblocks).setUvRotateNorth(1);
			((RenderBlocksAccessor) renderblocks).setUvRotateTop(3);
			((RenderBlocksAccessor) renderblocks).setUvRotateBottom(3);

			((BlockAccessor) block).getFixedBlockBounds().setBounds(0.0F, 0.0F, 0.0F, 1, 1, 4F / 16F);
			renderblocks.setRenderBoundsFromBlock(block);
			renderblocks.renderStandardBlock(block, x, y, z);

			((BlockAccessor) block).getFixedBlockBounds().setBounds(5F / 16F, 5F / 16F, 4F / 16F, 11F / 16F, 11F / 16F, 13F / 16F);
			renderblocks.setRenderBoundsFromBlock(block);
			renderblocks.renderStandardBlock(block, x, y, z);
		} else if (meta == ForgeDirection.DOWN.ordinal()) {
			((RenderBlocksAccessor) renderblocks).setUvRotateEast(3);
			((RenderBlocksAccessor) renderblocks).setUvRotateWest(3);
			((RenderBlocksAccessor) renderblocks).setUvRotateSouth(3);
			((RenderBlocksAccessor) renderblocks).setUvRotateNorth(3);

			((BlockAccessor) block).getFixedBlockBounds().setBounds(0.0F, 1.0F - 4F / 16F, 0.0F, 1.0F, 1.0F, 1.0F);
			renderblocks.setRenderBoundsFromBlock(block);
			renderblocks.renderStandardBlock(block, x, y, z);

			((BlockAccessor) block).getFixedBlockBounds().setBounds(5F / 16F, 1F - 13F / 16F, 5F / 16F, 11F / 16F, 1F - 4F / 16F, 11F / 16F);
			renderblocks.setRenderBoundsFromBlock(block);
			renderblocks.renderStandardBlock(block, x, y, z);
		} else if (meta == ForgeDirection.UP.ordinal()) {
			((BlockAccessor) block).getFixedBlockBounds().setBounds(0.0F, 0.0F, 0.0F, 1, 4F / 16F, 1);
			renderblocks.setRenderBoundsFromBlock(block);
			renderblocks.renderStandardBlock(block, x, y, z);

			((BlockAccessor) block).getFixedBlockBounds().setBounds(5F / 16F, 4F / 16F, 5F / 16F, 11F / 16F, 13F / 16F, 11F / 16F);
			renderblocks.setRenderBoundsFromBlock(block);
			renderblocks.renderStandardBlock(block, x, y, z);
		}

		((BlockAccessor) block).getFixedBlockBounds().setBounds(0.0F, 0.0F, 0.0F, 1, 1, 1);
		((RenderBlocksAccessor) renderblocks).setUvRotateEast(0);
		((RenderBlocksAccessor) renderblocks).setUvRotateWest(0);
		((RenderBlocksAccessor) renderblocks).setUvRotateSouth(0);
		((RenderBlocksAccessor) renderblocks).setUvRotateNorth(0);
		((RenderBlocksAccessor) renderblocks).setUvRotateTop(0);
		((RenderBlocksAccessor) renderblocks).setUvRotateBottom(0);

		return true;

	}

	@Override
	public void renderInventoryBlock(Block block, int i, int j, RenderBlocks renderblocks) {
		((BlockAccessor) block).getFixedBlockBounds().setBounds(CoreConstants.PIPE_MIN_POS, 0.0F, CoreConstants.PIPE_MIN_POS, CoreConstants.PIPE_MAX_POS, 1.0F, CoreConstants.PIPE_MAX_POS);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		((BlockAccessor) block).getFixedBlockBounds().setBounds(0.0F, 0.0F, 0.0F, 1, 4F / 16F, 1);
		renderblocks.setRenderBoundsFromBlock(block);
		renderBlockInInv(renderblocks, block, 0);

		((BlockAccessor) block).getFixedBlockBounds().setBounds(5F / 16F, 4F / 16F, 5F / 16F, 11F / 16F, 13F / 16F, 11F / 16F);
		renderblocks.setRenderBoundsFromBlock(block);
		renderBlockInInv(renderblocks, block, 1);

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		((BlockAccessor) block).getFixedBlockBounds().setBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	private void renderBlockInInv(RenderBlocks renderblocks, Block block, int i) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1F, 0.0F);
		renderblocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, i));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderblocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, i));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1F);
		renderblocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, i));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderblocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, i));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(-1F, 0.0F, 0.0F);
		renderblocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, i));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderblocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, i));
		tessellator.draw();
	}
}
