package btw.community.example.mixin;

import buildcraft.energy.worldgen.BiomeInitializer;
import net.minecraft.src.GenLayer;
import net.minecraft.src.WorldChunkManager;
import net.minecraft.src.WorldType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(WorldChunkManager.class)
public class WorldChunkManagerMixin {

    @ModifyVariable(method = "<init>(JLnet/minecraft/src/WorldType;)V", at = @At(value = "STORE", ordinal = 0), index = 4)
    private GenLayer[] btb$biomeModifications(GenLayer[] original, long seed, WorldType worldType) {
        return BiomeInitializer.initBcBiomes(worldType, seed, original);
    }
}
