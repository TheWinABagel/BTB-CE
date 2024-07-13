package dev.bagel.btb.mixin.accessors;

import net.minecraft.src.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(NBTTagCompound.class)
public interface NBTTagCompoundAccessor {
    @Accessor
    Map getTagMap();
}
