package btw.community.example.mixin.client;

import buildcraft.BuildCraftAddon;
import buildcraft.BuildCraftCore;
import buildcraft.BuildCraftEnergy;
import buildcraft.BuildCraftFactory;
import net.minecraft.src.BlockFluid;
import net.minecraft.src.ResourceManager;
import net.minecraft.src.TextureMap;
import net.minecraftforge.fluids.FluidRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureMap.class)
public abstract class TextureMapMixin {

    @Inject(method = "loadTextureAtlas", at = @At(value = "INVOKE", target = "Ljava/util/List;clear()V", shift = At.Shift.AFTER))
    private void btb$hookRegisterTexturesPre(ResourceManager par1ResourceManager, CallbackInfo ci) {
        BuildCraftAddon.textureHook((TextureMap) (Object) this);
    }

    @Inject(method = "loadTextureAtlas", at = @At("TAIL"))
    private void btb$hookRegisterTexturesPost(ResourceManager par1ResourceManager, CallbackInfo ci) {
        FluidRegistry.WATER.setIcons(BlockFluid.getFluidIcon("water_still"), BlockFluid.getFluidIcon("water_flow"));
        FluidRegistry.LAVA.setIcons(BlockFluid.getFluidIcon("lava_still"), BlockFluid.getFluidIcon("lava_flow"));
    }
}
