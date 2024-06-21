/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.transport;

import btw.community.example.injected.EntityPlayerExtension;
import buildcraft.BuildCraftTransport;
import buildcraft.core.BlockBuildCraft;
import buildcraft.core.GuiIds;
import buildcraft.core.IItemPipe;
import buildcraft.core.proxy.CoreProxy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Material;
import net.minecraft.src.IconRegister;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Icon;
import net.minecraft.src.World;

import java.util.ArrayList;

/**
 *
 * @author SandGrainOne
 */
public class BlockFilteredBuffer extends BlockBuildCraft {

	private static Icon blockTexture;

	public BlockFilteredBuffer(int blockId) {
		super(blockId, Material.iron);
		setHardness(5F);
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileFilteredBuffer();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer,
			int par6, float par7, float par8, float par9) {

		super.onBlockActivated(world, x, y, z, entityplayer, par6, par7, par8, par9);

		if (entityplayer.isSneaking()) {
			return false;
		}

		if (entityplayer.getCurrentEquippedItem() != null) {
			if (entityplayer.getCurrentEquippedItem().getItem() instanceof IItemPipe) {
				return false;
			}
		}

		if (!CoreProxy.proxy.isRenderWorld(world)) {
			((EntityPlayerExtension) entityplayer).openGui(BuildCraftTransport.instance.getModId(), GuiIds.FILTERED_BUFFER, world, x, y, z);
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
		blockTexture = par1IconRegister.registerIcon("buildcraft:filteredBuffer_all");
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Icon getIcon(int i, int j) {
		return blockTexture;
	}
}
