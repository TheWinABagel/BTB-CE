package net.fabricmc.example.mixin;

import buildcraft.api.interfaces.IWorldExtension;
import net.minecraft.src.Block;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public abstract class ExampleMixin implements IWorldExtension {

	@Shadow public abstract int getBlockId(int i, int j, int k);

	@Override
	public Block getBlock(int x, int y, int z) {
		return Block.blocksList[getBlockId(x, y, z)];
	}


}
