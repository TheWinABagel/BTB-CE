package net.minecraftforge.fluids;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

public class FluidTank implements IFluidTank {
   protected FluidStack fluid;
   /**
    * the maximum amount of elements in the hash (probably 3/4 the size due to meh hashing function)
    */
   protected int capacity;
   protected TileEntity tile;

   public FluidTank(int capacity) {
      this((FluidStack)null, capacity);
   }

   public FluidTank(FluidStack stack, int capacity) {
      this.fluid = stack;
      this.capacity = capacity;
   }

   public FluidTank(Fluid fluid, int amount, int capacity) {
      this(new FluidStack(fluid, amount), capacity);
   }

   /**
    * Reads from the given tag list and fills the slots in the inventory with the correct items.
    */
   public FluidTank readFromNBT(NBTTagCompound nbt) {
      if (!nbt.hasKey("Empty")) {
         FluidStack fluid = FluidStack.loadFluidStackFromNBT(nbt);
         if (fluid != null) {
            this.setFluid(fluid);
         }
      }

      return this;
   }

   /**
    * Save the entity to NBT (calls an abstract helper method to write extra data)
    */
   public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
      if (this.fluid != null) {
         this.fluid.writeToNBT(nbt);
      } else {
         nbt.setString("Empty", "");
      }

      return nbt;
   }

   public void setFluid(FluidStack fluid) {
      this.fluid = fluid;
   }

   public void setCapacity(int capacity) {
      this.capacity = capacity;
   }

   public FluidStack getFluid() {
      return this.fluid;
   }

   public int getFluidAmount() {
      return this.fluid == null ? 0 : this.fluid.amount;
   }

   public int getCapacity() {
      return this.capacity;
   }

   public FluidTankInfo getInfo() {
      return new FluidTankInfo(this);
   }

   public int fill(FluidStack resource, boolean doFill) {
      if (resource == null) {
         return 0;
      } else if (!doFill) {
         if (this.fluid == null) {
            return Math.min(this.capacity, resource.amount);
         } else {
            return !this.fluid.isFluidEqual(resource) ? 0 : Math.min(this.capacity - this.fluid.amount, resource.amount);
         }
      } else if (this.fluid == null) {
         this.fluid = new FluidStack(resource, Math.min(this.capacity, resource.amount));
         if (this.tile != null) {
            FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(this.fluid, this.tile.worldObj, this.tile.xCoord, this.tile.yCoord, this.tile.zCoord, this));
         }

         return this.fluid.amount;
      } else if (!this.fluid.isFluidEqual(resource)) {
         return 0;
      } else {
         int filled = this.capacity - this.fluid.amount;
         if (resource.amount < filled) {
            FluidStack var10000 = this.fluid;
            var10000.amount += resource.amount;
            filled = resource.amount;
         } else {
            this.fluid.amount = this.capacity;
         }

         if (this.tile != null) {
            FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(this.fluid, this.tile.worldObj, this.tile.xCoord, this.tile.yCoord, this.tile.zCoord, this));
         }

         return filled;
      }
   }

   public FluidStack drain(int maxDrain, boolean doDrain) {
      if (this.fluid == null) {
         return null;
      } else {
         int drained = maxDrain;
         if (this.fluid.amount < maxDrain) {
            drained = this.fluid.amount;
         }

         FluidStack stack = new FluidStack(this.fluid, drained);
         if (doDrain) {
            FluidStack var10000 = this.fluid;
            var10000.amount -= drained;
            if (this.fluid.amount <= 0) {
               this.fluid = null;
            }

            if (this.tile != null) {
               FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(this.fluid, this.tile.worldObj, this.tile.xCoord, this.tile.yCoord, this.tile.zCoord, this));
            }
         }

         return stack;
      }
   }
}
