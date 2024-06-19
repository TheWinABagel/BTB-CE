/**
 * Copyright (c) 2011-2017, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 * <p/>
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL. Please check the contents
 * of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core;

import buildcraft.core.lib.utils.ResourceUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.IconRegister;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Icon;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

public class BlockPathMarker extends BlockMarker {

    private Icon activeMarker;

    public BlockPathMarker() {}

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TilePathMarker();
    }

    @Override
    public Icon getIconAbsolute(IBlockAccess iblockaccess, int x, int y, int z, int side, int metadata) {
        TilePathMarker marker = (TilePathMarker) iblockaccess.getBlockTileEntity(x, y, z);

        if (side == 1 || (marker != null && marker.tryingToConnect)) {
            return activeMarker;
        } else {
            return super.getIconAbsolute(side, metadata);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void registerIcons(IconRegister par1IconRegister) {
        super.registerIcons(par1IconRegister);
        activeMarker = par1IconRegister
                .registerIcon(/*ResourceUtils.getObjectPrefix(Block.blockRegistry.getNameForObject(this))*/this.getTextureName() + "/active");
    }
}
