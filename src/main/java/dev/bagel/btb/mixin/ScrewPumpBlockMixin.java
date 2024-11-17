package dev.bagel.btb.mixin;

import btw.block.FluidSource;
import btw.block.MechanicalBlock;
import btw.block.blocks.ScrewPumpBlock;
import btw.util.MiscUtils;
import btw.world.util.BlockPos;
import dev.bagel.btb.extensions.TileScrewPump;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Debug(export = true)
@Mixin(ScrewPumpBlock.class)
public abstract class ScrewPumpBlockMixin extends Block implements MechanicalBlock, FluidSource, ITileEntityProvider {
    private final int btb$toDrain = 1;

    protected ScrewPumpBlockMixin(int par1, Material par2Material) {
        super(par1, par2Material);
    }

    @Shadow
    public abstract void setIsJammed(World world, int i, int j, int k, boolean bJammed);

    @Shadow
    protected abstract boolean startPumpSourceCheck(World world, int i, int j, int k);

    @Shadow
    public abstract boolean canInputAxlePowerToFacing(World world, int i, int j, int k, int iFacing);

    @Shadow
    protected abstract boolean onNeighborChangeShortPumpSourceCheck(World world, int i, int j, int k);

    @Shadow
    public abstract void setMechanicalOn(World world, int i, int j, int k, boolean bOn);

    @Shadow
    public abstract boolean isPumpingWater(World world, int i, int j, int k);

    @Shadow
    public abstract boolean isMechanicalOn(IBlockAccess blockAccess, int i, int j, int k);

