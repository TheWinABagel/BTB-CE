package net.minecraftforge.fluids;

import net.minecraft.src.World;

public interface IFluidBlock {
   Fluid getFluid();

   FluidStack drain(World world, int i, int j, int k, boolean bl);

   boolean canDrain(World world, int i, int j, int k);

   float getFilledPercentage(World world, int i, int j, int k);
}
