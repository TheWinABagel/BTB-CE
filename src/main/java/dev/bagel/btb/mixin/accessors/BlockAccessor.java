package dev.bagel.btb.mixin.accessors;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Block.class)
public interface BlockAccessor {

    @Accessor
    AxisAlignedBB getFixedBlockBounds();
}
