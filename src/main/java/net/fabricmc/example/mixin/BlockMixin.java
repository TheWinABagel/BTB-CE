package net.fabricmc.example.mixin;

import buildcraft.api.bagel.BasicRegistry;
import net.fabricmc.example.injected.BlockExtension;
import net.minecraft.src.*;
import net.minecraftforge.common.util.ForgeDirection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.UP;

@Mixin(Block.class)
public abstract class BlockMixin implements BlockExtension {
    @Shadow @Final public int blockID;

    @Shadow public abstract String getUnlocalizedName();

    @Shadow public Material blockMaterial;

    @Shadow public abstract boolean renderAsNormalBlock();

    @Shadow public abstract boolean canProvidePower();

    @Inject(method = "<init>*", at = @At("TAIL"))
    private void btb$collect(int par1, Material par2Material, CallbackInfo ci) {
        //todo item damage aware version, maybe using getSubItems?
        BasicRegistry.blockNames.put(this.getUnlocalizedName(), this.blockID);
    }

    @Override
    public boolean recolourBlock(World world, int x, int y, int z, ForgeDirection side, int colour) {
        if (((Object) this) == Block.cloth)
        {
            int meta = world.getBlockMetadata(x, y, z);
            if (meta != colour)
            {
                world.setBlockMetadataWithNotify(x, y, z, colour, 3);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
        {
            return blockMaterial.isOpaque() && renderAsNormalBlock() && !canProvidePower();
        }
    }
}
