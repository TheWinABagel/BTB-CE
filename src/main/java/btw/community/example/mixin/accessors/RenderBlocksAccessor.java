package btw.community.example.mixin.accessors;

import net.minecraft.src.RenderBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderBlocks.class)
public interface RenderBlocksAccessor {
    @Accessor
    boolean isRenderAllFaces();

    @Accessor
    double getRenderMinY();

    @Accessor
    double getRenderMaxY();

    @Accessor
    void setRenderMinY(double renderMinY);

    @Accessor
    void setRenderMaxY(double renderMaxY);

    @Accessor
    void setUvRotateTop(int uvRotateTop);

    @Accessor
    void setUvRotateBottom(int uvRotateBottom);
}
