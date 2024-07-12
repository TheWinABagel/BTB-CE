/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package buildcraft.builders;

import buildcraft.BuildCraftCore;
import net.fabricmc.api.EnvType;
import net.minecraft.src.World;

public class EventHandlerBuilders {

	public static void handleWorldLoad(World world) {
		// Temporary solution
		// Please remove the world Load event when world Unload event gets implimented
		if (BuildCraftCore.INSTANCE.getEffectiveSide() == EnvType.SERVER) {
			TilePathMarker.clearAvailableMarkersList(world);
		}
	}

	public static void handleWorldUnload(World world) {
		// When a world unloads clean from the list of available markers the ones
		// that were on the unloaded world
		if (BuildCraftCore.INSTANCE.getEffectiveSide() == EnvType.SERVER) {
			TilePathMarker.clearAvailableMarkersList(world);
		}
	}

}
