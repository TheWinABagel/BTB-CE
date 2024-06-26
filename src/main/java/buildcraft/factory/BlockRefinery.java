/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.factory;

import btw.community.example.injected.EntityPlayerExtension;
import buildcraft.BuildCraftCore;
import buildcraft.BuildCraftFactory;
import buildcraft.api.tools.IToolWrench;
import buildcraft.core.CreativeTabBuildCraft;
import buildcraft.core.GuiIds;
import buildcraft.core.fluids.FluidUtils;
import buildcraft.core.utils.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import java.util.ArrayList;

import net.minecraft.src.*;
import net.minecraftforge.common.ForgeDirection;
import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.WEST;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class BlockRefinery extends BlockContainer {

	private static Icon icon;

	public BlockRefinery(int i) {
		super(i, Material.iron);

		setHardness(5F);
		setCreativeTab(CreativeTabs.tabRedstone);
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
	public int getRenderType() {
		return BuildCraftCore.blockByEntityModel;
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileRefinery();
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLivingBase entityliving, ItemStack stack) {
		super.onBlockPlacedBy(world, i, j, k, entityliving, stack);

		ForgeDirection orientation = Utils.get2dOrientation(entityliving);

		world.setBlockMetadataWithNotify(i, j, k, orientation.getOpposite().ordinal(), 1);
	}

	//todofactory rotate
//	@Override
	public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection axis) {
		int meta = world.getBlockMetadata(x, y, z);

		switch (ForgeDirection.getOrientation(meta)) {
			case WEST:
				world.setBlockMetadataWithNotify(x, y, z, ForgeDirection.SOUTH.ordinal(), 3);
				break;
			case EAST:
				world.setBlockMetadataWithNotify(x, y, z, ForgeDirection.NORTH.ordinal(), 3);
				break;
			case NORTH:
				world.setBlockMetadataWithNotify(x, y, z, ForgeDirection.WEST.ordinal(), 3);
				break;
			case SOUTH:
			default:
				world.setBlockMetadataWithNotify(x, y, z, ForgeDirection.EAST.ordinal(), 3);
				break;
		}
		world.markBlockForUpdate(x, y, z);
		return true;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		TileEntity tile = world.getBlockTileEntity(x, y, z);

		if (!(tile instanceof TileRefinery))
			return false;

		ItemStack current = player.getCurrentEquippedItem();
		Item equipped = current != null ? current.getItem() : null;
		if (player.isSneaking() && equipped instanceof IToolWrench && ((IToolWrench) equipped).canWrench(player, x, y, z)) {
			((TileRefinery)tile).resetFilters();
			((IToolWrench) equipped).wrenchUsed(player, x, y, z);
			return true;
		}

		if (current != null && current.itemID != Item.bucketEmpty.itemID) {
			if (!world.isRemote) {
				if (FluidUtils.handleRightClick((TileRefinery) tile, ForgeDirection.getOrientation(side), player, true, false))
					return true;
			} else if (FluidContainerRegistry.isContainer(current)) {
				return true;
			}
		}

		if (!world.isRemote) {
			((EntityPlayerExtension) player).openGui(BuildCraftFactory.instance.getModId(), GuiIds.REFINERY, world, x, y, z);
		}

		return true;
	}

/*	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public void addCreativeItems(ArrayList itemList) {
		itemList.add(new ItemStack(this));
	}*/

	@Override
	@Environment(EnvType.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		icon = par1IconRegister.registerIcon("buildcraft:refineryBack");
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Icon getIcon(int par1, int par2) {
		return icon;
	}
}
