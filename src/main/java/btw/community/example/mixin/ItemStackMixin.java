package btw.community.example.mixin;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "getTooltip", at = @At("RETURN"))
    private void btb$tooltipHook(EntityPlayer player, boolean advancedTooltip, CallbackInfoReturnable<List<String>> cir) {
//        cir.setReturnValue(ListTooltipHandler.itemTooltipEvent((ItemStack) (Object) this, player, cir.getReturnValue(), advancedTooltip));
    }
}
