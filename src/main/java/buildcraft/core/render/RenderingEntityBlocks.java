package buildcraft.core.render;

import dev.bagel.btb.mixin.accessors.BlockAccessor;
import buildcraft.BuildCraftCore;
import buildcraft.core.CoreConstants;
import buildcraft.core.IInventoryRenderer;
import buildcraft.core.utils.Utils;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import java.util.HashMap;
import net.minecraft.src.Block;
import net.minecraft.src.Minecraft;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TextureMap;
import net.minecraft.src.ResourceLocation;
import net.minecraft.src.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderingEntityBlocks implements ISimpleBlockRenderingHandler {

	private static final ResourceLocation BLOCK_TEXTURE = TextureMap.locationBlocksTexture;

	public static class EntityRenderIndex {
		Block block;
		int damage;

		public EntityRenderIndex(Block block, int damage) {
			this.block = block;
			this.damage = damage;
		}

		@Override
		public int hashCode() {
			return block.hashCode() + damage;
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof EntityRenderIndex idx))
				return false;

            return idx.block == block && idx.damage == damage;
		}
	}
	public static HashMap<EntityRenderIndex, IInventoryRenderer> blockByEntityRenders = new HashMap<EntityRenderIndex, IInventoryRenderer>();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {

		if (block.getRenderType() == BuildCraftCore.blockByEntityModel) {

			EntityRenderIndex index = new EntityRenderIndex(block, metadata);
			if (blockByEntityRenders.containsKey(index)) {
				blockByEntityRenders.get(index).inventoryRender(-0.5, -0.5, -0.5, 0, 0);
			}

		} else if (block.getRenderType() == BuildCraftCore.legacyPipeModel) {
			Tessellator tessellator = Tessellator.instance;

			((BlockAccessor) block).getFixedBlockBounds().setBounds(CoreConstants.PIPE_MIN_POS, 0.0F, CoreConstants.PIPE_MIN_POS, CoreConstants.PIPE_MAX_POS, 1.0F, CoreConstants.PIPE_MAX_POS);
			renderer.setRenderBoundsFromBlock(block);
			block.setBlockBoundsForItemRender();
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, -1F, 0.0F);
			renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, metadata));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, metadata));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, -1F);
			renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, metadata));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, metadata));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1F, 0.0F, 0.0F);
			renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, metadata));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, metadata));
			tessellator.draw();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			((BlockAccessor) block).getFixedBlockBounds().setBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

		if (block.getRenderType() == BuildCraftCore.blockByEntityModel) {
			// renderblocks.renderStandardBlock(block, i, j, k);
		} else if (block.getRenderType() == BuildCraftCore.legacyPipeModel) {
			Minecraft.getMinecraft().renderEngine.bindTexture(BLOCK_TEXTURE);
			legacyPipeRender(renderer, world, x, y, z, block, modelId);

		}

		return true;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		return true;
	}

	@Override
	public int getRenderId() {
		return BuildCraftCore.blockByEntityModel;
	}

	/* LEGACY PIPE RENDERING and quarry frames! */
	private void legacyPipeRender(RenderBlocks renderblocks, IBlockAccess iblockaccess, int i, int j, int k, Block block, int l) {
		float minSize = CoreConstants.PIPE_MIN_POS;
		float maxSize = CoreConstants.PIPE_MAX_POS;

		((BlockAccessor) block).getFixedBlockBounds().setBounds(minSize, minSize, minSize, maxSize, maxSize, maxSize);
		renderblocks.setRenderBoundsFromBlock(block);
		renderblocks.renderStandardBlock(block, i, j, k);

		if (Utils.checkLegacyPipesConnections(iblockaccess, i, j, k, i - 1, j, k)) {
			((BlockAccessor) block).getFixedBlockBounds().setBounds(0.0F, minSize, minSize, minSize, maxSize, maxSize);
			renderblocks.setRenderBoundsFromBlock(block);
			renderblocks.renderStandardBlock(block, i, j, k);
		}

		if (Utils.checkLegacyPipesConnections(iblockaccess, i, j, k, i + 1, j, k)) {
			((BlockAccessor) block).getFixedBlockBounds().setBounds(maxSize, minSize, minSize, 1.0F, maxSize, maxSize);
			renderblocks.setRenderBoundsFromBlock(block);
			renderblocks.renderStandardBlock(block, i, j, k);
		}

		if (Utils.checkLegacyPipesConnections(iblockaccess, i, j, k, i, j - 1, k)) {
			((BlockAccessor) block).getFixedBlockBounds().setBounds(minSize, 0.0F, minSize, maxSize, minSize, maxSize);
			renderblocks.setRenderBoundsFromBlock(block);
			renderblocks.renderStandardBlock(block, i, j, k);
		}

		if (Utils.checkLegacyPipesConnections(iblockaccess, i, j, k, i, j + 1, k)) {
			((BlockAccessor) block).getFixedBlockBounds().setBounds(minSize, maxSize, minSize, maxSize, 1.0F, maxSize);
			renderblocks.setRenderBoundsFromBlock(block);
			renderblocks.renderStandardBlock(block, i, j, k);
		}

		if (Utils.checkLegacyPipesConnections(iblockaccess, i, j, k, i, j, k - 1)) {
			((BlockAccessor) block).getFixedBlockBounds().setBounds(minSize, minSize, 0.0F, maxSize, maxSize, minSize);
			renderblocks.setRenderBoundsFromBlock(block);
			renderblocks.renderStandardBlock(block, i, j, k);
		}

		if (Utils.checkLegacyPipesConnections(iblockaccess, i, j, k, i, j, k + 1)) {
			((BlockAccessor) block).getFixedBlockBounds().setBounds(minSize, minSize, maxSize, maxSize, maxSize, 1.0F);
			renderblocks.setRenderBoundsFromBlock(block);
			renderblocks.renderStandardBlock(block, i, j, k);
		}

		((BlockAccessor) block).getFixedBlockBounds().setBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
}
