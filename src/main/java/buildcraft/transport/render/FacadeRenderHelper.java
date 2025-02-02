/*
 * Copyright (c) SpaceToad, 2011-2012
 * http://www.mod-buildcraft.com
 * 
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.transport.render;

import dev.bagel.btb.mixin.accessors.RenderBlocksAccessor;
import buildcraft.BuildCraftTransport;
import buildcraft.core.CoreConstants;
import buildcraft.core.utils.MatrixTranformations;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.PipeIconProvider;
import buildcraft.transport.PipeRenderState;
import buildcraft.transport.TransportConstants;
import net.minecraft.src.Block;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Icon;
import net.minecraftforge.common.ForgeDirection;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public class FacadeRenderHelper {

	private static final float zFightOffset = 1F / 4096F;
	private static final float[][] zeroStateFacade = new float[3][2];
	private static final float[][] zeroStateSupport = new float[3][2];
	private static final float[] xOffsets = new float[6];
	private static final float[] yOffsets = new float[6];
	private static final float[] zOffsets = new float[6];

	static {

		// X START - END
		zeroStateFacade[0][0] = 0.0F;
		zeroStateFacade[0][1] = 1.0F;
		// Y START - END
		zeroStateFacade[1][0] = 0.0F;
		zeroStateFacade[1][1] = TransportConstants.FACADE_THICKNESS;
		// Z START - END
		zeroStateFacade[2][0] = 0.0F;
		zeroStateFacade[2][1] = 1.0F;

		// X START - END
		zeroStateSupport[0][0] = CoreConstants.PIPE_MIN_POS;
		zeroStateSupport[0][1] = CoreConstants.PIPE_MAX_POS;
		// Y START - END
		zeroStateSupport[1][0] = TransportConstants.FACADE_THICKNESS;
		zeroStateSupport[1][1] = CoreConstants.PIPE_MIN_POS;
		// Z START - END
		zeroStateSupport[2][0] = CoreConstants.PIPE_MIN_POS;
		zeroStateSupport[2][1] = CoreConstants.PIPE_MAX_POS;

		xOffsets[0] = zFightOffset;
		xOffsets[1] = zFightOffset;
		xOffsets[2] = 0;
		xOffsets[3] = 0;
		xOffsets[4] = 0;
		xOffsets[5] = 0;

		yOffsets[0] = 0;
		yOffsets[1] = 0;
		yOffsets[2] = zFightOffset;
		yOffsets[3] = zFightOffset;
		yOffsets[4] = 0;
		yOffsets[5] = 0;

		zOffsets[0] = zFightOffset;
		zOffsets[1] = zFightOffset;
		zOffsets[2] = 0;
		zOffsets[3] = 0;
		zOffsets[4] = 0;
		zOffsets[5] = 0;
	}

	private static void setRenderBounds(RenderBlocks renderblocks, float[][] rotated, ForgeDirection side) {
		renderblocks.setRenderBounds(
				rotated[0][0] + xOffsets[side.ordinal()],
				rotated[1][0] + yOffsets[side.ordinal()],
				rotated[2][0] + zOffsets[side.ordinal()],
				rotated[0][1] - xOffsets[side.ordinal()],
				rotated[1][1] - yOffsets[side.ordinal()],
				rotated[2][1] - zOffsets[side.ordinal()]);
	}

	public static void pipeFacadeRenderer(RenderBlocks renderblocks, BlockGenericPipe block, PipeRenderState state, int x, int y, int z) {
		state.textureArray = new Icon[6];

		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
			int facadeId = state.facadeMatrix.getFacadeBlockId(direction);
			if (facadeId != 0) {
				Block renderBlock = Block.blocksList[facadeId];
				int renderMeta = state.facadeMatrix.getFacadeMetaId(direction);

				for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
					state.textureArray[side.ordinal()] = renderBlock.getIcon(side.ordinal(), renderMeta);
					if (side == direction || side == direction.getOpposite())
						block.setRenderSide(side, true);
					else
						block.setRenderSide(side, state.facadeMatrix.getFacadeBlockId(side) == 0);
				}

				try {
					BlockGenericPipe.facadeRenderColor = Item.itemsList[state.facadeMatrix.getFacadeBlockId(direction)].getColorFromItemStack(new ItemStack(facadeId, 1, renderMeta), 0);
				} catch (Throwable error) {
				}

				if (renderBlock.getRenderType() == 31) {
					if ((renderMeta & 12) == 4) {
						((RenderBlocksAccessor) renderblocks).setUvRotateEast(1);
						((RenderBlocksAccessor) renderblocks).setUvRotateWest(1);
						((RenderBlocksAccessor) renderblocks).setUvRotateTop(1);
						((RenderBlocksAccessor) renderblocks).setUvRotateBottom(1);
					} else if ((renderMeta & 12) == 8) {
						((RenderBlocksAccessor) renderblocks).setUvRotateSouth(1);
						((RenderBlocksAccessor) renderblocks).setUvRotateNorth(1);
					}
				}

				// Hollow facade
				if (state.pipeConnectionMatrix.isConnected(direction)) {
					float[][] rotated = MatrixTranformations.deepClone(zeroStateFacade);
					rotated[0][0] = CoreConstants.PIPE_MIN_POS - zFightOffset * 4;
					rotated[0][1] = CoreConstants.PIPE_MAX_POS + zFightOffset * 4;
					rotated[2][0] = 0.0F;
					rotated[2][1] = CoreConstants.PIPE_MIN_POS - zFightOffset * 2;
					MatrixTranformations.transform(rotated, direction);
					setRenderBounds(renderblocks, rotated, direction);
					renderblocks.renderStandardBlock(block, x, y, z);

					rotated = MatrixTranformations.deepClone(zeroStateFacade);
					rotated[0][0] = CoreConstants.PIPE_MIN_POS - zFightOffset * 4;
					rotated[0][1] = CoreConstants.PIPE_MAX_POS + zFightOffset * 4;
					rotated[2][0] = CoreConstants.PIPE_MAX_POS + zFightOffset * 2;
					MatrixTranformations.transform(rotated, direction);
					setRenderBounds(renderblocks, rotated, direction);
					renderblocks.renderStandardBlock(block, x, y, z);

					rotated = MatrixTranformations.deepClone(zeroStateFacade);
					rotated[0][0] = 0.0F;
					rotated[0][1] = CoreConstants.PIPE_MIN_POS - zFightOffset * 2;
					MatrixTranformations.transform(rotated, direction);
					setRenderBounds(renderblocks, rotated, direction);
					renderblocks.renderStandardBlock(block, x, y, z);

					rotated = MatrixTranformations.deepClone(zeroStateFacade);
					rotated[0][0] = CoreConstants.PIPE_MAX_POS + zFightOffset * 2;
					rotated[0][1] = 1F;
					MatrixTranformations.transform(rotated, direction);
					setRenderBounds(renderblocks, rotated, direction);
					renderblocks.renderStandardBlock(block, x, y, z);
				} else { // Solid facade
					float[][] rotated = MatrixTranformations.deepClone(zeroStateFacade);
					MatrixTranformations.transform(rotated, direction);
					setRenderBounds(renderblocks, rotated, direction);
					renderblocks.renderStandardBlock(block, x, y, z);
				}

				if (renderBlock.getRenderType() == 31) {
					((RenderBlocksAccessor) renderblocks).setUvRotateSouth(0);
					((RenderBlocksAccessor) renderblocks).setUvRotateEast(0);
					((RenderBlocksAccessor) renderblocks).setUvRotateWest(0);
					((RenderBlocksAccessor) renderblocks).setUvRotateNorth(0);
					((RenderBlocksAccessor) renderblocks).setUvRotateTop(0);
					((RenderBlocksAccessor) renderblocks).setUvRotateBottom(0);
				}
			}

			BlockGenericPipe.facadeRenderColor = -1;
		}

		state.textureArray = null;
		block.setRenderAllSides();

		state.currentTexture = BuildCraftTransport.INSTANCE.pipeIconProvider.getIcon(PipeIconProvider.TYPE.PipeStructureCobblestone.ordinal()); // Structure Pipe

		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
			if (state.facadeMatrix.getFacadeBlockId(direction) != 0 && !state.pipeConnectionMatrix.isConnected(direction)) {
				float[][] rotated = MatrixTranformations.deepClone(zeroStateSupport);
				MatrixTranformations.transform(rotated, direction);

				renderblocks.setRenderBounds(rotated[0][0], rotated[1][0], rotated[2][0], rotated[0][1], rotated[1][1], rotated[2][1]);
				renderblocks.renderStandardBlock(block, x, y, z);
			}
		}
	}
}
