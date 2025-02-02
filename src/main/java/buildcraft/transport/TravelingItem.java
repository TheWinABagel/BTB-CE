/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.transport;

import buildcraft.BuildCraftCore;
import buildcraft.api.core.Position;
import buildcraft.core.inventory.StackHelper;
import buildcraft.core.proxy.CoreProxy;
import buildcraft.core.utils.EnumColor;
import com.google.common.collect.MapMaker;
import java.util.EnumSet;
import java.util.Map;

import net.fabricmc.api.EnvType;
import net.minecraft.src.*;
import net.minecraftforge.common.ForgeDirection;

public final class TravelingItem {

	public static final TravelingItemCache serverCache = new TravelingItemCache();
	public static final TravelingItemCache clientCache = new TravelingItemCache();
	public static final InsertionHandler DEFAULT_INSERTION_HANDLER = new InsertionHandler();
	private static int maxId = 0;
	protected float speed = 0.01F;
	private ItemStack itemStack;
	private TileEntity container;
	public double xCoord, yCoord, zCoord;
	public final int id;
	public boolean toCenter = true;
	public EnumColor color;
	public ForgeDirection input = ForgeDirection.UNKNOWN;
	public ForgeDirection output = ForgeDirection.UNKNOWN;
	public final EnumSet<ForgeDirection> blacklist = EnumSet.noneOf(ForgeDirection.class);
	private NBTTagCompound extraData;
	private InsertionHandler insertionHandler = DEFAULT_INSERTION_HANDLER;

	/* CONSTRUCTORS */
	private TravelingItem(int id) {
		this.id = id;
	}

	public static TravelingItem make(int id) {
		TravelingItem item = new TravelingItem(id);
		getCache().cache(item);
		return item;
	}

	public static TravelingItem make() {
		return make(maxId < Short.MAX_VALUE ? ++maxId : (maxId = Short.MIN_VALUE));
	}

	public static TravelingItem make(double x, double y, double z, ItemStack stack) {
		TravelingItem item = make();
		item.xCoord = x;
		item.yCoord = y;
		item.zCoord = z;
		item.itemStack = stack.copy();
		return item;
	}

	public static TravelingItem make(NBTTagCompound nbt) {
		TravelingItem item = make();
		item.readFromNBT(nbt);
		return item;
	}



	public static TravelingItemCache getCache() {
		if (BuildCraftCore.INSTANCE.getEffectiveSide() == EnvType.CLIENT)
			return clientCache;
		return serverCache;
	}

	/* GETTING & SETTING */
	public void setPosition(double x, double y, double z) {
		this.xCoord = x;
		this.yCoord = y;
		this.zCoord = z;
	}

