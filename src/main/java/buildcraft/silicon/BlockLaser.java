/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.silicon;

import buildcraft.core.CreativeTabBuildCraft;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class BlockLaser extends BlockContainer {

	@Environment(EnvType.CLIENT)
	private Icon textureTop, textureBottom, textureSide;

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
	public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
		RenderingRegistry.instance().renderInventoryBlock(renderBlocks, this, iItemDamage, getRenderType());
	}

/*	@Override
	public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
		super.renderBlockAsItem(renderBlocks, iItemDamage, fBrightness);
	}*/

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
/*
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public void addCreativeItems(ArrayList itemList) {
		itemList.add(new ItemStack(this));
	}*/

	@Override
	@Environment(EnvType.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		textureTop = par1IconRegister.registerIcon("buildcraft:laser_top");
		textureBottom = par1IconRegister.registerIcon("buildcraft:laser_bottom");
		textureSide = par1IconRegister.registerIcon("buildcraft:laser_side");
	}
}
