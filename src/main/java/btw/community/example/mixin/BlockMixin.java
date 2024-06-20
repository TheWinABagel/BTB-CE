package btw.community.example.mixin;

import btw.community.example.injected.BlockExtension;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin implements BlockExtension {
    @Shadow @Final public int blockID;

    @Shadow public abstract String getUnlocalizedName();

    @Shadow public Material blockMaterial;

    @Shadow public abstract boolean renderAsNormalBlock();

    @Shadow public abstract boolean canProvidePower();

    @Inject(method = "<init>*", at = @At("TAIL"))
    private void btb$collect(int par1, Material par2Material, CallbackInfo ci) {

    }

}
