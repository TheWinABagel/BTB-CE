/**
 * Copyright (c) SpaceToad, 2011
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package buildcraft.builders;

import dev.bagel.btb.injected.EntityPlayerExtension;
import buildcraft.BuildCraftBuilders;
import buildcraft.api.filler.IFillerPattern;
import buildcraft.core.CreativeTabBuildCraft;
import buildcraft.core.GuiIds;
import buildcraft.core.proxy.CoreProxy;
import buildcraft.core.utils.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.Material;
import net.minecraft.src.IconRegister;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Icon;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

public class BlockFiller extends BlockContainer {

	Icon textureSides;
	Icon textureTopOn;
	Icon textureTopOff;
	public IFillerPattern currentPattern;

	public BlockFiller(int i) {
		super(i, Material.iron);

		setHardness(5F);
		setCreativeTab(CreativeTabBuildCraft.MACHINES.get());
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {

		// Drop through if the player is sneaking
		if (entityplayer.isSneaking())
			return false;

		if (!CoreProxy.getProxy().isClientWorld(world)) {
            ((EntityPlayerExtension) entityplayer).btb$openGui(BuildCraftBuilders.INSTANCE.getModId(), GuiIds.FILLER, world, i, j, k);
		}
		return true;

	}

	@Override
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
		int m = world.getBlockMetadata(x, y, z);
		TileEntity tile = world.getBlockTileEntity(x, y, z);

		if (tile != null && tile instanceof TileFiller) {
			TileFiller filler = (TileFiller) tile;
			if (side == 1 || side == 0) {
				if (!filler.isActive())
					return textureTopOff;
				else
					return textureTopOn;
			} else if (filler.currentPattern != null)
				return filler.currentPattern.getIcon();
			else
				return textureSides;
		}

		return getIcon(side, m);
	}

	@Override
	public Icon getIcon(int i, int j) {
		if (i == 0 || i == 1)
			return textureTopOn;
		else
			return textureSides;
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileFiller();
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		Utils.preDestroyBlock(world, x, y, z);
		super.breakBlock(world, x, y, z, par5, par6);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
	    textureTopOn = par1IconRegister.registerIcon("buildcraft:blockFillerTopOn");
        textureTopOff = par1IconRegister.registerIcon("buildcraft:blockFillerTopOff");
        textureSides = par1IconRegister.registerIcon("buildcraft:blockFillerSides");
	}
}
