package btw.community.example.mixin.accessors;

import btw.AddonHandler;
import btw.network.packet.handler.CustomPacketHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(value = AddonHandler.class, remap = false)
public interface AddonHandlerAccessor {
    @Accessor
    static Map<String, CustomPacketHandler> getPacketHandlers() {
        throw new UnsupportedOperationException();
    }
}
