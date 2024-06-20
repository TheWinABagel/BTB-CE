package btw.community.example.mixin;

import btw.community.example.injected.TileEntityExtension;
import net.minecraft.src.TileEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TileEntity.class)
public abstract class TileEntityMixin implements TileEntityExtension {
    @Override
    public void onChunkUnload() {
    }
}
