/**
 * Copyright (c) 2011-2017, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 * <p/>
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL. Please check the contents
 * of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.internal;

import net.minecraft.src.IBlockAccess;

public interface IFramePipeConnection {

    boolean isPipeConnected(IBlockAccess blockAccess, int x1, int y1, int z1, int x2, int y2, int z2);
}
