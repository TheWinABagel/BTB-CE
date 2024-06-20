package net.minecraftforge.fluids;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

public class ItemFluidContainer extends Item implements IFluidContainerItem {
   /**
    * the maximum amount of elements in the hash (probably 3/4 the size due to meh hashing function)
    */
   protected int capacity;

   public ItemFluidContainer(int itemID) {
      super(itemID);
   }

   public ItemFluidContainer(int itemID, int capacity) {
      super(itemID);
      this.capacity = capacity;
   }

   public ItemFluidContainer setCapacity(int capacity) {
      this.capacity = capacity;
      return this;
   }

   public FluidStack getFluid(ItemStack container) {
      return container.stackTagCompound != null && container.stackTagCompound.hasKey("Fluid") ? FluidStack.loadFluidStackFromNBT(container.stackTagCompound.getCompoundTag("Fluid")) : null;
   }

   public int getCapacity(ItemStack container) {
      return this.capacity;
   }

   public int fill(ItemStack container, FluidStack resource, boolean doFill) {
      if (resource == null) {
         return 0;
      } else if (!doFill) {
         if (container.stackTagCompound != null && container.stackTagCompound.hasKey("Fluid")) {
            FluidStack stack = FluidStack.loadFluidStackFromNBT(container.stackTagCompound.getCompoundTag("Fluid"));
            if (stack == null) {
               return Math.min(this.capacity, resource.amount);
            } else {
               return !stack.isFluidEqual(resource) ? 0 : Math.min(this.capacity - stack.amount, resource.amount);
            }
         } else {
            return Math.min(this.capacity, resource.amount);
         }
      } else {
         if (container.stackTagCompound == null) {
            container.stackTagCompound = new NBTTagCompound();
         }

         NBTTagCompound fluidTag;
         if (!container.stackTagCompound.hasKey("Fluid")) {
            fluidTag = resource.writeToNBT(new NBTTagCompound());
            if (this.capacity < resource.amount) {
               fluidTag.setInteger("Amount", this.capacity);
               container.stackTagCompound.setTag("Fluid", fluidTag);
               return this.capacity;
            } else {
               container.stackTagCompound.setTag("Fluid", fluidTag);
               return resource.amount;
            }
         } else {
            fluidTag = container.stackTagCompound.getCompoundTag("Fluid");
            FluidStack stack = FluidStack.loadFluidStackFromNBT(fluidTag);
            if (!stack.isFluidEqual(resource)) {
               return 0;
            } else {
               int filled = this.capacity - stack.amount;
               if (resource.amount < filled) {
                  stack.amount += resource.amount;
                  filled = resource.amount;
               } else {
                  stack.amount = this.capacity;
               }

               container.stackTagCompound.setTag("Fluid", stack.writeToNBT(fluidTag));
               return filled;
            }
         }
      }
   }

   public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
      if (container.stackTagCompound != null && container.stackTagCompound.hasKey("Fluid") && maxDrain != 0) {
         FluidStack stack = FluidStack.loadFluidStackFromNBT(container.stackTagCompound.getCompoundTag("Fluid"));
         if (stack == null) {
            return null;
         } else {
            int drained = Math.min(stack.amount, maxDrain);
            if (doDrain) {
               if (maxDrain >= stack.amount) {
                  container.stackTagCompound.removeTag("Fluid");
                  if (container.stackTagCompound.hasNoTags()) {
                     container.stackTagCompound = null;
                  }

                  return stack;
               }

               NBTTagCompound fluidTag = container.stackTagCompound.getCompoundTag("Fluid");
               fluidTag.setInteger("Amount", fluidTag.getInteger("Amount") - maxDrain);
               container.stackTagCompound.setTag("Fluid", fluidTag);
            }

            stack.amount = drained;
            return stack;
         }
      } else {
         return null;
      }
   }
}
