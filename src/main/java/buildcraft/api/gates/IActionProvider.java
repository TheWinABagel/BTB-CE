/** 
 * Copyright (c) SpaceToad, 2011
 * http://www.mod-buildcraft.com
 * 
 * BuildCraft is distributed under the terms of the Minecraft Mod Public 
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package buildcraft.api.gates;

import net.minecraft.src.Block;
import net.minecraft.src.TileEntity;

import java.util.LinkedList;

public interface IActionProvider {

	/**
	 * Returns the list of actions available to a gate next to the given block.
	 */
	public abstract LinkedList<IAction> getNeighborActions(Block block, TileEntity tile);

}
