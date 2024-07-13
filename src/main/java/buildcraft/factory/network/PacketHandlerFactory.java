package buildcraft.factory.network;

import dev.bagel.btb.extensions.BuildcraftCustomPacketHandler;
import buildcraft.core.network.PacketIds;
import buildcraft.core.network.PacketPayloadStream;
import buildcraft.core.network.PacketUpdate;
import buildcraft.factory.TileRefinery;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.src.*;
import net.minecraftforge.fluids.FluidRegistry;

public class PacketHandlerFactory implements BuildcraftCustomPacketHandler {

	@Override
	public void onPacketData(EntityPlayer player, Packet250CustomPayload packet, DataInputStream data, int packetID) throws IOException {
		try {
			PacketUpdate packetU = new PacketUpdate();
			switch (packetID) {

				case PacketIds.REFINERY_FILTER_SET:
					packetU.readData(data);
					onRefinerySelect(player, packetU);
					break;

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private TileRefinery getRefinery(World world, int x, int y, int z) {
		if (!world.blockExists(x, y, z))
			return null;

		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if (!(tile instanceof TileRefinery))
			return null;

		return (TileRefinery) tile;
	}

	private void onRefinerySelect(EntityPlayer playerEntity, PacketUpdate packet) throws IOException {

		TileRefinery tile = getRefinery(playerEntity.worldObj, packet.posX, packet.posY, packet.posZ);
		if (tile == null || packet.payload == null)
			return;
	
		DataInputStream stream = ((PacketPayloadStream)packet.payload).stream;

		tile.setFilter(stream.readByte(), FluidRegistry.getFluid(stream.readShort()));
	}
}
