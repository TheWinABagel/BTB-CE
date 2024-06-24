package btw.community.example.mixin.accessors;

import net.minecraft.src.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(targets = "btw.world.chunk.ChunkTrackerEntry")
public interface ChunkTrackerEntryAccessor {
    @Invoker
    void callSendToPlayersWatchingNotWaitingFullChunk(Packet packet);
}
