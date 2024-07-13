package dev.bagel.btb.mixin.accessors;

import btw.world.chunk.ChunkTrackerEntry;
import net.minecraft.src.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChunkTrackerEntry.class)
public interface ChunkTrackerEntryAccessor {
    @Invoker
    void callSendToPlayersWatchingNotWaitingFullChunk(Packet packet);
}
