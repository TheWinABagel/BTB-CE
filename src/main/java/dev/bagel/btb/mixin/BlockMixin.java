package dev.bagel.btb.mixin;

import dev.bagel.btb.injected.BlockExtension;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Random;

@Mixin(Block.class)
public abstract class BlockMixin implements BlockExtension {
    @Shadow @Final public int blockID;

    @Shadow public abstract String getUnlocalizedName();

    @Shadow public Material blockMaterial;

    @Shadow public abstract boolean renderAsNormalBlock();

    @Shadow public abstract boolean canProvidePower();

    @Shadow public abstract int quantityDroppedWithBonus(int par1, Random par2Random);

    @Shadow public abstract int idDropped(int par1, Random par2Random, int par3);

    @Shadow public abstract int damageDropped(int par1);

    @Inject(method = "<init>*", at = @At("TAIL"))
    private void btb$collect(int par1, Material par2Material, CallbackInfo ci) {

    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList();
        int count = this.quantityDroppedWithBonus(fortune, world.rand);

        for(int i = 0; i < count; ++i) {
            int id = this.idDropped(metadata, world.rand, fortune);
            if (id > 0) {
                ret.add(new ItemStack(id, 1, this.damageDropped(metadata)));
            }
        }

        return ret;
    }

/*    @Inject(method = "renderBlockAsItem", at = @At("HEAD"), cancellable = true)
    private void test(RenderBlocks renderBlocks, int iItemDamage, float fBrightness, CallbackInfo ci) {
        if (RenderingRegistry.instance().blockRenderers.containsKey(((Block)(Object) this).getRenderType())) {
            RenderingRegistry.instance().renderInventoryBlock(renderBlocks, (Block)(Object) this, iItemDamage, (int) fBrightness);
            ci.cancel();
        }
    }*/
}
