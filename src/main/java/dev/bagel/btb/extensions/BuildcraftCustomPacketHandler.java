package dev.bagel.btb.extensions;

import btw.network.packet.handler.CustomPacketHandler;
import net.minecraft.src.*;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public interface BuildcraftCustomPacketHandler extends CustomPacketHandler { //Forge is IPacketHandler

    public void onPacketData(EntityPlayer player, Packet250CustomPayload packet, DataInputStream data, int packetID) throws IOException;

    @Override
    default void handleCustomPacket(Packet250CustomPayload packet, EntityPlayer player) throws IOException {
        DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
        int packetID = data.read();
        onPacketData(player, packet, data, packetID);
    }
}
