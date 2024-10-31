package dev.bagel.btb.extensions;

import btw.network.packet.handler.CustomPacketHandler;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Packet250CustomPayload;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface BuildcraftCustomPacketHandler extends CustomPacketHandler { //Forge is IPacketHandler
    List<BuildcraftCustomPacketHandler> ID_MAP = new ArrayList<>();
    public void onPacketData(EntityPlayer player, Packet250CustomPayload packet, DataInputStream data, int packetID) throws IOException;

    @Override
    default void handleCustomPacket(Packet250CustomPayload packet, EntityPlayer player) throws IOException {
        DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
        int packetID = data.read();
        for (BuildcraftCustomPacketHandler handler : ID_MAP) {
            handler.onPacketData(player, packet, data, packetID);
        }
    }
}
