/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.factory;

import buildcraft.BuildCraftCore;
import buildcraft.core.CreativeTabBuildCraft;
import buildcraft.core.inventory.InvUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import java.util.ArrayList;

import net.minecraft.src.*;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class BlockTank extends BlockContainer {

	private Icon textureStackedSide;
	private Icon textureBottomSide;
	private Icon textureTop;

	public BlockTank(int i) {
		super(i, Material.glass);
		initBlockBounds(0.125F, 0F, 0.125F, 0.875F, 1F, 0.875F);
		setHardness(0.5F);
		setCreativeTab(CreativeTabs.tabRedstone);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	public boolean isACube() {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileTank();
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Icon getIcon(int par1, int par2) {
		switch (par1) {
			case 0:
			case 1:
				return textureTop;
			default:
				return textureBottomSide;
		}
	}

	@SuppressWarnings({"all"})
	@Override
	public Icon getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		switch (l) {
			case 0:
			case 1:
				return textureTop;
			default:
				if (iblockaccess.getBlockId(i, j - 1, k) == blockID)
					return textureStackedSide;
				else
					return textureBottomSide;
		}
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {

		ItemStack current = entityplayer.inventory.getCurrentItem();
		if (current != null) {

			FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(current);

			TileTank tank = (TileTank) world.getBlockTileEntity(i, j, k);

			// Handle filled containers
			if (liquid != null) {
				int qty = tank.fill(ForgeDirection.UNKNOWN, liquid, true);

				if (qty != 0 && !BuildCraftCore.debugMode && !entityplayer.capabilities.isCreativeMode) {
					entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, InvUtils.consumeItem(current));
				}

				return true;

				// Handle empty containers
			} else {

				FluidStack available = tank.getTankInfo(ForgeDirection.UNKNOWN)[0].fluid;
				if (available != null) {
					ItemStack filled = FluidContainerRegistry.fillFluidContainer(available, current);

					liquid = FluidContainerRegistry.getFluidForFilledItem(filled);

					if (liquid != null) {
						if (!BuildCraftCore.debugMode && !entityplayer.capabilities.isCreativeMode) {
							if (current.stackSize > 1) {
								if (!entityplayer.inventory.addItemStackToInventory(filled))
									return false;
								else {
									entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, InvUtils.consumeItem(current));
								}
							} else {
								entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, InvUtils.consumeItem(current));
								entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, filled);
							}
						}
						tank.drain(ForgeDirection.UNKNOWN, liquid.amount, true);
						return true;
					}
				}
			}
		}

		return false;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		if (side <= 1)
			return world.getBlockId(x, y, z) != blockID;
		return super.shouldSideBeRendered(world, x, y, z, side);
	}

/*	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public void addCreativeItems(ArrayList itemList) {
		itemList.add(new ItemStack(this));
	}*/

	@Override
	@Environment(EnvType.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		textureStackedSide = par1IconRegister.registerIcon("buildcraft:tank_stacked_side");
		textureBottomSide = par1IconRegister.registerIcon("buildcraft:tank_bottom_side");
		textureTop = par1IconRegister.registerIcon("buildcraft:tank_top");
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if (tile instanceof TileTank) {
			TileTank tank = (TileTank) tile;
			return tank.getFluidLightLevel();
		}
		return super.getLightValue(world, x, y, z);
	}
}
