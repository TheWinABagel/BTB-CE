/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.transport.render;

import buildcraft.BuildCraftCore;
import buildcraft.BuildCraftCore.RenderMode;
import buildcraft.BuildCraftTransport;
import buildcraft.api.gates.IGateExpansion;
import buildcraft.core.CoreConstants;
import buildcraft.core.render.RenderEntityBlock;
import buildcraft.core.render.RenderEntityBlock.RenderInfo;
import buildcraft.core.utils.EnumColor;
import buildcraft.core.render.RenderUtils;
import buildcraft.core.utils.MatrixTranformations;
import buildcraft.transport.Pipe;
import buildcraft.api.transport.PipeWire;
import buildcraft.transport.PipeIconProvider;
import buildcraft.transport.PipeRenderState;
import buildcraft.transport.PipeTransportFluids;
import buildcraft.transport.PipeTransportItems;
import buildcraft.transport.PipeTransportPower;
import buildcraft.transport.TileGenericPipe;
import buildcraft.transport.TravelingItem;

import com.google.common.collect.Maps;

import java.util.HashMap;

import net.minecraft.src.Block;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.RenderItem;
import net.minecraft.src.RenderManager;
import net.minecraft.src.TextureMap;
import net.minecraft.src.TileEntitySpecialRenderer;
import net.minecraft.src.EntityItem;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Icon;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

public class PipeRendererTESR extends TileEntitySpecialRenderer {

	final static private int LIQUID_STAGES = 40;
	final static private int MAX_ITEMS_TO_RENDER = 10;
	private final EntityItem dummyEntityItem = new EntityItem(null);
	private final RenderItem customRenderItem;

	private class DisplayFluidList {

		public int[] sideHorizontal = new int[LIQUID_STAGES];
		public int[] sideVertical = new int[LIQUID_STAGES];
		public int[] centerHorizontal = new int[LIQUID_STAGES];
		public int[] centerVertical = new int[LIQUID_STAGES];
	}
	private final HashMap<Integer, DisplayFluidList> displayFluidLists = Maps.newHashMap();
	private final int[] angleY = {0, 0, 270, 90, 0, 180};
	private final int[] angleZ = {90, 270, 0, 0, 0, 0};
	final static private int POWER_STAGES = 100;
	public int[] displayPowerList = new int[POWER_STAGES];
	public int[] displayPowerListOverload = new int[POWER_STAGES];

	public PipeRendererTESR() {
		customRenderItem = new RenderItem() { //todotransport med custom render item
/*			@Override
			public boolean shouldBob() {
				return false;
			}

			@Override
			public boolean shouldSpreadItems() {
				return false;
			}*/
		};
		customRenderItem.setRenderManager(RenderManager.instance);
	}

