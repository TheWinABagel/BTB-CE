/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.transport.gui;

import buildcraft.core.DefaultProps;
import buildcraft.core.gui.GuiBuildCraft;
import buildcraft.core.utils.StringUtils;
import buildcraft.transport.TileFilteredBuffer;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author SandGrainOne
 */
public class GuiFilteredBuffer extends GuiBuildCraft {

	private static final ResourceLocation TEXTURE = new ResourceLocation("buildcraft", DefaultProps.TEXTURE_PATH_GUI + "/filteredBuffer_gui.png");
	IInventory playerInventory;
	TileFilteredBuffer filteredBuffer;

	public GuiFilteredBuffer(InventoryPlayer playerInventory, TileFilteredBuffer filteredBuffer) {
		super(new ContainerFilteredBuffer(playerInventory, filteredBuffer), playerInventory, TEXTURE);

		this.playerInventory = playerInventory;
		this.filteredBuffer = filteredBuffer;
		xSize = 175;
		ySize = 169;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		mc.renderEngine.bindTexture(TEXTURE);
		int cornerX = (width - xSize) / 2;
		int cornerY = (height - ySize) / 2;

		drawTexturedModalRect(cornerX, cornerY, 0, 0, xSize, ySize);

		IInventory filters = filteredBuffer.getFilters();

		for (int col = 0; col < filters.getSizeInventory(); col++) {
			if (filters.getStackInSlot(col) == null) {
				drawTexturedModalRect(cornerX + 7 + col * 18, cornerY + 60, 176, 0, 18, 18);
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String title = StringUtils.localize("tile.filteredBufferBlock");
		int xPos = (xSize - fontRenderer.getStringWidth(title)) / 2;
		fontRenderer.drawString(title, xPos, 10, 0x404040);
	}
}
