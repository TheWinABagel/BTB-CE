package buildcraft.core.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import dev.bagel.btb.extensions.BuildcraftCustomPacketHandler;
import buildcraft.builders.network.PacketHandlerBuilders;
import buildcraft.silicon.network.PacketHandlerSilicon;
import buildcraft.transport.network.PacketHandlerTransport;
import buildcraft.transport.network.PacketPipeTransportItemStackRequest;
import buildcraft.transport.network.PacketPipeTransportTraveler;
import net.minecraft.src.*;


public class PacketHandler implements BuildcraftCustomPacketHandler {
	@Override
	public void onPacketData(EntityPlayer player, Packet250CustomPayload packet, DataInputStream data, int packetID) {
		try {
			switch (packetID) {
				case PacketIds.TILE_UPDATE: {
					PacketTileUpdate pkt = new PacketTileUpdate();
					pkt.readData(data);
					onTileUpdate(player, pkt);
					break;
				}

				case PacketIds.STATE_UPDATE: {
					PacketTileState pkt = new PacketTileState();
					pkt.readData(data);
					World world = (player).worldObj;
					TileEntity tile = world.getBlockTileEntity(pkt.posX, pkt.posY, pkt.posZ);
					if (tile instanceof ISyncedTile) {
						pkt.applyStates(data, (ISyncedTile) tile);
					}
					break;
				}

				case PacketIds.GUI_RETURN: {
					PacketGuiReturn pkt = new PacketGuiReturn(player);
					pkt.readData(data);
					break;
				}

				case PacketIds.GUI_WIDGET: {
					PacketGuiWidget pkt = new PacketGuiWidget();
					pkt.readData(data);
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void onTileUpdate(EntityPlayer player, PacketTileUpdate packet) throws IOException {
		World world = player.worldObj;

		if (!packet.targetExists(world))
			return;

		TileEntity entity = packet.getTarget(world);
		if (!(entity instanceof ISynchronizedTile tile))
			return;

		tile.handleUpdatePacket(packet);
		tile.postPacketHandling(packet);
	}
}
