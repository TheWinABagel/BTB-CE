package btw.community.example.extensions;

import btw.network.packet.handler.CustomPacketHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

import java.io.IOException;

public interface BuildcraftCustomPacketHandler extends CustomPacketHandler { //Forge is IPacketHandler

    @Override
    default void handleCustomPacket(Packet250CustomPayload packet) throws IOException {
        if (Minecraft.getMinecraft() != null) {
            this.onPacketData(Minecraft.getMinecraft().thePlayer, packet);
        }
        else if (MinecraftServer.getServer() != null) {
            System.err.println("server recieved packet from a player, but it cant tell which one");
        }
        //NO-OP
    }

    public void onPacketData(EntityPlayer player, Packet250CustomPayload packet) throws IOException;
}
