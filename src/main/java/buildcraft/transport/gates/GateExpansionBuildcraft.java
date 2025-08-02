/*
 * Copyright (c) SpaceToad, 2011-2012
 * http://www.mod-buildcraft.com
 * 
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.transport.gates;

import buildcraft.api.gates.IGateExpansion;
import buildcraft.core.utils.StringUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public abstract class GateExpansionBuildcraft implements IGateExpansion {

	private final String tag;
	private Icon iconBlock;
	private Icon iconItem;

	public GateExpansionBuildcraft(String tag) {
		this.tag = tag;
	}

	@Override
	public String getUniqueIdentifier() {
		return "buildcraft:" + tag;
	}

	@Override
	public String getDisplayName() {
		return StringUtils.localize("gate.expansion." + tag);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void registerBlockOverlay(IconRegister iconRegister) {
		iconBlock = iconRegister.registerIcon("buildcraft:gates/gate_expansion_" + tag);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void registerItemOverlay(IconRegister iconRegister) {
		iconItem = iconRegister.registerIcon("buildcraft:gates/gate_expansion_" + tag);
	}

	@Override
	public Icon getOverlayBlock() {
		return iconBlock;
	}

	@Override
	public Icon getOverlayItem() {
		return iconItem;
	}
}
