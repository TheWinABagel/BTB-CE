/*
 * Copyright (c) SpaceToad, 2011-2012
 * http://www.mod-buildcraft.com
 * 
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.api.gates;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.TileEntity;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public interface IGateExpansion {

	String getUniqueIdentifier();

	String getDisplayName();

	GateExpansionController makeController(TileEntity pipeTile);

	@Environment(EnvType.CLIENT)
	void registerBlockOverlay(IconRegister iconRegister);

	@Environment(EnvType.CLIENT)
	void registerItemOverlay(IconRegister iconRegister);

	Icon getOverlayBlock();

	Icon getOverlayItem();
}
