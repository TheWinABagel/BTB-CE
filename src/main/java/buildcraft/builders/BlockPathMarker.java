/**
 * Copyright (c) SpaceToad, 2011
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package buildcraft.builders;

import buildcraft.core.utils.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Icon;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

import java.util.ArrayList;

public class BlockPathMarker extends BlockMarker {

	private Icon activeMarker;

    public BlockPathMarker(int i) {
		super(i);
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TilePathMarker();
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		Utils.preDestroyBlock(world, x, y, z);
		super.breakBlock(world, x, y, z, par5, par6);
	}

	@SuppressWarnings({ "all" })
	// @Override (client only)
	public Icon getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		TilePathMarker marker = (TilePathMarker) iblockaccess.getBlockTileEntity(i, j, k);

		if (l == 1 || (marker != null && marker.tryingToConnect))
			return activeMarker;
		else
			return super.getBlockTexture(iblockaccess, i, j, k, l);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
	    blockIcon = par1IconRegister.registerIcon("buildcraft:blockPathMarker");
	    activeMarker = par1IconRegister.registerIcon("buildcraft:blockPathMarkerActive");
	}
}
