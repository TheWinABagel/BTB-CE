package btw.community.example.mixin;

import btw.community.example.injected.ItemExtension;
import net.minecraft.src.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public abstract class ItemMixin implements ItemExtension {
    @Shadow @Final public int itemID;

    @Shadow public abstract String getUnlocalizedName();

    @Inject(method = "<init>*", at = @At("TAIL"))
    private void btb$collect(int id, CallbackInfo ci) {

    }

}
