package net.minecraftforge.fluids;

import net.minecraftforge.common.ForgeDirection;

public interface IFluidHandler {
   int fill(ForgeDirection forgeDirection, FluidStack fluidStack, boolean bl);

   FluidStack drain(ForgeDirection forgeDirection, FluidStack fluidStack, boolean bl);

   FluidStack drain(ForgeDirection forgeDirection, int i, boolean bl);

   boolean canFill(ForgeDirection forgeDirection, Fluid fluid);

   boolean canDrain(ForgeDirection forgeDirection, Fluid fluid);

   FluidTankInfo[] getTankInfo(ForgeDirection forgeDirection);
}
