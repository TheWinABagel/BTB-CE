/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.builders.gui;

import buildcraft.api.filler.FillerManager;
import buildcraft.builders.TileFiller;
import buildcraft.core.DefaultProps;
import buildcraft.core.gui.GuiBuildCraft;
import buildcraft.core.gui.GuiTools;
import buildcraft.core.gui.buttons.GuiBetterButton;
import buildcraft.core.gui.buttons.StandardButtonTextureSets;
import buildcraft.core.network.PacketGuiReturn;
import buildcraft.core.utils.StringUtils;
import net.minecraft.src.GuiButton;
import net.minecraft.src.IInventory;
import net.minecraft.src.ResourceLocation;

public class GuiFiller extends GuiBuildCraft {

	private static final ResourceLocation TEXTURE = new ResourceLocation("buildcraft", DefaultProps.TEXTURE_PATH_GUI + "/filler.png");
	IInventory playerInventory;
	TileFiller filler;

	public GuiFiller(IInventory playerInventory, TileFiller filler) {
		super(new ContainerFiller(playerInventory, filler), filler, TEXTURE);
		this.playerInventory = playerInventory;
		this.filler = filler;
		xSize = 175;
		ySize = 240;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.clear();
		int w = (width - xSize) / 2;
		int h = (height - ySize) / 2;

		buttonList.add(new GuiBetterButton(0, w + 80 - 18, h + 30, 10, StandardButtonTextureSets.LEFT_BUTTON, ""));
		buttonList.add(new GuiBetterButton(1, w + 80 + 16 + 8, h + 30, 10, StandardButtonTextureSets.RIGHT_BUTTON, ""));
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);

		if (button.id == 0)
			filler.currentPattern = FillerManager.registry.getPreviousPattern(filler.currentPattern);
		if (button.id == 1)
			filler.currentPattern = FillerManager.registry.getNextPattern(filler.currentPattern);

		PacketGuiReturn pkt = new PacketGuiReturn(filler);
		pkt.sendPacket();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String title = StringUtils.localize("tile.fillerBlock");
		fontRenderer.drawString(title, getCenteredOffset(title), 6, 0x404040);
		fontRenderer.drawString(StringUtils.localize("gui.filling.resources"), 8, 74, 0x404040);
		fontRenderer.drawString(StringUtils.localize("gui.inventory"), 8, 142, 0x404040);
		GuiTools.drawCenteredString(fontRenderer, filler.currentPattern.getDisplayName(), 56);
	}
}
