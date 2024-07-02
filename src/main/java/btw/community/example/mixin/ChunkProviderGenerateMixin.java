package btw.community.example.mixin;

import buildcraft.core.SpringPopulate;
import buildcraft.energy.worldgen.OilPopulate;
import net.minecraft.src.ChunkProviderGenerate;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(ChunkProviderGenerate.class)
public class ChunkProviderGenerateMixin {

    @Shadow private World worldObj;

    @Shadow private Random rand;

    @Inject(method = "populate", at = @At("TAIL"))
    private void generateStructures(IChunkProvider chunkProvider, int chunkX, int chunkZ, CallbackInfo ci) {
        SpringPopulate.populate(chunkProvider, worldObj, rand, chunkX, chunkZ);
    }

    @Inject(method = "populate", at = @At(value = "INVOKE", target = "Ljava/util/Random;setSeed(J)V", ordinal = 1, shift = At.Shift.AFTER))
    private void generateOil(IChunkProvider chunkProvider, int chunkX, int chunkZ, CallbackInfo ci) {
        OilPopulate.INSTANCE.generateOil(this.worldObj, this.rand, chunkX, chunkZ);
    }
}
