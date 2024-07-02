package btw.community.example.mixin.accessors;

import btw.world.chunk.ChunkTracker;
import btw.world.chunk.ChunkTrackerEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChunkTracker.class)
public interface ChunkTrackerAccessor {
    @Invoker(remap = false)
    ChunkTrackerEntry callGetOrCreateTrackerEntry(int iChunkX, int iChunkZ);
}
