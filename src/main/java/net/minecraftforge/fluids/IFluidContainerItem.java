package net.minecraftforge.fluids;

import net.minecraft.src.ItemStack;

public interface IFluidContainerItem {
   FluidStack getFluid(ItemStack itemStack);

   int getCapacity(ItemStack itemStack);

   int fill(ItemStack itemStack, FluidStack fluidStack, boolean bl);

   FluidStack drain(ItemStack itemStack, int i, boolean bl);
}
