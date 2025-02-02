/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core;

import dev.bagel.btb.mixin.accessors.EntityPlayerAccessor;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.core.network.ISynchronizedTile;
import buildcraft.core.network.PacketPayload;
import buildcraft.core.network.PacketPayloadArrays;
import buildcraft.core.network.PacketTileUpdate;
import buildcraft.core.network.PacketUpdate;
import buildcraft.core.network.TilePacketWrapper;
import buildcraft.core.proxy.CoreProxy;
import buildcraft.core.utils.Utils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.src.EntityLivingBase;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public abstract class TileBuildCraft extends TileEntity implements ISynchronizedTile {

	@SuppressWarnings("rawtypes")
	private static Map<Class, TilePacketWrapper> updateWrappers = new HashMap<Class, TilePacketWrapper>();
	@SuppressWarnings("rawtypes")
	private static Map<Class, TilePacketWrapper> descriptionWrappers = new HashMap<Class, TilePacketWrapper>();
	private final TilePacketWrapper descriptionPacket;
	private final TilePacketWrapper updatePacket;
	private boolean init = false;
	private String owner = "[BuildCraft]";

	public TileBuildCraft() {
		if (!updateWrappers.containsKey(this.getClass())) {
			updateWrappers.put(this.getClass(), new TilePacketWrapper(this.getClass()));
		}

		if (!descriptionWrappers.containsKey(this.getClass())) {
			descriptionWrappers.put(this.getClass(), new TilePacketWrapper(this.getClass()));
		}

		updatePacket = updateWrappers.get(this.getClass());
		descriptionPacket = descriptionWrappers.get(this.getClass());

	}

	public String getOwner() {
		return owner;
	}

	@Override
	public void updateEntity() {
		if (!init && !isInvalid()) {
			initialize();
			init = true;
		}

		if (this instanceof IPowerReceptor receptor) {
            receptor.getPowerReceiver(null).update();
		}
	}

	@Override
	public void invalidate() {
		init = false;
		super.invalidate();
	}

	public void initialize() {
		Utils.handleBufferedDescription(this);
	}

	public void onBlockPlacedBy(EntityLivingBase entity, ItemStack stack) {
		if (entity instanceof EntityPlayer player)
			owner = ((EntityPlayerAccessor) player).getUsername();
	}

	public void destroy() {
	}

	public void sendNetworkUpdate() {
		if (CoreProxy.getProxy().isServerWorld(worldObj)) {
			CoreProxy.getProxy().sendToPlayers(getUpdatePacket(), worldObj, xCoord, yCoord, zCoord, DefaultProps.NETWORK_UPDATE_RANGE);
		}
	}

	@Override
	public Packet getDescriptionPacket() {
		return new PacketTileUpdate(this).getPacket();
	}

	@Override
	public PacketPayload getPacketPayload() {
		return updatePacket.toPayload(this);
	}

	@Override
	public Packet getUpdatePacket() {
		return new PacketTileUpdate(this).getPacket();
	}

	@Override
	public void handleDescriptionPacket(PacketUpdate packet) throws IOException {
		if (packet.payload instanceof PacketPayloadArrays payloadArrays)
			descriptionPacket.fromPayload(this, payloadArrays);
	}

	@Override
	public void handleUpdatePacket(PacketUpdate packet) throws IOException {
		if (packet.payload instanceof PacketPayloadArrays payloadArrays)
			updatePacket.fromPayload(this, payloadArrays);
	}

	@Override
	public void postPacketHandling(PacketUpdate packet) {
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setString("owner", owner);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey("owner"))
			owner = nbt.getString("owner");
	}



	public boolean isInvNameLocalized() {
		// TODO Auto-generated method stub
		return false;
	}

	public World getWorld() {
		return worldObj;
	}
}
