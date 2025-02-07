package dev.bagel.btb.injected;

import net.minecraft.src.EffectRenderer;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;

public interface CustomDestroyEffectsBlock {
    boolean addBlockDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer);
    boolean addBlockHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer);
}
