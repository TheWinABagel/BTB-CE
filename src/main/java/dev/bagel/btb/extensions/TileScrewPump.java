package dev.bagel.btb.extensions;

import buildcraft.core.fluids.Tank;
import net.minecraft.src.Block;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileScrewPump extends TileEntity implements IFluidHandler {
    public final Tank TANK = new Tank("ScrewPump", FluidContainerRegistry.BUCKET_VOLUME, this);
    public TileScrewPump() {}

    @Override
    public int fill(ForgeDirection forgeDirection, FluidStack resource, boolean doFill) {
        if (resource == null) {
            return 0;
        }
        if (resource.getFluid() != FluidRegistry.WATER) {
            return 0;
        }

        resource = resource.copy();
        int totalUsed = 0;

        FluidStack liquid = TANK.getFluid();
        if (liquid != null && liquid.amount >= FluidContainerRegistry.BUCKET_VOLUME) {
            return 0;
        }

        while (resource.amount > 0) {
            int used = TANK.fill(resource, doFill);
            resource.amount -= used;
            if (used > 0) {
                this.updateEntity();
            }

            totalUsed += used;
        }
        return totalUsed;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (resource == null)
            return null;
        if (!resource.isFluidEqual(TANK.getFluid()))
            return null;
        return drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection forgeDirection, int maxEmpty, boolean doDrain) {
        return TANK.drain(maxEmpty, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection forgeDirection, Fluid fluid) {
        return true;
    }

    @Override
    public boolean canDrain(ForgeDirection forgeDirection, Fluid fluid) {
        return fluid.equals(FluidRegistry.WATER);
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection forgeDirection) {
        return new FluidTankInfo[]{TANK.getInfo()};
    }

    @Override
    public Block getBlockType() {
        return super.getBlockType();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        TANK.readFromNBT(nbt);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        TANK.writeToNBT(nbt);
    }
}
