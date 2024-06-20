package net.minecraftforge.fluids;

public final class FluidTankInfo {
   public final FluidStack fluid;
   /**
    * the maximum amount of elements in the hash (probably 3/4 the size due to meh hashing function)
    */
   public final int capacity;

   public FluidTankInfo(FluidStack fluid, int capacity) {
      this.fluid = fluid;
      this.capacity = capacity;
   }

   public FluidTankInfo(IFluidTank tank) {
      this.fluid = tank.getFluid();
      this.capacity = tank.getCapacity();
   }
}
