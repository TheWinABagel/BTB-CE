package net.minecraftforge.fluids;

import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

import java.util.Locale;

public class FluidStack {
   public int fluidID;
   public int amount;
   public NBTTagCompound tag;

   public FluidStack(Fluid fluid, int amount) {
      this.fluidID = fluid.getID();
      this.amount = amount;
   }

   public FluidStack(int fluidID, int amount) {
      this.fluidID = fluidID;
      this.amount = amount;
   }

   public FluidStack(int fluidID, int amount, NBTTagCompound nbt) {
      this(fluidID, amount);
      if (nbt != null) {
         this.tag = (NBTTagCompound)nbt.copy();
      }

   }

   public FluidStack(FluidStack stack, int amount) {
      this(stack.fluidID, amount, stack.tag);
   }

   public static FluidStack loadFluidStackFromNBT(NBTTagCompound nbt) {
      if (nbt == null) {
         return null;
      } else {
         String fluidName = nbt.getString("FluidName");
         if (fluidName == null) {
            fluidName = nbt.hasKey("LiquidName") ? nbt.getString("LiquidName").toLowerCase(Locale.ENGLISH) : null;
            fluidName = Fluid.convertLegacyName(fluidName);
         }

         if (fluidName != null && FluidRegistry.getFluid(fluidName) != null) {
            FluidStack stack = new FluidStack(FluidRegistry.getFluidID(fluidName), nbt.getInteger("Amount"));
            if (nbt.hasKey("Tag")) {
               stack.tag = nbt.getCompoundTag("Tag");
            } else if (nbt.hasKey("extra")) {
               stack.tag = nbt.getCompoundTag("extra");
            }

            return stack;
         } else {
            return null;
         }
      }
   }

   /**
    * Save the entity to NBT (calls an abstract helper method to write extra data)
    */
   public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
      nbt.setString("FluidName", FluidRegistry.getFluidName(this.fluidID));
      nbt.setInteger("Amount", this.amount);
      if (this.tag != null) {
         nbt.setTag("Tag", this.tag);
      }

      return nbt;
   }

   public final Fluid getFluid() {
      return FluidRegistry.getFluid(this.fluidID);
   }

   /**
    * Returns a copy of the bounding box.
    */
   public FluidStack copy() {
      return new FluidStack(this.fluidID, this.amount, this.tag);
   }

   public boolean isFluidEqual(FluidStack other) {
      return other != null && this.fluidID == other.fluidID && this.isFluidStackTagEqual(other);
   }

   private boolean isFluidStackTagEqual(FluidStack other) {
      return this.tag == null ? other.tag == null : (other.tag == null ? false : this.tag.equals(other.tag));
   }

   public static boolean areFluidStackTagsEqual(FluidStack stack1, FluidStack stack2) {
      return stack1 == null && stack2 == null ? true : (stack1 != null && stack2 != null ? stack1.isFluidStackTagEqual(stack2) : false);
   }

   public boolean containsFluid(FluidStack other) {
      return this.isFluidEqual(other) && this.amount >= other.amount;
   }

   public boolean isFluidStackIdentical(FluidStack other) {
      return this.isFluidEqual(other) && this.amount == other.amount;
   }

   public boolean isFluidEqual(ItemStack other) {
      if (other == null) {
         return false;
      } else {
         return other.getItem() instanceof IFluidContainerItem ? this.isFluidEqual(((IFluidContainerItem)other.getItem()).getFluid(other)) : this.isFluidEqual(FluidContainerRegistry.getFluidForFilledItem(other));
      }
   }

   public final int hashCode() {
      return this.fluidID;
   }

   public final boolean equals(Object o) {
      return !(o instanceof FluidStack) ? false : this.isFluidEqual((FluidStack)o);
   }
}
