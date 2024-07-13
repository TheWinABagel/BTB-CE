package buildcraft.builders.network;

import buildcraft.core.DefaultProps;
import buildcraft.core.network.PacketCoordinates;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketLibraryAction extends PacketCoordinates {

	public int actionId;

	public PacketLibraryAction() {
		this.channel = DefaultProps.BUILDERS_CHANNEL_NAME;
	}

	public PacketLibraryAction(int packetId, int x, int y, int z) {
		super(packetId, x, y, z);
	}

	@Override
	public void writeData(DataOutputStream data) throws IOException {
		data.writeInt(actionId);
		super.writeData(data);
	}

	@Override
	public void readData(DataInputStream data) throws IOException {
		actionId = data.readInt();
		super.readData(data);
	}
}
