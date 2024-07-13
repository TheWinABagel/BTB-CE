package dev.bagel.btb.mixin.accessors;

import net.minecraft.src.NBTTagList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NBTTagList.class)
public interface NBTTagListAccessor {
    @Accessor
    byte getTagType();
}
