package btw.community.example.mixin;

import buildcraft.builders.EventHandlerBuilders;
import buildcraft.builders.TilePathMarker;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Shadow public WorldServer[] worldServers;

    @Inject(method = "loadAllWorlds", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/src/ServerConfigurationManager;setPlayerManager([Lnet/minecraft/src/WorldServer;)V",
            shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void btb$onServerWorldLoad(String par1Str, String par2Str, long par3, WorldType par5WorldType, String par6Str, CallbackInfo ci, ISaveHandler var7, WorldInfo var9, WorldSettings var8, int worldId, byte var11) {
        EventHandlerBuilders.handleWorldLoad(this.worldServers[worldId]);
    }

    @Inject(method = "stopServer", at = @At(
            value = "INVOKE", target = "Lnet/minecraft/src/WorldServer;flush()V",
            shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void btb$onServerWorldUnloadOne(CallbackInfo ci, int var1, WorldServer world) {
        EventHandlerBuilders.handleWorldUnload(world);
    }

    @Inject(method = "deleteWorldAndStopServer", at = @At(
            value = "INVOKE", target = "Lnet/minecraft/src/WorldServer;flush()V",
            shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void btb$onServerWorldUnloadTwo(CallbackInfo ci, int var1, WorldServer world) {
        EventHandlerBuilders.handleWorldUnload(world);
    }

    @Inject(method = "run", at = @At(value = "FIELD", target = "net/minecraft/server/MinecraftServer.serverIsRunning : Z", shift = At.Shift.AFTER))
    private void willThisWorkWtf(CallbackInfo ci) {
        TilePathMarker.clearAvailableMarkersList();
    }
}
