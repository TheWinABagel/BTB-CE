package buildcraft.core.network;

import buildcraft.core.DefaultProps;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;

public abstract class BuildCraftPacket {

	protected boolean isChunkDataPacket = false;
	protected String channel = DefaultProps.NET_CHANNEL_NAME;

	public abstract int getID();

	public Packet getPacket() {

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream data = new DataOutputStream(bytes);
		try {
			data.writeByte(getID());
			writeData(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Packet250CustomPayload packet = new Packet250CustomPayload(channel, bytes.toByteArray());
		packet.isChunkDataPacket = this.isChunkDataPacket;
		return packet;
	}

	public abstract void readData(DataInputStream data) throws IOException;

	public abstract void writeData(DataOutputStream data) throws IOException;
}
