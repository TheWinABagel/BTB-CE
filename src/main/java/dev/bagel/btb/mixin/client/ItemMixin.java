package dev.bagel.btb.mixin.client;

import dev.bagel.btb.injected.client.ItemRenderExtension;
import net.minecraft.src.Item;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public abstract class ItemMixin implements ItemRenderExtension {
    //todotransport not necessary
    @Override
    public int getRenderPasses(int metadata) {
        return this.requiresMultipleRenderPasses() ? 2 : 1;
    }

    @Override
    public boolean requiresMultipleRenderPasses() {
        return false;
    }
}
