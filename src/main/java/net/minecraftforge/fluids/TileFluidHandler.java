package net.minecraftforge.fluids;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileFluidHandler extends TileEntity implements IFluidHandler {
   protected FluidTank tank = new FluidTank(1000);

   /**
    * Reads from the given tag list and fills the slots in the inventory with the correct items.
    */
   public void readFromNBT(NBTTagCompound tag) {
      super.readFromNBT(tag);
      this.tank.writeToNBT(tag);
   }

   /**
    * Save the entity to NBT (calls an abstract helper method to write extra data)
    */
   public void writeToNBT(NBTTagCompound tag) {
      super.writeToNBT(tag);
      this.tank.readFromNBT(tag);
   }

   public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
      return this.tank.fill(resource, doFill);
   }

   public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
      return resource != null && resource.isFluidEqual(this.tank.getFluid()) ? this.tank.drain(resource.amount, doDrain) : null;
   }

   public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
      return this.tank.drain(maxDrain, doDrain);
   }

   public boolean canFill(ForgeDirection from, Fluid fluid) {
      return true;
   }

   public boolean canDrain(ForgeDirection from, Fluid fluid) {
      return true;
   }

   public FluidTankInfo[] getTankInfo(ForgeDirection from) {
      return new FluidTankInfo[]{this.tank.getInfo()};
   }
}
