package net.fabricmc.example.mixin;

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
}
