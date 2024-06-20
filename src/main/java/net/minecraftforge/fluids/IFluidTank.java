package net.minecraftforge.fluids;

public interface IFluidTank {
   FluidStack getFluid();

   int getFluidAmount();

   int getCapacity();

   FluidTankInfo getInfo();

   int fill(FluidStack fluidStack, boolean bl);

   FluidStack drain(int i, boolean bl);
}
