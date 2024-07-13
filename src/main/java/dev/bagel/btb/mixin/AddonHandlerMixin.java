package dev.bagel.btb.mixin;

import btw.AddonHandler;
import buildcraft.BuildCraftAddon;
import btw.network.packet.handler.CustomPacketHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Minecraft;
import net.minecraft.src.NetServerHandler;
import net.minecraft.src.Packet250CustomPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

@Mixin(value = AddonHandler.class)
public class AddonHandlerMixin {

    @Environment(EnvType.CLIENT)
    @Inject(method = "clientCustomPacketReceived", at = @At("HEAD"), cancellable = true, remap = false)
    private static void btb$handleClientBCPacket(Minecraft mcInstance, Packet250CustomPayload packet, CallbackInfo ci) {
        try {
            if (BuildCraftAddon.BCPacketHandlers.get(packet.channel) != null) {
                DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
                int packetID = data.read();
                BuildCraftAddon.BCPacketHandlers.forEach((string, packetHandler) -> {
                    try {
                        packetHandler.onPacketData(mcInstance.thePlayer, packet, data, packetID);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                ci.cancel();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Inject(method = "serverCustomPacketReceived", at = @At("HEAD"), cancellable = true, remap = false)
    private static void btb$handleServerBCPacket(NetServerHandler handler, Packet250CustomPayload packet, CallbackInfo ci) {
        try {
            if (BuildCraftAddon.BCPacketHandlers.get(packet.channel) != null) {
                DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
                int packetID = data.read();
                BuildCraftAddon.BCPacketHandlers.forEach((string, packetHandler) -> {
                    try {
                        packetHandler.onPacketData(handler.playerEntity, packet, data, packetID);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                BuildCraftAddon.BCPacketHandlers.get(packet.channel);
                ci.cancel();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
