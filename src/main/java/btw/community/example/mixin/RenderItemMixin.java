package btw.community.example.mixin;

import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItem.class)
public class RenderItemMixin {
    @Inject(method = "renderItemIntoGUI", at = @At("HEAD"))
    private void test(FontRenderer par1FontRenderer, TextureManager par2TextureManager, ItemStack par3ItemStack, int par4, int par5, CallbackInfo ci) {
        if (par3ItemStack == null) return;
        int id = par3ItemStack.itemID;
/*        Block blk = Block.blocksList[id];
        if (par3ItemStack.getItemSpriteNumber() == 0 && blk == null) {
            System.out.println("The broken item id is: " + id);
        }*/
    }
}
