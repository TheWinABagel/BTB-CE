package btw.community.example.mixin;

import net.minecraft.src.RenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(RenderManager.class)
public interface RenderManagerAccessor {
    @Accessor
    Map getEntityRenderMap();
}
