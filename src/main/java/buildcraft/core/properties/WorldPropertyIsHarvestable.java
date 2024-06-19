/**
 * Copyright (c) 2011-2017, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 * <p/>
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL. Please check the contents
 * of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.properties;

import buildcraft.api.crops.CropManager;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;

public class WorldPropertyIsHarvestable extends WorldProperty {

    @Override
    public boolean get(IBlockAccess blockAccess, Block block, int meta, int x, int y, int z) {
        return CropManager.isMature(blockAccess, block, meta, x, y, z);
    }
}
