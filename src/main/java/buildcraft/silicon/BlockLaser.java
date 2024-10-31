/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.silicon;

import btw.block.model.BlockModel;
import cpw.mods.fml.client.registry.RenderingRegistry;
import dev.bagel.btb.injected.CustomBoundingBoxBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;
import net.minecraftforge.common.ForgeDirection;

import java.util.List;

public class BlockLaser extends BlockContainer implements CustomBoundingBoxBlock {

	@Environment(EnvType.CLIENT)
	private Icon textureTop, textureBottom, textureSide;

	public static final LaserModel model = new LaserModel();

	public BlockLaser(int i) {
		super(i, Material.iron);
		setHardness(10F);
		setCreativeTab(CreativeTabs.tabRedstone);
		setUnlocalizedName("laserBlock");
	}

	@Override
	public int getRenderType() {
		return SiliconProxy.laserBlockModel;
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

	@Override
	public boolean doesItemRenderAsBlock(int iItemDamage) {
		return true;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
		return true;
	}

	@Override
	public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
		RenderingRegistry.instance().renderInventoryBlock(renderBlocks, this, iItemDamage, getRenderType());
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileLaser();
	}

	@Override
	public Icon getIcon(int i, int j) {
		if (i == ForgeDirection.values()[j].getOpposite().ordinal())
			return textureBottom;
		else if (i == j)
			return textureTop;
		else
			return textureSide;

	}

	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float par6, float par7, float par8, int meta) {
		super.onBlockPlaced(world, x, y, z, side, par6, par7, par8, meta);

		if (side <= 6) {
			meta = side;
		}

		return meta;
	}

	@Override
	public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
		BlockModel modelCopy = model.makeTemporaryCopy();
		int iFacing = world.getBlockMetadata(i, j, k);
		modelCopy.tiltToFacingAlongY(iFacing);

		modelCopy.addIntersectingBoxesToCollisionList(world, i, j ,k, par5AxisAlignedBB, par6List);
	}

	@Override
	public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
		BlockModel modelCopy = model.makeTemporaryCopy();
		int iFacing = world.getBlockMetadata(i, j, k);
//		int iFacing = ForgeDirection.getOrientation(world.getBlockMetadata(i,j,k)).getOpposite().ordinal();

		modelCopy.rotateAroundYToFacing(iFacing);
		modelCopy.tiltToFacingAlongY(iFacing);
		return modelCopy.collisionRayTrace(world, i, j, k, startRay, endRay);
	}

/*
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public void addCreativeItems(ArrayList itemList) {
		itemList.add(new ItemStack(this));
	}*/

	@Override
	public List<AxisAlignedBB> getCustomSelectionBoxes(World world, int x, int y, int z) {
		return model.boxBase;
	}

//	@Override
//	@Environment(value=EnvType.CLIENT)
//	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
//		AxisAlignedBB transformedBox = model.boxBase.makeTemporaryCopy();
//		transformedBox.rotateAroundYToFacing(this.getFacing(world, i, j, k));
//		transformedBox.offset(i, j, k);
//		return transformedBox;
//	}

	@Override
	@Environment(EnvType.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		textureTop = par1IconRegister.registerIcon("buildcraft:laser_top");
		textureBottom = par1IconRegister.registerIcon("buildcraft:laser_bottom");
		textureSide = par1IconRegister.registerIcon("buildcraft:laser_side");
	}
}
