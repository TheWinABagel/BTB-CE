/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.energy;

import net.minecraft.src.*;

import java.util.HashMap;
import java.util.Map;

public class BucketHandler {

	public static BucketHandler INSTANCE = new BucketHandler();
	public Map<Block, Item> buckets = new HashMap<Block, Item>();

	private BucketHandler() {
	}


	public ItemStack onBucketFill(EntityPlayer player, ItemStack currentStack, World world, MovingObjectPosition target) {

		ItemStack result = fillCustomBucket(world, target);

		if (result == null)
			return null;

		if (player.capabilities.isCreativeMode) return currentStack;

		return result;
	}

	private ItemStack fillCustomBucket(World world, MovingObjectPosition pos) {

		int blockID = world.getBlockId(pos.blockX, pos.blockY, pos.blockZ);

		Item bucket = buckets.get(Block.blocksList[blockID]);
		if (bucket != null && world.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ) == 0) {
			world.setBlock(pos.blockX, pos.blockY, pos.blockZ, 0);
			return new ItemStack(bucket);
		} else
			return null;

	}
}