	private DisplayFluidList getDisplayFluidLists(int liquidId, World world) {
		if (displayFluidLists.containsKey(liquidId)) {
			return displayFluidLists.get(liquidId);
		}

		DisplayFluidList d = new DisplayFluidList();
		displayFluidLists.put(liquidId, d);

		RenderInfo block = new RenderInfo();

		Fluid fluid = FluidRegistry.getFluid(liquidId);
		if (fluid.getBlockID() > 0) {
			block.baseBlock = Block.blocksList[fluid.getBlockID()];
		} else {
			block.baseBlock = Block.waterStill;
		}
		block.texture = fluid.getStillIcon();

		float size = CoreConstants.PIPE_MAX_POS - CoreConstants.PIPE_MIN_POS;

		// render size

		for (int s = 0; s < LIQUID_STAGES; ++s) {
			float ratio = (float) s / (float) LIQUID_STAGES;

			// SIDE HORIZONTAL

			d.sideHorizontal[s] = GLAllocation.generateDisplayLists(1);
			GL11.glNewList(d.sideHorizontal[s], 4864 /* GL_COMPILE */);

			block.minX = 0.0F;
			block.minZ = CoreConstants.PIPE_MIN_POS + 0.01F;

			block.maxX = block.minX + size / 2F + 0.01F;
			block.maxZ = block.minZ + size - 0.02F;

			block.minY = CoreConstants.PIPE_MIN_POS + 0.01F;
			block.maxY = block.minY + (size - 0.02F) * ratio;

			RenderEntityBlock.INSTANCE.renderBlock(block, world, 0, 0, 0, false, true);

			GL11.glEndList();

			// SIDE VERTICAL

			d.sideVertical[s] = GLAllocation.generateDisplayLists(1);
			GL11.glNewList(d.sideVertical[s], 4864 /* GL_COMPILE */);

			block.minY = CoreConstants.PIPE_MAX_POS - 0.01;
			block.maxY = 1;

			block.minX = 0.5 - (size / 2 - 0.01) * ratio;
			block.maxX = 0.5 + (size / 2 - 0.01) * ratio;

			block.minZ = 0.5 - (size / 2 - 0.01) * ratio;
			block.maxZ = 0.5 + (size / 2 - 0.01) * ratio;

			RenderEntityBlock.INSTANCE.renderBlock(block, world, 0, 0, 0, false, true);

			GL11.glEndList();

			// CENTER HORIZONTAL

			d.centerHorizontal[s] = GLAllocation.generateDisplayLists(1);
			GL11.glNewList(d.centerHorizontal[s], 4864 /* GL_COMPILE */);

			block.minX = CoreConstants.PIPE_MIN_POS + 0.01;
			block.minZ = CoreConstants.PIPE_MIN_POS + 0.01;

			block.maxX = block.minX + size - 0.02;
			block.maxZ = block.minZ + size - 0.02;

			block.minY = CoreConstants.PIPE_MIN_POS + 0.01;
			block.maxY = block.minY + (size - 0.02F) * ratio;

			RenderEntityBlock.INSTANCE.renderBlock(block, world, 0, 0, 0, false, true);

			GL11.glEndList();

			// CENTER VERTICAL

			d.centerVertical[s] = GLAllocation.generateDisplayLists(1);
			GL11.glNewList(d.centerVertical[s], 4864 /* GL_COMPILE */);

			block.minY = CoreConstants.PIPE_MIN_POS + 0.01;
			block.maxY = CoreConstants.PIPE_MAX_POS - 0.01;

			block.minX = 0.5 - (size / 2 - 0.02) * ratio;
			block.maxX = 0.5 + (size / 2 - 0.02) * ratio;

			block.minZ = 0.5 - (size / 2 - 0.02) * ratio;
			block.maxZ = 0.5 + (size / 2 - 0.02) * ratio;

			RenderEntityBlock.INSTANCE.renderBlock(block, world, 0, 0, 0, false, true);

			GL11.glEndList();

		}

		return d;
	}
	boolean initialized = false;

