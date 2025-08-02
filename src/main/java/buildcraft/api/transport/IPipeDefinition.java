/*
 * Copyright (c) SpaceToad, 2011-2012
 * http://www.mod-buildcraft.com
 * 
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.api.transport;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.TileEntity;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public interface IPipeDefinition {

	String getUniqueTag();

	@Environment(EnvType.CLIENT)
	void registerIcons(IconRegister iconRegister);

	Icon getIcon(int index);

	Icon getItemIcon();

	PipeBehavior makePipeBehavior(TileEntity tile);
}
