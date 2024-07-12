package btw.community.example.mixin;

import buildcraft.builders.EventHandlerBuilders;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.File;

@Mixin(IntegratedServer.class)
public abstract class IntegratedServerMixin extends MinecraftServer {
    public IntegratedServerMixin(File par1File) {
        super(par1File);
    }

    @Inject(method = "loadAllWorlds", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/src/ServerConfigurationManager;setPlayerManager([Lnet/minecraft/src/WorldServer;)V",
            shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void btb$onIntegratedWorldLoad(String par1Str, String par2Str, long par3, WorldType par5WorldType, String par6Str, CallbackInfo ci, ISaveHandler var7, int worldId, byte var9) {
        EventHandlerBuilders.handleWorldLoad(this.worldServers[worldId]);
    }
}
