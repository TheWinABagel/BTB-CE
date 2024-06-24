package btw.community.example.mixin;

import btw.AddonHandler;
import btw.community.example.extensions.BuildcraftCustomPacketHandler;
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

import java.io.IOException;
import java.util.Map;

@Mixin(AddonHandler.class)
public class AddonHandlerMixin {

    @Shadow private static Map<String, CustomPacketHandler> packetHandlers;

    @Environment(EnvType.CLIENT)
    @Inject(method = "clientCustomPacketReceived", at = @At(value = "INVOKE", target = "Lbtw/network/packet/handler/CustomPacketHandler;handleCustomPacket(Lnet/minecraft/src/Packet250CustomPayload;)V"))
    private static void btb$handleClientBCPacket(Minecraft mcInstance, Packet250CustomPayload packet, CallbackInfo ci) {
        try {
            if (AddonHandlerAccessor.getPacketHandlers().get(packet.channel) instanceof BuildcraftCustomPacketHandler bc) {
                bc.onPacketData(mcInstance.thePlayer, packet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Inject(method = "serverCustomPacketReceived", at = @At(value = "INVOKE", target = "Lbtw/network/packet/handler/CustomPacketHandler;handleCustomPacket(Lnet/minecraft/src/Packet250CustomPayload;)V"))
    private static void btb$handleServerBCPacket(NetServerHandler handler, Packet250CustomPayload packet, CallbackInfo ci) {
        try {
            if (AddonHandlerAccessor.getPacketHandlers().get(packet.channel) instanceof BuildcraftCustomPacketHandler bc) {
                bc.onPacketData(handler.playerEntity, packet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