	private void initializeDisplayPowerList(World world) {
		if (initialized)
			return;

		initialized = true;

		RenderInfo block = new RenderInfo();
		block.texture = BuildCraftTransport.INSTANCE.pipeIconProvider.getIcon(PipeIconProvider.TYPE.Power_Normal.ordinal());

		float size = CoreConstants.PIPE_MAX_POS - CoreConstants.PIPE_MIN_POS;

		for (int s = 0; s < POWER_STAGES; ++s) {
			displayPowerList[s] = GLAllocation.generateDisplayLists(1);
			GL11.glNewList(displayPowerList[s], 4864 /* GL_COMPILE */);

			float minSize = 0.005F;

			float unit = (size - minSize) / 2F / POWER_STAGES;

			block.minY = 0.5 - (minSize / 2F) - unit * s;
			block.maxY = 0.5 + (minSize / 2F) + unit * s;

			block.minZ = 0.5 - (minSize / 2F) - unit * s;
			block.maxZ = 0.5 + (minSize / 2F) + unit * s;

			block.minX = 0;
			block.maxX = 0.5 + (minSize / 2F) + unit * s;

			RenderEntityBlock.INSTANCE.renderBlock(block, world, 0, 0, 0, false, true);

			GL11.glEndList();
		}

		block.texture = BuildCraftTransport.INSTANCE.pipeIconProvider.getIcon(PipeIconProvider.TYPE.Power_Overload.ordinal());

		size = CoreConstants.PIPE_MAX_POS - CoreConstants.PIPE_MIN_POS;

		for (int s = 0; s < POWER_STAGES; ++s) {
			displayPowerListOverload[s] = GLAllocation.generateDisplayLists(1);
			GL11.glNewList(displayPowerListOverload[s], 4864 /* GL_COMPILE */);

			float minSize = 0.005F;

			float unit = (size - minSize) / 2F / POWER_STAGES;

			block.minY = 0.5 - (minSize / 2F) - unit * s;
			block.maxY = 0.5 + (minSize / 2F) + unit * s;

			block.minZ = 0.5 - (minSize / 2F) - unit * s;
			block.maxZ = 0.5 + (minSize / 2F) + unit * s;

			block.minX = 0;
			block.maxX = 0.5 + (minSize / 2F) + unit * s;

			RenderEntityBlock.INSTANCE.renderBlock(block, world, 0, 0, 0, false, true);

			GL11.glEndList();
		}
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {

		if (BuildCraftCore.render == RenderMode.NoDynamic)
			return;

		TileGenericPipe pipe = (TileGenericPipe) tileentity;

		if (pipe.pipe == null)
			return;

		renderGatesWires(pipe, x, y, z);

		switch (pipe.getPipeType()) {
			case ITEM:
				renderSolids(pipe.pipe, x, y, z);
				break;
			case FLUID:
				renderFluids(pipe.pipe, x, y, z);
				break;
			case POWER:
				renderPower(pipe.pipe, x, y, z);
				break;
			case STRUCTURE:
				// no object to render in a structure pipe;
				break;
		}
	}

	private void renderGatesWires(TileGenericPipe pipe, double x, double y, double z) {
		PipeRenderState state = pipe.renderState;

		if (state.wireMatrix.hasWire(PipeWire.RED)) {
			pipeWireRender(pipe, CoreConstants.PIPE_MIN_POS, CoreConstants.PIPE_MAX_POS, CoreConstants.PIPE_MIN_POS, PipeWire.RED, x, y, z);
		}

		if (state.wireMatrix.hasWire(PipeWire.BLUE)) {
			pipeWireRender(pipe, CoreConstants.PIPE_MAX_POS, CoreConstants.PIPE_MAX_POS, CoreConstants.PIPE_MAX_POS, PipeWire.BLUE, x, y, z);
		}

		if (state.wireMatrix.hasWire(PipeWire.GREEN)) {
			pipeWireRender(pipe, CoreConstants.PIPE_MAX_POS, CoreConstants.PIPE_MIN_POS, CoreConstants.PIPE_MIN_POS, PipeWire.GREEN, x, y, z);
		}

		if (state.wireMatrix.hasWire(PipeWire.YELLOW)) {
			pipeWireRender(pipe, CoreConstants.PIPE_MIN_POS, CoreConstants.PIPE_MIN_POS, CoreConstants.PIPE_MAX_POS, PipeWire.YELLOW, x, y, z);
		}

		if (pipe.pipe.gate != null) {
			pipeGateRender(pipe, x, y, z);
		}
	}

	private void pipeWireRender(TileGenericPipe pipe, float cx, float cy, float cz, PipeWire color, double x, double y, double z) {

		PipeRenderState state = pipe.renderState;

		float minX = CoreConstants.PIPE_MIN_POS;
		float minY = CoreConstants.PIPE_MIN_POS;
		float minZ = CoreConstants.PIPE_MIN_POS;

		float maxX = CoreConstants.PIPE_MAX_POS;
		float maxY = CoreConstants.PIPE_MAX_POS;
		float maxZ = CoreConstants.PIPE_MAX_POS;

		boolean foundX = false, foundY = false, foundZ = false;

		if (state.wireMatrix.isWireConnected(color, ForgeDirection.WEST)) {
			minX = 0;
			foundX = true;
		}

		if (state.wireMatrix.isWireConnected(color, ForgeDirection.EAST)) {
			maxX = 1;
			foundX = true;
		}

		if (state.wireMatrix.isWireConnected(color, ForgeDirection.DOWN)) {
			minY = 0;
			foundY = true;
		}

		if (state.wireMatrix.isWireConnected(color, ForgeDirection.UP)) {
			maxY = 1;
			foundY = true;
		}

		if (state.wireMatrix.isWireConnected(color, ForgeDirection.NORTH)) {
			minZ = 0;
			foundZ = true;
		}

		if (state.wireMatrix.isWireConnected(color, ForgeDirection.SOUTH)) {
			maxZ = 1;
			foundZ = true;
		}

		boolean center = false;

		if (minX == 0 && maxX != 1 && (foundY || foundZ))
			if (cx == CoreConstants.PIPE_MIN_POS) {
				maxX = CoreConstants.PIPE_MIN_POS;
			} else {
				center = true;
			}

		if (minX != 0 && maxX == 1 && (foundY || foundZ))
			if (cx == CoreConstants.PIPE_MAX_POS) {
				minX = CoreConstants.PIPE_MAX_POS;
			} else {
				center = true;
			}

		if (minY == 0 && maxY != 1 && (foundX || foundZ))
			if (cy == CoreConstants.PIPE_MIN_POS) {
				maxY = CoreConstants.PIPE_MIN_POS;
			} else {
				center = true;
			}

		if (minY != 0 && maxY == 1 && (foundX || foundZ))
			if (cy == CoreConstants.PIPE_MAX_POS) {
				minY = CoreConstants.PIPE_MAX_POS;
			} else {
				center = true;
			}

		if (minZ == 0 && maxZ != 1 && (foundX || foundY))
			if (cz == CoreConstants.PIPE_MIN_POS) {
				maxZ = CoreConstants.PIPE_MIN_POS;
			} else {
				center = true;
			}

		if (minZ != 0 && maxZ == 1 && (foundX || foundY))
			if (cz == CoreConstants.PIPE_MAX_POS) {
				minZ = CoreConstants.PIPE_MAX_POS;
			} else {
				center = true;
			}

		boolean found = foundX || foundY || foundZ;

		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_CULL_FACE);
		RenderHelper.disableStandardItemLighting();

		GL11.glColor3f(1, 1, 1);
		GL11.glTranslatef((float) x, (float) y, (float) z);

		float scale = 1.001f;
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		GL11.glScalef(scale, scale, scale);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);


