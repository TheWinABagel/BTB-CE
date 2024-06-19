package net.fabricmc.example.mixin;

import net.fabricmc.example.injected.TileEntityExtension;
import net.minecraft.src.TileEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TileEntity.class)
public abstract class TileEntityMixin implements TileEntityExtension {
    @Override
    public void onChunkUnload() {
    }
}
