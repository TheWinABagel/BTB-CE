package dev.bagel.btb.mixin;

import dev.bagel.btb.injected.TileEntityExtension;
import net.minecraft.src.TileEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TileEntity.class)
public abstract class TileEntityMixin implements TileEntityExtension {
    @Override
    public void onChunkUnload() {
    }
}
