package btw.community.example.mixin;

import buildcraft.BuildCraftCore;
import net.minecraft.src.ResourceManager;
import net.minecraft.src.TextureMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureMap.class)
public abstract class TextureMapMixin {

    @Inject(method = "loadTextureAtlas", at = @At(value = "INVOKE", target = "Ljava/util/List;clear()V", shift = At.Shift.AFTER))
    private void btb$hookRegisterTextures(ResourceManager par1ResourceManager, CallbackInfo ci) {
        BuildCraftCore.textureHook((TextureMap) (Object) this);
    }

}
