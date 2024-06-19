/**
 * Copyright (c) 2011-2017, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 * <p/>
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL. Please check the contents
 * of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.properties;

import net.minecraft.src.Block;
import net.minecraft.src.BlockLiquid;
import net.minecraft.src.IBlockAccess;
import net.minecraftforge.fluids.BlockFluidBase;

public class WorldPropertyIsFluidSource extends WorldProperty {

    @Override
    public boolean get(IBlockAccess blockAccess, Block block, int meta, int x, int y, int z) {
        return (block instanceof BlockLiquid || block instanceof BlockFluidBase) && meta == 0;
    }
}
