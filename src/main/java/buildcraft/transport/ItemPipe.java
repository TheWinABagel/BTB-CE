/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.transport;

import buildcraft.BuildCraftTransport;
import buildcraft.api.core.IIconProvider;
import buildcraft.core.IItemPipe;
import buildcraft.core.ItemBuildCraft;
import buildcraft.core.utils.BCLog;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.IconRegister;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Icon;
import net.minecraft.src.World;

import java.util.List;
import java.util.logging.Level;

public class ItemPipe extends ItemBuildCraft implements IItemPipe {

	@Environment(EnvType.CLIENT)
	private IIconProvider iconProvider;
	private int pipeIconIndex;

	protected ItemPipe(int i) {
		super(i);
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float par8, float par9, float par10) {
		int blockID = BuildCraftTransport.genericPipeBlock.blockID;
		Block block = BuildCraftTransport.genericPipeBlock;

		int id = world.getBlockId(i, j, k);

		if (id == Block.snow.blockID) {
			side = 1;
		} else if (id != Block.vine.blockID && id != Block.tallGrass.blockID && id != Block.deadBush.blockID
				&& (Block.blocksList[id] == null || !Block.blocksList[id].isReplaceableVegetation(world, i, j, k))) {
			if (side == 0) {
				j--;
			}
			if (side == 1) {
				j++;
			}
			if (side == 2) {
				k--;
			}
			if (side == 3) {
				k++;
			}
			if (side == 4) {
				i--;
			}
			if (side == 5) {
				i++;
			}
		}

		if (itemstack.stackSize == 0)
			return false;
		if (world.canPlaceEntityOnSide(blockID, i, j, k, false, side, entityplayer, itemstack)) {

			Pipe pipe = BlockGenericPipe.createPipe(itemID);
			if (pipe == null) {
				BCLog.logger.log(Level.WARNING, "Pipe failed to create during placement at {0},{1},{2}", new Object[]{i, j, k});
				return true;
			}
			if (BlockGenericPipe.placePipe(pipe, world, i, j, k, blockID, 0)) {

				Block.blocksList[blockID].onBlockPlacedBy(world, i, j, k, entityplayer, itemstack);
				world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F,
						block.stepSound.getPlaceSound(),
						(block.stepSound.getVolume() + 1.0F) / 2.0F,
						block.stepSound.getPitch() * 0.8F);
				itemstack.stackSize--;
			}
			return true;
		} else {
			BCLog.logger.log(Level.WARNING, "Pipe cannot be placed at {0},{1},{2}", new Object[]{i, j, k});
			return false;
		}
	}

	@Environment(EnvType.CLIENT)
	public void setPipesIcons(IIconProvider iconProvider) {
		this.iconProvider = iconProvider;
	}

	public void setPipeIconIndex(int index) {
		this.pipeIconIndex = index;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Icon getIconFromDamage(int par1) {
		if (iconProvider != null) { // invalid pipes won't have this set
			return iconProvider.getIcon(pipeIconIndex);
		} else {
			return null;
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		// NOOP
	}

	@Override
	@Environment(EnvType.CLIENT)
	public int getSpriteNumber() {
		return 0;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
		super.addInformation(stack, player, list, advanced);
		Class<? extends Pipe> pipe = BlockGenericPipe.pipes.get(itemID);
		List<String> toolTip = PipeToolTipManager.getToolTip(pipe);
		list.addAll(toolTip);
	}
}
