package dev.bagel.btb.mixin;

import dev.bagel.btb.extensions.BlockUnloadExtension;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(World.class)
public abstract class WorldMixin {

	@Shadow public abstract int getBlockId(int i, int j, int k);

	@Shadow private List entityRemoval;

	@Inject(method = "updateEntities", at = @At(value = "INVOKE", target = "Ljava/util/List;removeAll(Ljava/util/Collection;)Z", ordinal = 1))
	private void btb$onEntityUnloadHook(CallbackInfo ci) {
		this.entityRemoval.forEach(entity -> {
			if (entity instanceof BlockUnloadExtension unloadExtension) {
				unloadExtension.onChunkUnload();
			}
		});
	}

}