    @Shadow
    public abstract boolean isJammed(IBlockAccess blockAccess, int i, int j, int k);

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setIsTile(int iBlockID, CallbackInfo ci) {
        this.isBlockContainer = true;
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Inject(method = "isJammed", at = @At("HEAD"))
    private void tetstest(IBlockAccess blockAccess, int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
//        System.out.println("is jammed: " + ((blockAccess.getBlockMetadata(i, j, k) & 8) > 0));
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileScrewPump();
    }


//    @Redirect(method = "isPumpingWater", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I"))
//    private int btb$modifyIfCanPump(World world, int i, int j, int k) {
//        BlockPos tankPos = new BlockPos(i, j, k);
//        tankPos.addFacingAsOffset(ForgeDirection.OPPOSITES[this.getFacing(world, i, j, k)]);
//        if (world.getBlockTileEntity(tankPos.x, tankPos.y, tankPos.z) instanceof TileScrewPump tsp && tsp.TANK.getFluidAmount() > btb$toDrain) {
//            System.out.println("modifying if it can pump");
//            return Block.waterMoving.blockID;
//        }
//        return world.getBlockId(i, j, k);
//    }

//    @Inject(method = "updateTick", at = @At(value = "INVOKE", target = "Lbtw/block/blocks/ScrewPumpBlock;onNeighborChangeShortPumpSourceCheck(Lnet/minecraft/src/World;III)Z"), cancellable = true)
//    private void btb$startPumping22(World world, int i, int j, int k, Random random, CallbackInfo ci) {
//        System.out.println("printing onNeighborChangeShortPumpSourceCheck");
//        if (world.getBlockTileEntity(i, j, k) instanceof TileScrewPump tsp && tsp.TANK.getFluidAmount() > btb$toDrain) {
//            System.out.println("Draining ");
//            tsp.TANK.drain(btb$toDrain, true);
//            System.out.println("Drained ");
//            int iTargetHeight = world.getBlockMetadata(i, j + 1, k);
//            System.out.println("Setting block and meta ");
//            if (iTargetHeight > 1 && iTargetHeight < 8) {
//                world.setBlockAndMetadata(i, j + 1, k, Block.waterMoving.blockID, iTargetHeight - 1);
////                world.setBlockAndMetadataWithNotify(i, j + 1, k, Block.waterMoving.blockID, iTargetHeight - 1);
////                world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
//                System.out.println("Set sucessfully!");
//            }
//            ci.cancel();
//            return;
//
//        }
//    }

//    @Inject(method = "updateTick", at = @At("RETURN"))
//    private void testTesttestRETURN(World world, int i, int j, int k, Random random, CallbackInfo ci) {
//        System.out.println("Got to the end of updateTick");
//    }

//    @Redirect(method = "updateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;scheduleBlockUpdate(IIIII)V"))
//    private void injectIntoTest(World instance, int par1, int par2, int par3, int par4, int par5) {
//        System.out.println("redirecting scheduleBlockUpdate!");
//        instance.scheduleBlockUpdateWithPriority(par1, par2, par3, par4, par5, 10);
//    }

//    @Redirect(method = "updateTick", at = @At(value = "INVOKE", target = "Lbtw/block/blocks/ScrewPumpBlock;onNeighborChangeShortPumpSourceCheck(Lnet/minecraft/src/World;III)Z"))
//    private boolean btb$startPumping22(ScrewPumpBlock instance, World world, int i, int j, int k) {
//        System.out.println("printing onNeighborChangeShortPumpSourceCheck");
//        if (world.getBlockTileEntity(i, j, k) instanceof TileScrewPump tsp && tsp.TANK.getFluidAmount() > btb$toDrain) {
//            System.out.println("Draining ");
//            tsp.TANK.drain(btb$toDrain, true);
////            System.out.println("Drained ");
////            int iTargetHeight = world.getBlockMetadata(i, j + 1, k);
////            System.out.println("Setting block and meta ");
////            if (iTargetHeight > 1 && iTargetHeight < 8) {
////                world.setBlockAndMetadata(i, j + 1, k, Block.waterMoving.blockID, iTargetHeight - 1);
//////                world.setBlockAndMetadataWithNotify(i, j + 1, k, Block.waterMoving.blockID, iTargetHeight - 1);
//////                world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
////                System.out.println("Set sucessfully!");
////            }
//            System.out.println("Returning true ");
//            return true;
//
//        }
//        else
//            return onNeighborChangeShortPumpSourceCheck(world, i, j, k);
//    }

//    @Redirect(method = "updateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I"))
//    private int btb$startPumping1(World world, int i, int j, int k) {
//        BlockPos tankPos = new BlockPos(i, j, k);
//        tankPos.addFacingAsOffset(ForgeDirection.OPPOSITES[this.getFacing(world, i, j, k)]);
//
//        if (world.getBlockTileEntity(tankPos.x, tankPos.y, tankPos.z) instanceof TileScrewPump tsp && tsp.TANK.getFluidAmount() > btb$toDrain) {
//            tsp.TANK.drain(btb$toDrain, true);
//            System.out.println("onNeighborChangeShortPumpSourceCheck SUCCEESS, setting ");
//            return Block.waterMoving.blockID;
//        }
//        System.out.println("onNeighborChangeShortPumpSourceCheck failed");
//        return world.getBlockId(i, j, k);
//    }

//    @Redirect(method = "startPumpSourceCheck", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I"))
//    private int btb$startPumping2(World world, int i, int j, int k) {
//        BlockPos tankPos = new BlockPos(i, j, k);
//        tankPos.addFacingAsOffset(ForgeDirection.OPPOSITES[this.getFacing(world, i, j, k)]);
//        if (world.getBlockTileEntity(tankPos.x, tankPos.y, tankPos.z) instanceof TileScrewPump tsp && tsp.TANK.getFluidAmount() > btb$toDrain) {
//            tsp.TANK.drain(btb$toDrain, true);
//            //fake not requiring any water, then do update ourselves
//            world.setBlockAndMetadataWithNotify(tankPos.x, tankPos.y + 1, tankPos.z, Block.waterMoving.blockID, 7);
//            world.scheduleBlockUpdate(tankPos.x, tankPos.y, tankPos.z, this.blockID, this.tickRate(world));
//            return 0;
//        }
//        return world.getBlockId(i, j, k);
//    }

//    @Redirect(method = "updateTick", at = @At(value = "INVOKE", target = "Lbtw/block/blocks/ScrewPumpBlock;startPumpSourceCheck(Lnet/minecraft/src/World;III)Z"))
//    private boolean btb$fixJamming(ScrewPumpBlock instance, World world, int i, int j, int k) {
//        System.out.println("Redirecting startPumpSourceCheck");
//        if (world.getBlockTileEntity(i, j, k) instanceof TileScrewPump tsp && tsp.TANK.getFluidAmount() > btb$toDrain) {
//            world.setBlockAndMetadataWithNotify(i, j + 1, k, Block.waterMoving.blockID, 7);
//            world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
//            System.out.println("Success!");
//            return true;
//        }
//        return this.startPumpSourceCheck(world, i, j, k);
//    }

//    @Inject(method = "updateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I", ordinal = 0))
//    private void btb$fixJamming(World world, int i, int j, int k, Random random, CallbackInfo ci) {
//        BlockPos tankPos = new BlockPos(i, j, k);
//        tankPos.addFacingAsOffset(ForgeDirection.OPPOSITES[this.getFacing(world, i, j, k)]);
//
//        if (world.getBlockTileEntity(tankPos.x, tankPos.y, tankPos.z) instanceof TileScrewPump tsp && tsp.TANK.getFluidAmount() < btb$toDrain) {
//            this.setIsJammed(world, tankPos.x, tankPos.y, tankPos.z, false);
//        }
//    }

//    @Redirect(method = "updateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I", ordinal = 1))
//    private int btb$startPumping(World instance, int var7, int var8, int var4) {
//        return 0;
//    }

//    @Inject(method = "updateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I", ordinal = 2))
//    private void btb$startPumping2(World world, int i, int j, int k, Random random, CallbackInfo ci) {
//        BlockPos tankPos = new BlockPos(i, j, k);
//        tankPos.addFacingAsOffset(ForgeDirection.OPPOSITES[this.getFacing(world, i, j, k)]);
//

//        if (world.getBlockTileEntity(tankPos.x, tankPos.y, tankPos.z) instanceof TileScrewPump tsp && tsp.TANK.getFluidAmount() > btb$toDrain) {
//            int iTargetBlockID = world.getBlockId(i, j + 1, k);
//            if (Block.blocksList[iTargetBlockID] != null) {
//                Block.blocksList[iTargetBlockID].onNeighborBlockChange(world, i, j + 1, k, this.blockID);
//            }
//        }
//    }
    @Inject(method = "isPumpingWater", at = @At(value = "INVOKE", target = "Lbtw/world/util/BlockPos;addFacingAsOffset(I)V"), cancellable = true)
    private void btb$modifyIfItCanPump(World world, int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
        if (world.getBlockTileEntity(i, j, k) instanceof TileScrewPump tsp && tsp.TANK.getFluidAmount() > btb$toDrain) {
            System.out.println("Successfully modifying if it can pump");
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isSourceToFluidBlockAtFacing", at = @At(value = "HEAD"), cancellable = true)
    private void btb$modifyIsSourceToFluid(World world, int i, int j, int k, int iFacing, CallbackInfoReturnable<Integer> cir) {
        if (world.getBlockTileEntity(i, j, k) instanceof TileScrewPump tsp && tsp.TANK.getFluidAmount() > btb$toDrain) {

            int iSourceHeight = 0;
            int iTargetHeight = world.getBlockMetadata(i, j + 1, k);
            if (iTargetHeight > 0 && iTargetHeight < 8) {
                iSourceHeight = iTargetHeight - 1;
            }
            System.out.println("isSourceToFluidBlockAtFacing is being set to " + iSourceHeight);
            cir.setReturnValue(iSourceHeight);
//            cir.setReturnValue(-1);
        }
    }
    //todotransport aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    /**
     * @author Bagel
     * @reason I am lazy :(
     */
    @Overwrite
    public void updateTick(World world, int blockX, int blockY, int blockZ, Random random) {
        System.out.println("Head of update tick");
        boolean isOn;
        boolean isReceivingPower;
        TileScrewPump tsp = null;
        if (world.getBlockTileEntity(blockX, blockY, blockZ) instanceof TileScrewPump pump) {
            tsp = pump;
            System.out.println("Tile entity is valid!");
        }
        if (this.isJammed(world, blockX, blockY, blockZ)) {
            System.out.println("Block is jammed");
            BlockPos sourcePos = new BlockPos(blockX, blockY, blockZ);
            sourcePos.addFacingAsOffset(this.getFacing(world, blockX, blockY, blockZ));
            int iSourceBlockID = world.getBlockId(sourcePos.x, sourcePos.y, sourcePos.z);
            if ((iSourceBlockID != Block.waterMoving.blockID && iSourceBlockID != Block.waterStill.blockID) || (tsp != null && tsp.TANK.isEmpty())) {
                this.setIsJammed(world, blockX, blockY, blockZ, false);
            }
        }
        if ((isReceivingPower = this.isInputtingMechanicalPower(world, blockX, blockY, blockZ)) != (isOn = this.isMechanicalOn(world, blockX, blockY, blockZ))) {
            System.out.println("Is getting power");
            this.setMechanicalOn(world, blockX, blockY, blockZ, isReceivingPower);
            world.markBlockForUpdate(blockX, blockY, blockZ);
            if (this.isPumpingWater(world, blockX, blockY, blockZ)) {
                System.out.println("Its pumping water so update");
                world.scheduleBlockUpdate(blockX, blockY, blockZ, this.blockID, this.tickRate(world));
            }
            if (!isReceivingPower && this.isJammed(world, blockX, blockY, blockZ)) {
                System.out.println("Set not pumping");
                this.setIsJammed(world, blockX, blockY, blockZ, false);
            }
        } else if (isOn) {
            if (this.isPumpingWater(world, blockX, blockY, blockZ)) {
                boolean bSourceValidated = false;
                int blockIDAbove = world.getBlockId(blockX, blockY + 1, blockZ);
                if (blockIDAbove == Block.waterMoving.blockID || blockIDAbove == Block.waterStill.blockID) {
                    if (this.btb$pumpSourceCheck(world, blockX, blockY, blockZ, 4)) {
//                        if (tsp != null && tsp.TANK.getFluid() != null)
//                                tsp.TANK.drain(btb$toDrain, true);
                        int iTargetHeight = world.getBlockMetadata(blockX, blockY + 1, blockZ);
                        if (iTargetHeight > 1 && iTargetHeight < 8) {
                            world.setBlockAndMetadataWithNotify(blockX, blockY + 1, blockZ, Block.waterMoving.blockID, iTargetHeight - 1);
                            world.scheduleBlockUpdate(blockX, blockY, blockZ, this.blockID, this.tickRate(world));
                        }
                    } else {
                        this.setIsJammed(world, blockX, blockY, blockZ, true);
                    }
                } else if (world.isAirBlock(blockX, blockY + 1, blockZ)) {
                    System.out.println("Block above is air");
                    if (this.btb$pumpSourceCheck(world, blockX, blockY, blockZ, 128)) {
                        System.out.println("Pump source check succeeded, updating block above with water");
                        world.setBlockAndMetadataWithNotify(blockX, blockY + 1, blockZ, Block.waterMoving.blockID, 7);
                        world.scheduleBlockUpdate(blockX, blockY, blockZ, this.blockID, this.tickRate(world));
                    } else {
                        System.out.println("Pump source check failed, setting jammed");
                        this.setIsJammed(world, blockX, blockY, blockZ, true);
                    }
                }
            } else {
                int iTargetBlockID = world.getBlockId(blockX, blockY + 1, blockZ);
                if (iTargetBlockID == Block.waterMoving.blockID || iTargetBlockID == Block.waterStill.blockID) {
                    Block.blocksList[iTargetBlockID].onNeighborBlockChange(world, blockX, blockY + 1, blockZ, this.blockID);
                }
            }
        }
    }

    private boolean btb$pumpSourceCheck(World world, int i, int j, int k, int distance) {
        if (world.getBlockTileEntity(i, j, k) instanceof TileScrewPump tsp && tsp.TANK.getFluidAmount() > btb$toDrain) {
            return true;
        }
        BlockPos sourcePos = new BlockPos(i, j, k);
        sourcePos.addFacingAsOffset(this.getFacing(world, i, j, k));
        int iSourceBlockID = world.getBlockId(sourcePos.x, sourcePos.y, sourcePos.z);
        if (iSourceBlockID == Block.waterMoving.blockID || iSourceBlockID == Block.waterStill.blockID) {
            return MiscUtils.doesWaterHaveValidSource(world, sourcePos.x, sourcePos.y, sourcePos.z, distance);
        }
        return false;
    }

    @Override
    public void breakBlock(World world, int i, int j, int k, int l, int m) {
        super.breakBlock(world, i, j, k, l, m);
        world.removeBlockTileEntity(i, j, k);
    }

//    @Override
//    public boolean onBlockEventReceived(World world, int i, int j, int k, int l, int m) {
//        super.onBlockEventReceived(world, i, j, k, l, m);
//        TileEntity var7 = world.getBlockTileEntity(i, j, k);
//        return var7 != null ? var7.receiveClientEvent(l, m) : false;
//    }
}
