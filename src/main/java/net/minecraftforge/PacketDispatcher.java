package net.minecraftforge;

import buildcraft.core.utils.BCLog;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.Minecraft;
import net.minecraft.src.Packet;

public class PacketDispatcher {

    public static void sendPacketToServer(Packet packet) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc != null && mc.thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(packet);
        }
    }

    public static void sendPacketToPlayer(Packet packet, EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            ((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(packet);
        }

    }

    public static void sendPacketToAllAround(double X, double Y, double Z, double range, int dimensionId, Packet packet) {
        MinecraftServer server = MinecraftServer.getServer();
        if (server != null) {
            server.getConfigurationManager().sendToAllNear(X, Y, Z, range, dimensionId, packet);
        } else {
            BCLog.logger.fine("Attempt to send packet to all around without a server instance available");
        }

    }
}
