package btw.community.example.extensions;

import btw.network.packet.handler.CustomPacketHandler;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.NetServerHandler;
import net.minecraft.src.Packet250CustomPayload;

import java.io.IOException;

public interface BuildcraftCustomPacketHandler extends CustomPacketHandler {

    @Override
    default void handleCustomPacket(Packet250CustomPayload var1) throws IOException {
        //NO-OP
    }

    public void onPacketData(EntityPlayer player, Packet250CustomPayload packet) throws IOException;
}
