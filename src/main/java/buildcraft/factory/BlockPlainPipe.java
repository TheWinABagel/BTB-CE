/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.factory;

import buildcraft.core.CoreConstants;
import buildcraft.core.IFramePipeConnection;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import java.util.List;
import java.util.Random;

import net.minecraft.src.*;

public class BlockPlainPipe extends Block implements IFramePipeConnection {

	public BlockPlainPipe(int i) {
		super(i, Material.glass);

		initBlockBounds(CoreConstants.PIPE_MIN_POS, 0.0, CoreConstants.PIPE_MIN_POS, CoreConstants.PIPE_MAX_POS, 1.0, CoreConstants.PIPE_MAX_POS);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
		return true;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean isACube() {
		return false;
	}

	public int idDropped(int i, Random random) {
		return 0;
	}

	@Override
	public boolean isPipeConnected(IBlockAccess blockAccess, int x1, int y1, int z1, int x2, int y2, int z2) {
		return false;
	}

	public float getHeightInPipe() {
		return 0.5F;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubBlocks(int id, CreativeTabs tab, List list) {
		list.add(new ItemStack(this));
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon("buildcraft:blockPlainPipe");
	}

	@Override
	public String getLocalizedName() {
		return StatCollector.translateToLocal(this.getUnlocalizedName());
	}
}