		bindTexture(TextureMap.locationBlocksTexture);

		RenderInfo box = new RenderInfo();
		box.texture = BuildCraftTransport.INSTANCE.wireIconProvider.getIcon(state.wireMatrix.getWireIconIndex(color));

		// Z render

		if (minZ != CoreConstants.PIPE_MIN_POS || maxZ != CoreConstants.PIPE_MAX_POS || !found) {
			box.setBounds(cx == CoreConstants.PIPE_MIN_POS ? cx - 0.05F : cx, cy == CoreConstants.PIPE_MIN_POS ? cy - 0.05F : cy, minZ, cx == CoreConstants.PIPE_MIN_POS ? cx
					: cx + 0.05F, cy == CoreConstants.PIPE_MIN_POS ? cy : cy + 0.05F, maxZ);
			RenderEntityBlock.INSTANCE.renderBlock(box, pipe.worldObj, 0, 0, 0, pipe.xCoord, pipe.yCoord, pipe.zCoord, true, true);
		}

		// X render

		if (minX != CoreConstants.PIPE_MIN_POS || maxX != CoreConstants.PIPE_MAX_POS || !found) {
			box.setBounds(minX, cy == CoreConstants.PIPE_MIN_POS ? cy - 0.05F : cy, cz == CoreConstants.PIPE_MIN_POS ? cz - 0.05F : cz, maxX, cy == CoreConstants.PIPE_MIN_POS ? cy
					: cy + 0.05F, cz == CoreConstants.PIPE_MIN_POS ? cz : cz + 0.05F);
			RenderEntityBlock.INSTANCE.renderBlock(box, pipe.worldObj, 0, 0, 0, pipe.xCoord, pipe.yCoord, pipe.zCoord, true, true);
		}

		// Y render

