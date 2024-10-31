package dev.bagel.btb.mixin.client;

import dev.bagel.btb.injected.CustomBoundingBoxBlock;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderGlobal.class)
public abstract class RenderGlobalMixin {

    @Shadow protected abstract void drawOutlinedBoundingBox(AxisAlignedBB par1AxisAlignedBB);

    @Shadow private WorldClient theWorld;

    @Redirect(method = "drawSelectionBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderGlobal;drawOutlinedBoundingBox(Lnet/minecraft/src/AxisAlignedBB;)V"))
    private void test(RenderGlobal instance, AxisAlignedBB aabb, EntityPlayer player, MovingObjectPosition pos, int par3, float par4) {
        int blockId = this.theWorld.getBlockId(pos.blockX, pos.blockY, pos.blockZ);
        Block block = Block.blocksList[blockId];
        if (block instanceof CustomBoundingBoxBlock cbbb) {
            float expand = 0.002f;
            double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)par4;
            double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)par4;
            double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)par4;
            for (AxisAlignedBB bb : cbbb.getCustomSelectionBoxes(this.theWorld, pos.blockX, pos.blockY, pos.blockZ)) {
                bb = bb.makeTemporaryCopy();

                int facing = theWorld.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ);
                bb.rotateAroundYToFacing(facing);
                bb.tiltToFacingAlongY(facing);
                bb = bb.offset(pos.blockX, pos.blockY, pos.blockZ).expand(expand, expand, expand).getOffsetBoundingBox(-posX, -posY, -posZ);
                drawOutlinedBoundingBox(bb);
            }
        }
        else {
            drawOutlinedBoundingBox(aabb);
        }
    }
}