	public void movePosition(double x, double y, double z) {
		this.xCoord += x;
		this.yCoord += y;
		this.zCoord += z;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public void setItemStack(ItemStack item) {
		this.itemStack = item;
	}

	public TileEntity getContainer() {
		return container;
	}

	public void setContainer(TileEntity container) {
		this.container = container;
	}

	public NBTTagCompound getExtraData() {
		if (extraData == null)
			extraData = new NBTTagCompound();
		return extraData;
	}

	public boolean hasExtraData() {
		return extraData != null;
	}

	public void setInsertionHandler(InsertionHandler handler) {
		if (handler == null)
			return;
		this.insertionHandler = handler;
	}

	public InsertionHandler getInsertionHandler() {
		return insertionHandler;
	}

	public void reset() {
		toCenter = true;
		blacklist.clear();
		input = ForgeDirection.UNKNOWN;
		output = ForgeDirection.UNKNOWN;
	}

	/* SAVING & LOADING */
	private void readFromNBT(NBTTagCompound data) {
		setPosition(data.getDouble("x"), data.getDouble("y"), data.getDouble("z"));

		setSpeed(data.getFloat("speed"));
		setItemStack(ItemStack.loadItemStackFromNBT(data.getCompoundTag("Item")));

		toCenter = data.getBoolean("toCenter");
		input = ForgeDirection.getOrientation(data.getInteger("input"));
		output = ForgeDirection.getOrientation(data.getInteger("output"));

		byte c = data.getByte("color");
		if (c != -1)
			color = EnumColor.fromId(c);

		if (data.hasKey("extraData"))
			extraData = data.getCompoundTag("extraData");
	}

	public void writeToNBT(NBTTagCompound data) {
		data.setDouble("x", xCoord);
		data.setDouble("y", yCoord);
		data.setDouble("z", zCoord);
		data.setFloat("speed", getSpeed());
		NBTTagCompound itemStackTag = new NBTTagCompound();
		getItemStack().writeToNBT(itemStackTag);
		data.setCompoundTag("Item", itemStackTag);

		data.setBoolean("toCenter", toCenter);
		data.setInteger("input", input.ordinal());
		data.setInteger("output", output.ordinal());

		data.setByte("color", color != null ? (byte) color.ordinal() : -1);

		if (extraData != null)
			data.setTag("extraData", extraData);
	}

	public EntityItem toEntityItem() {
		if (container != null && !CoreProxy.getProxy().isClientWorld(container.worldObj)) {
			if (getItemStack().stackSize <= 0)
				return null;

			Position motion = new Position(0, 0, 0, output);
			motion.moveForwards(0.1 + getSpeed() * 2F);

			EntityItem entity = new EntityItem(container.worldObj, xCoord, yCoord, zCoord, getItemStack());

			entity.age = BuildCraftCore.itemLifespan;
			entity.delayBeforeCanPickup = 10;

			float f3 = 0.00F + container.worldObj.rand.nextFloat() * 0.04F - 0.02F;
			entity.motionX = (float) container.worldObj.rand.nextGaussian() * f3 + motion.x;
			entity.motionY = (float) container.worldObj.rand.nextGaussian() * f3 + motion.y;
			entity.motionZ = (float) container.worldObj.rand.nextGaussian() * f3 + +motion.z;
			return entity;
		}
		return null;
	}

	public float getEntityBrightness(float f) {
		int i = MathHelper.floor_double(xCoord);
		int j = MathHelper.floor_double(zCoord);
		if (container != null && container.worldObj.blockExists(i, 128 / 2, j)) {
			double d = 0.66000000000000003D;
			int k = MathHelper.floor_double(yCoord + d);
			return container.worldObj.getLightBrightness(i, k, j);
		} else
			return 0.0F;
	}

	public boolean isCorrupted() {
		return itemStack == null || itemStack.stackSize <= 0 || Item.itemsList[itemStack.itemID] == null;
	}

	public boolean canBeGroupedWith(TravelingItem otherItem) {
		if(otherItem == this)
			return false;
		if (toCenter != otherItem.toCenter)
			return false;
		if (output != otherItem.output)
			return false;
		if (color != otherItem.color)
			return false;
		if (hasExtraData() || otherItem.hasExtraData())
			return false;
		if (insertionHandler != DEFAULT_INSERTION_HANDLER)
			return false;
		if (!blacklist.equals(otherItem.blacklist))
			return false;
		if (otherItem.isCorrupted())
			return false;
		return StackHelper.instance().canStacksMerge(itemStack, otherItem.itemStack);
	}

	public boolean tryMergeInto(TravelingItem otherItem) {
		if (!canBeGroupedWith(otherItem))
			return false;
		if (StackHelper.instance().mergeStacks(itemStack, otherItem.itemStack, false) == itemStack.stackSize) {
			StackHelper.instance().mergeStacks(itemStack, otherItem.itemStack, true);
			itemStack.stackSize = 0;
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 67 * hash + this.id;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TravelingItem other = (TravelingItem) obj;
		if (this.id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TravelingItem: " + id;
	}

	public static class InsertionHandler {

		public boolean canInsertItem(TravelingItem item, IInventory inv) {
			return true;
		}
	}

	public static class TravelingItemCache {

		private final Map<Integer, TravelingItem> itemCache = new MapMaker().weakValues().makeMap();

		public void cache(TravelingItem item) {
			itemCache.put(item.id, item);
		}

		public TravelingItem get(int id) {
			return itemCache.get(id);
		}
	}
}
