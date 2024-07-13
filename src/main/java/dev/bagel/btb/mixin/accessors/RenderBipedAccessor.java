package dev.bagel.btb.mixin.accessors;

import net.minecraft.src.RenderBiped;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderBiped.class)
public interface RenderBipedAccessor {
    @Accessor
    static String[] getBipedArmorFilenamePrefix() {
        throw new UnsupportedOperationException();
    }

    @Mutable
    @Accessor
    static void setBipedArmorFilenamePrefix(String[] bipedArmorFilenamePrefix) {
        throw new UnsupportedOperationException();
    }
}
