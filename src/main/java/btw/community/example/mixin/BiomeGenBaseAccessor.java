package btw.community.example.mixin;

import net.minecraft.src.BiomeGenBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BiomeGenBase.class)
public interface BiomeGenBaseAccessor {
    @Invoker
    BiomeGenBase callSetMinMaxHeight(float par1, float par2);
}
