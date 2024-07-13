package dev.bagel.btb.extensions;

import net.minecraft.src.*;

import java.io.DataInputStream;
import java.io.IOException;

public interface BuildcraftCustomPacketHandler { //Forge is IPacketHandler

    public void onPacketData(EntityPlayer player, Packet250CustomPayload packet, DataInputStream data, int packetID) throws IOException;

}
