package btw.community.example.mixin;

import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(World.class)
public abstract class WorldMixin {

	@Shadow public abstract int getBlockId(int i, int j, int k);




}
