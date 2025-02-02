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
import buildcraft.core.GuiIds;
import buildcraft.core.proxy.CoreProxy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.Material;
import net.minecraft.src.IconRegister;
import net.minecraft.src.EntityLivingBase;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Icon;
import net.minecraft.src.World;

public class BlockBlueprintLibrary extends BlockContainer {

	private Icon textureTop;
    private Icon textureSide;

    public BlockBlueprintLibrary(int i) {
		super(i, Material.wood);
		//setCreativeTab(CreativeTabBuildCraft.MACHINES.get());
		setHardness(5F);
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		super.onBlockActivated(world, i, j, k, entityplayer, par6, par7, par8, par9);

		// Drop through if the player is sneaking
		if (entityplayer.isSneaking())
			return false;

		TileBlueprintLibrary tile = (TileBlueprintLibrary) world.getBlockTileEntity(i, j, k);

		if (!tile.locked || entityplayer.getEntityName().equals(tile.owner))
			if (!CoreProxy.getProxy().isClientWorld(world)) {
                ((EntityPlayerExtension) entityplayer).btb$openGui(BuildCraftBuilders.INSTANCE.getModId(), GuiIds.BLUEPRINT_LIBRARY, world, i, j, k);
			}

		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileBlueprintLibrary();
	}

	@Override
	public Icon getIcon(int i, int j) {
		switch (i) {
		case 0:
		case 1:
			return textureTop;
		default:
			return textureSide;
		}
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLivingBase entityliving, ItemStack stack) {
		if (CoreProxy.getProxy().isServerWorld(world) && entityliving instanceof EntityPlayer) {
			TileBlueprintLibrary tile = (TileBlueprintLibrary) world.getBlockTileEntity(i, j, k);
			tile.owner = ((EntityPlayer) entityliving).getEntityName();
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
	    textureTop = par1IconRegister.registerIcon("buildcraft:library_topbottom");
        textureSide = par1IconRegister.registerIcon("buildcraft:library_side");
	}
}
