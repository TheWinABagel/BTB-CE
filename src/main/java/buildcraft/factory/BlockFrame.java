/**
 * Copyright (c) SpaceToad, 2011
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package buildcraft.factory;

import btw.community.example.mixin.accessors.BlockAccessor;
import buildcraft.BuildCraftCore;
import buildcraft.core.CoreConstants;
import buildcraft.core.IFramePipeConnection;
import buildcraft.core.utils.Utils;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;

import java.util.List;
import java.util.Random;

public class BlockFrame extends Block implements IFramePipeConnection {

	public BlockFrame(int i) {
		super(i, Material.glass);
		setHardness(0.5F);
		setTickRandomly(true);
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {
		if (world.isRemote)
			return;

		int meta = world.getBlockMetadata(i, j, k);
		if (meta == 1 && random.nextInt(10) > 5) {
			world.setBlock(i, j, k, 0);
		}
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean doesItemRenderAsBlock(int iItemDamage) {
		return true;
	}

	@Override
	public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
		RenderingRegistry.instance().renderInventoryBlock(renderBlocks, this, iItemDamage, getRenderType());
	}

	@Override
	public int idDropped(int i, Random random, int j) {
		return -1;
	}

	@Override
	public int getRenderType() {
		return BuildCraftCore.legacyPipeModel;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		float xMin = CoreConstants.PIPE_MIN_POS, xMax = CoreConstants.PIPE_MAX_POS, yMin = CoreConstants.PIPE_MIN_POS, yMax = CoreConstants.PIPE_MAX_POS, zMin = CoreConstants.PIPE_MIN_POS, zMax = CoreConstants.PIPE_MAX_POS;

		if (Utils.checkLegacyPipesConnections(world, i, j, k, i - 1, j, k)) {
			xMin = 0.0F;
		}

		if (Utils.checkLegacyPipesConnections(world, i, j, k, i + 1, j, k)) {
			xMax = 1.0F;
		}

		if (Utils.checkLegacyPipesConnections(world, i, j, k, i, j - 1, k)) {
			yMin = 0.0F;
		}

		if (Utils.checkLegacyPipesConnections(world, i, j, k, i, j + 1, k)) {
			yMax = 1.0F;
		}

		if (Utils.checkLegacyPipesConnections(world, i, j, k, i, j, k - 1)) {
			zMin = 0.0F;
		}

		if (Utils.checkLegacyPipesConnections(world, i, j, k, i, j, k + 1)) {
			zMax = 1.0F;
		}

		return AxisAlignedBB.getBoundingBox((double) i + xMin, (double) j + yMin, (double) k + zMin, (double) i + xMax, (double) j + yMax, (double) k + zMax);
	}

	@SuppressWarnings({ "all" })
	// @Override (client only)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
		return getCollisionBoundingBoxFromPool(world, i, j, k);
	}

	@Override
	public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity) {
		((BlockAccessor) this).getFixedBlockBounds().setBounds(CoreConstants.PIPE_MIN_POS, CoreConstants.PIPE_MIN_POS, CoreConstants.PIPE_MIN_POS, CoreConstants.PIPE_MAX_POS, CoreConstants.PIPE_MAX_POS, CoreConstants.PIPE_MAX_POS);
		super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);

		if (Utils.checkLegacyPipesConnections(world, i, j, k, i - 1, j, k)) {
			((BlockAccessor) this).getFixedBlockBounds().setBounds(0.0F, CoreConstants.PIPE_MIN_POS, CoreConstants.PIPE_MIN_POS, CoreConstants.PIPE_MAX_POS, CoreConstants.PIPE_MAX_POS, CoreConstants.PIPE_MAX_POS);
			super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
		}

		if (Utils.checkLegacyPipesConnections(world, i, j, k, i + 1, j, k)) {
			((BlockAccessor) this).getFixedBlockBounds().setBounds(CoreConstants.PIPE_MIN_POS, CoreConstants.PIPE_MIN_POS, CoreConstants.PIPE_MIN_POS, 1.0F, CoreConstants.PIPE_MAX_POS, CoreConstants.PIPE_MAX_POS);
			super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
		}

		if (Utils.checkLegacyPipesConnections(world, i, j, k, i, j - 1, k)) {
			((BlockAccessor) this).getFixedBlockBounds().setBounds(CoreConstants.PIPE_MIN_POS, 0.0F, CoreConstants.PIPE_MIN_POS, CoreConstants.PIPE_MAX_POS, CoreConstants.PIPE_MAX_POS, CoreConstants.PIPE_MAX_POS);
			super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
		}

		if (Utils.checkLegacyPipesConnections(world, i, j, k, i, j + 1, k)) {
			((BlockAccessor) this).getFixedBlockBounds().setBounds(CoreConstants.PIPE_MIN_POS, CoreConstants.PIPE_MIN_POS, CoreConstants.PIPE_MIN_POS, CoreConstants.PIPE_MAX_POS, 1.0F, CoreConstants.PIPE_MAX_POS);
			super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
		}

		if (Utils.checkLegacyPipesConnections(world, i, j, k, i, j, k - 1)) {
			((BlockAccessor) this).getFixedBlockBounds().setBounds(CoreConstants.PIPE_MIN_POS, CoreConstants.PIPE_MIN_POS, 0.0F, CoreConstants.PIPE_MAX_POS, CoreConstants.PIPE_MAX_POS, CoreConstants.PIPE_MAX_POS);
			super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
		}

		if (Utils.checkLegacyPipesConnections(world, i, j, k, i, j, k + 1)) {
			((BlockAccessor) this).getFixedBlockBounds().setBounds(CoreConstants.PIPE_MIN_POS, CoreConstants.PIPE_MIN_POS, CoreConstants.PIPE_MIN_POS, CoreConstants.PIPE_MAX_POS, CoreConstants.PIPE_MAX_POS, 1.0F);
			super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
		}

		((BlockAccessor) this).getFixedBlockBounds().setBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 vec3d, Vec3 vec3d1) {
		float xMin = CoreConstants.PIPE_MIN_POS, xMax = CoreConstants.PIPE_MAX_POS, yMin = CoreConstants.PIPE_MIN_POS, yMax = CoreConstants.PIPE_MAX_POS, zMin = CoreConstants.PIPE_MIN_POS, zMax = CoreConstants.PIPE_MAX_POS;

		if (Utils.checkLegacyPipesConnections(world, i, j, k, i - 1, j, k)) {
			xMin = 0.0F;
		}

		if (Utils.checkLegacyPipesConnections(world, i, j, k, i + 1, j, k)) {
			xMax = 1.0F;
		}

		if (Utils.checkLegacyPipesConnections(world, i, j, k, i, j - 1, k)) {
			yMin = 0.0F;
		}

		if (Utils.checkLegacyPipesConnections(world, i, j, k, i, j + 1, k)) {
			yMax = 1.0F;
		}

		if (Utils.checkLegacyPipesConnections(world, i, j, k, i, j, k - 1)) {
			zMin = 0.0F;
		}

		if (Utils.checkLegacyPipesConnections(world, i, j, k, i, j, k + 1)) {
			zMax = 1.0F;
		}

		((BlockAccessor) this).getFixedBlockBounds().setBounds(xMin, yMin, zMin, xMax, yMax, zMax);

		MovingObjectPosition r = super.collisionRayTrace(world, i, j, k, vec3d, vec3d1);

		((BlockAccessor) this).getFixedBlockBounds().setBounds(0, 0, 0, 1, 1, 1);

		return r;
	}

	@Override
	public boolean isPipeConnected(IBlockAccess blockAccess, int x1, int y1, int z1, int x2, int y2, int z2) {
		return blockAccess.getBlockId(x2, y2, z2) == blockID;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public void getSubBlocks(int id, CreativeTabs tab, List list) {
		list.add(new ItemStack(this));
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
	    blockIcon = par1IconRegister.registerIcon("buildcraft:blockFrame");
	}
}
