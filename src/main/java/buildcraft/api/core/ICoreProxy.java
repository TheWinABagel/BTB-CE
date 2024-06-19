/**
 * Copyright (c) 2011-2017, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 *
 * The BuildCraft API is distributed under the terms of the MIT License. Please check the contents of the license, which
 * should be located as "LICENSE.API" in the BuildCraft source code distribution.
 */
package buildcraft.api.core;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.WorldServer;

import java.lang.ref.WeakReference;

public interface ICoreProxy {

    WeakReference<EntityPlayer> getBuildCraftPlayer(WorldServer world);
}