		if (minY != CoreConstants.PIPE_MIN_POS || maxY != CoreConstants.PIPE_MAX_POS || !found) {
			box.setBounds(cx == CoreConstants.PIPE_MIN_POS ? cx - 0.05F : cx, minY, cz == CoreConstants.PIPE_MIN_POS ? cz - 0.05F : cz, cx == CoreConstants.PIPE_MIN_POS ? cx
					: cx + 0.05F, maxY, cz == CoreConstants.PIPE_MIN_POS ? cz : cz + 0.05F);
			RenderEntityBlock.INSTANCE.renderBlock(box, pipe.worldObj, 0, 0, 0, pipe.xCoord, pipe.yCoord, pipe.zCoord, true, true);
		}

		if (center || !found) {
			box.setBounds(cx == CoreConstants.PIPE_MIN_POS ? cx - 0.05F : cx, cy == CoreConstants.PIPE_MIN_POS ? cy - 0.05F : cy, cz == CoreConstants.PIPE_MIN_POS ? cz - 0.05F : cz,
					cx == CoreConstants.PIPE_MIN_POS ? cx : cx + 0.05F, cy == CoreConstants.PIPE_MIN_POS ? cy : cy + 0.05F, cz == CoreConstants.PIPE_MIN_POS ? cz : cz + 0.05F);
			RenderEntityBlock.INSTANCE.renderBlock(box, pipe.worldObj, 0, 0, 0, pipe.xCoord, pipe.yCoord, pipe.zCoord, true, true);
		}

