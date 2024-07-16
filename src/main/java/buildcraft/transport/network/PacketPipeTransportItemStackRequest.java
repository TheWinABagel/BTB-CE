package buildcraft.transport.network;

import buildcraft.core.DefaultProps;
import buildcraft.core.network.BuildCraftPacket;
import buildcraft.core.network.PacketIds;
import buildcraft.transport.TravelingItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketPipeTransportItemStackRequest extends BuildCraftPacket {

	public int travelerID;
	public EntityPlayer player;

	public PacketPipeTransportItemStackRequest(EntityPlayer player) {
		this.player = player;
		this.channel = DefaultProps.TRANSPORT_CHANNEL_NAME;
	}

	public PacketPipeTransportItemStackRequest(int travelerID) {
		this.travelerID = travelerID;
		this.channel = DefaultProps.TRANSPORT_CHANNEL_NAME;
	}

	@Override
	public void writeData(DataOutputStream data) throws IOException {
		data.writeShort(travelerID);
	}

	@Override
	public void readData(DataInputStream data) throws IOException {
		travelerID = data.readShort();
		TravelingItem.TravelingItemCache cache = TravelingItem.serverCache;
		TravelingItem item = cache.get(travelerID);
        if (item == null) {
			return;
        }
        if (player instanceof EntityPlayerMP playerMP) {
            playerMP.playerNetServerHandler.sendPacketToPlayer(new PacketPipeTransportItemStack(travelerID, item.getItemStack()).getPacket());
        }
	}

	@Override
	public int getID() {
		return PacketIds.PIPE_ITEMSTACK_REQUEST;
	}
}
