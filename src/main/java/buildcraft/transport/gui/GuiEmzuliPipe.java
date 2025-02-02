/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.transport.gui;

import net.minecraft.src.IInventory;
import net.minecraft.src.ResourceLocation;


import buildcraft.core.DefaultProps;
import buildcraft.core.gui.GuiBuildCraft;
import buildcraft.core.utils.StringUtils;
import buildcraft.transport.pipes.PipeItemsEmzuli;

/**
 *
 * @author SandGrainOne
 */
public class GuiEmzuliPipe extends GuiBuildCraft {

	private static final ResourceLocation TEXTURE = new ResourceLocation("buildcraft", DefaultProps.TEXTURE_PATH_GUI + "/pipe_emzuli.png");
	IInventory filterInventory;
	PipeItemsEmzuli pipe;

	public GuiEmzuliPipe(IInventory playerInventory, PipeItemsEmzuli pipe) {
		super(new ContainerEmzuliPipe(playerInventory, pipe), pipe.getFilters(), TEXTURE);

		this.pipe = pipe;
		filterInventory = pipe.getFilters();

		xSize = 176;
		ySize = 166;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String title = StringUtils.localize("gui.pipes.emzuli.title");
		fontRenderer.drawString(title, (xSize - fontRenderer.getStringWidth(title)) / 2, 6, 0x404040);
		fontRenderer.drawString(StringUtils.localize("gui.inventory"), 8, ySize - 93, 0x404040);
	}
}