		RenderHelper.enableStandardItemLighting();

		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}

	private void pipeGateRender(TileGenericPipe pipe, double x, double y, double z) {
		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
//		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_CULL_FACE);
//		GL11.glDisable(GL11.GL_TEXTURE_2D);
		RenderHelper.disableStandardItemLighting();

		GL11.glColor3f(1, 1, 1);
		GL11.glTranslatef((float) x, (float) y, (float) z);

		bindTexture(TextureMap.locationBlocksTexture);

		Icon iconLogic;
		if (pipe.renderState.isGateLit())
			iconLogic = pipe.pipe.gate.logic.getIconLit();
		else
			iconLogic = pipe.pipe.gate.logic.getIconDark();

		float translateCenter = 0; 
				
		// Render base gate		
		renderGate(pipe, iconLogic, 0, 0.1F, 0, 0);
		
		float pulseStage = pipe.pipe.gate.getPulseStage() * 2F;
		
		if (pipe.renderState.isGatePulsing() || pulseStage != 0) {
			// Render pulsing gate
			float amplitude = 0.10F;
			float start = 0.01F;			
		
			if (pulseStage < 1) {
				translateCenter = (pulseStage * amplitude) + start;
			} else {
				translateCenter = amplitude - ((pulseStage - 1F) * amplitude) + start;
			}
		
			renderGate(pipe, iconLogic, 0, 0.13F, translateCenter, translateCenter);
		}

		Icon materialIcon = pipe.pipe.gate.material.getIconBlock();
		if (materialIcon != null)
			renderGate(pipe, materialIcon, 1, 0.13F, translateCenter, translateCenter);

		for (IGateExpansion expansion : pipe.pipe.gate.expansions.keySet()) {
			renderGate(pipe, expansion.getOverlayBlock(), 2, 0.13F, translateCenter, translateCenter);
		}		
		
		RenderHelper.enableStandardItemLighting();

		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}

	private void renderGate(TileGenericPipe tile, Icon icon, int layer, float trim, float translateCenter, float extraDepth) {
		PipeRenderState state = tile.renderState;

		RenderInfo box = new RenderInfo();
		box.texture = icon;

		float[][] zeroState = new float[3][2];
		float min = CoreConstants.PIPE_MIN_POS + trim / 2F;
		float max = CoreConstants.PIPE_MAX_POS - trim / 2F;

		// X START - END
		zeroState[0][0] = min;
		zeroState[0][1] = max;
		// Y START - END
		zeroState[1][0] = CoreConstants.PIPE_MIN_POS - 0.10F - 0.001F * layer;
		zeroState[1][1] = CoreConstants.PIPE_MIN_POS + 0.001F + 0.01F * layer + extraDepth;
		// Z START - END
		zeroState[2][0] = min;
		zeroState[2][1] = max;
		
		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
			if (shouldRenderNormalPipeSide(state, direction)) {
				GL11.glPushMatrix();
				
				float xt = direction.offsetX * translateCenter, 
						yt = direction.offsetY * translateCenter, 
						zt = direction.offsetZ * translateCenter;
								
				GL11.glTranslatef(xt, yt, zt);					
				
				float[][] rotated = MatrixTranformations.deepClone(zeroState);
				MatrixTranformations.transform(rotated, direction);

				if (layer != 0)
					box.setRenderSingleSide(direction.ordinal());
				box.setBounds(rotated[0][0], rotated[1][0], rotated[2][0], rotated[0][1], rotated[1][1], rotated[2][1]);
				RenderEntityBlock.INSTANCE.renderBlock(box, tile.worldObj, 0, 0, 0, tile.xCoord, tile.yCoord, tile.zCoord, true, true);
				
				GL11.glPopMatrix();
			}
		}				
	}
		
	private boolean shouldRenderNormalPipeSide(PipeRenderState state, ForgeDirection direction) {
		return !state.pipeConnectionMatrix.isConnected(direction) && state.facadeMatrix.getFacadeBlockId(direction) == 0 && !state.plugMatrix.isConnected(direction) && !isOpenOrientation(state, direction);
	}

	public boolean isOpenOrientation(PipeRenderState state, ForgeDirection direction) {
		int connections = 0;

		ForgeDirection targetOrientation = ForgeDirection.UNKNOWN;

		for (ForgeDirection o : ForgeDirection.VALID_DIRECTIONS) {
			if (state.pipeConnectionMatrix.isConnected(o)) {

				connections++;

				if (connections == 1)
					targetOrientation = o;
			}
		}

		if (connections > 1 || connections == 0)
			return false;

		return targetOrientation.getOpposite() == direction;
	}

	private void renderPower(Pipe<PipeTransportPower> pipe, double x, double y, double z) {
		initializeDisplayPowerList(pipe.container.worldObj);

		PipeTransportPower pow = pipe.transport;

		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glDisable(GL11.GL_LIGHTING);
//		GL11.glEnable(GL11.GL_BLEND);

		GL11.glTranslatef((float) x, (float) y, (float) z);

		bindTexture(TextureMap.locationBlocksTexture);

		int[] displayList = pow.overload > 0 ? displayPowerListOverload : displayPowerList;

		for (int side = 0; side < 6; ++side) {
			GL11.glPushMatrix();

			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			GL11.glRotatef(angleY[side], 0, 1, 0);
			GL11.glRotatef(angleZ[side], 0, 0, 1);
			float scale = 1.0F - side * 0.0001F;
			GL11.glScalef(scale, scale, scale);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

			short stage = pow.clientDisplayPower[side];
			if (stage >= 1) {
				if (stage < displayList.length) {
					GL11.glCallList(displayList[stage]);
				} else {
					GL11.glCallList(displayList[displayList.length - 1]);
				}
			}

			GL11.glPopMatrix();
		}

		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}

	private void renderFluids(Pipe<PipeTransportFluids> pipe, double x, double y, double z) {
		PipeTransportFluids trans = pipe.transport;

		boolean needsRender = false;
		for (int i = 0; i < 7; ++i) {
			FluidStack fluidStack = trans.renderCache[i];
			if (fluidStack != null && fluidStack.amount > 0) {
				needsRender = true;
				break;
			}
		}

		if (!needsRender)
			return;

		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glTranslatef((float) x, (float) y, (float) z);

		// sides

		boolean sides = false, above = false;

		for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
			int i = side.ordinal();

			FluidStack fluidStack = trans.renderCache[i];

			if (fluidStack == null || fluidStack.amount <= 0)
				continue;

			if (!pipe.container.isPipeConnected(side))
				continue;

			DisplayFluidList d = getListFromBuffer(fluidStack, pipe.container.worldObj);

			if (d == null)
				continue;

			int stage = (int) ((float) fluidStack.amount / (float) (trans.getCapacity()) * (LIQUID_STAGES - 1));

			GL11.glPushMatrix();
			int list = 0;

			switch (ForgeDirection.VALID_DIRECTIONS[i]) {
				case UP:
					above = true;
					list = d.sideVertical[stage];
					break;
				case DOWN:
					GL11.glTranslatef(0, -0.75F, 0);
					list = d.sideVertical[stage];
					break;
				case EAST:
				case WEST:
				case SOUTH:
				case NORTH:
					sides = true;
					// Yes, this is kind of ugly, but was easier than transform the coordinates above.
					GL11.glTranslatef(0.5F, 0.0F, 0.5F);
					GL11.glRotatef(angleY[i], 0, 1, 0);
					GL11.glRotatef(angleZ[i], 0, 0, 1);
					GL11.glTranslatef(-0.5F, 0.0F, -0.5F);
					list = d.sideHorizontal[stage];
					break;
				default:
			}
			bindTexture(TextureMap.locationBlocksTexture);
			RenderUtils.setGLColorFromInt(trans.colorRenderCache[i]);
			GL11.glCallList(list);
			GL11.glPopMatrix();
		}
		// CENTER
		FluidStack fluidStack = trans.renderCache[ForgeDirection.UNKNOWN.ordinal()];

		if (fluidStack != null && fluidStack.amount > 0) {
			DisplayFluidList d = getListFromBuffer(fluidStack, pipe.container.worldObj);

			if (d != null) {
				int stage = (int) ((float) fluidStack.amount / (float) (trans.getCapacity()) * (LIQUID_STAGES - 1));

				bindTexture(TextureMap.locationBlocksTexture);
				RenderUtils.setGLColorFromInt(trans.colorRenderCache[ForgeDirection.UNKNOWN.ordinal()]);

				if (above) {
					GL11.glCallList(d.centerVertical[stage]);
				}

				if (!above || sides) {
					GL11.glCallList(d.centerHorizontal[stage]);
				}
			}

		}

		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}

	private DisplayFluidList getListFromBuffer(FluidStack stack, World world) {

		int liquidId = stack.fluidID;

		if (liquidId == 0)
			return null;

		return getDisplayFluidLists(liquidId, world);
	}

	private void renderSolids(Pipe<PipeTransportItems> pipe, double x, double y, double z) {
		GL11.glPushMatrix();
		GL11.glDisable(2896 /* GL_LIGHTING */);

		float light = pipe.container.worldObj.getLightBrightness(pipe.container.xCoord, pipe.container.yCoord, pipe.container.zCoord);

		int count = 0;
		for (TravelingItem item : pipe.transport.items) {
			if (count >= MAX_ITEMS_TO_RENDER) {
				break;
			}

			doRenderItem(item, x + item.xCoord - pipe.container.xCoord, y + item.yCoord - pipe.container.yCoord, z + item.zCoord - pipe.container.zCoord, light, item.color);
			count++;
		}

		GL11.glEnable(2896 /* GL_LIGHTING */);
		GL11.glPopMatrix();
	}

	public void doRenderItem(TravelingItem travellingItem, double x, double y, double z, float light, EnumColor color) {

		if (travellingItem == null || travellingItem.getItemStack() == null)
			return;

		float renderScale = 0.7f;
		ItemStack itemstack = travellingItem.getItemStack();
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x, (float) y, (float) z);
		GL11.glTranslatef(0, 0.25F, 0);
		GL11.glScalef(renderScale, renderScale, renderScale);
		dummyEntityItem.setEntityItemStack(itemstack);
		customRenderItem.doRenderItem(dummyEntityItem, 0, 0, 0, 0, 0);
		if (color != null) {
			bindTexture(TextureMap.locationBlocksTexture);
			RenderInfo block = new RenderInfo();

			block.texture = PipeIconProvider.TYPE.ItemBox.getIcon();

			float pix = 0.0625F;

			float min = -4 * pix;
			float max = 4 * pix;

			block.minY = min;
			block.maxY = max;

			block.minZ = min;
			block.maxZ = max;

			block.minX = min;
			block.maxX = max;

			RenderUtils.setGLColorFromInt(color.getLightHex());
			RenderEntityBlock.INSTANCE.renderBlock(block, null, 0, 0, 0, false, true);
		}
		GL11.glPopMatrix();
	}
}
