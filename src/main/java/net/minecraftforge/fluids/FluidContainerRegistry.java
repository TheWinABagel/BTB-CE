package net.minecraftforge.fluids;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

import java.util.*;

public abstract class FluidContainerRegistry {
   private static Map<List, FluidContainerData> containerFluidMap = new HashMap();
   private static Map<List, FluidContainerData> filledContainerMap = new HashMap();
   private static Set<List> emptyContainers = new HashSet();
   public static final int BUCKET_VOLUME = 1000;
   public static final ItemStack EMPTY_BUCKET;
   public static final ItemStack EMPTY_BOTTLE;
   private static final ItemStack NULL_EMPTYCONTAINER;

   private FluidContainerRegistry() {
   }

   public static boolean registerFluidContainer(FluidStack stack, ItemStack filledContainer, ItemStack emptyContainer) {
      return registerFluidContainer(new FluidContainerData(stack, filledContainer, emptyContainer));
   }

   public static boolean registerFluidContainer(Fluid fluid, ItemStack filledContainer, ItemStack emptyContainer) {
      if (!FluidRegistry.isFluidRegistered(fluid)) {
         FluidRegistry.registerFluid(fluid);
      }

      return registerFluidContainer(new FluidStack(fluid, 1000), filledContainer, emptyContainer);
   }

   public static boolean registerFluidContainer(FluidStack stack, ItemStack filledContainer) {
      return registerFluidContainer(new FluidContainerData(stack, filledContainer, (ItemStack)null, true));
   }

   public static boolean registerFluidContainer(Fluid fluid, ItemStack filledContainer) {
      if (!FluidRegistry.isFluidRegistered(fluid)) {
         FluidRegistry.registerFluid(fluid);
      }

      return registerFluidContainer(new FluidStack(fluid, 1000), filledContainer);
   }

   public static boolean registerFluidContainer(FluidContainerData data) {
      if (isFilledContainer(data.filledContainer)) {
         return false;
      } else {
         containerFluidMap.put(Arrays.asList(data.filledContainer.itemID, data.filledContainer.getItemDamage()), data);
         if (data.emptyContainer != null && data.emptyContainer != NULL_EMPTYCONTAINER) {
            filledContainerMap.put(Arrays.asList(data.emptyContainer.itemID, data.emptyContainer.getItemDamage(), data.fluid.fluidID), data);
            emptyContainers.add(Arrays.asList(data.emptyContainer.itemID, data.emptyContainer.getItemDamage()));
         }

//         MinecraftForge.EVENT_BUS.post(new FluidContainerRegisterEvent(data));
         return true;
      }
   }

   public static FluidStack getFluidForFilledItem(ItemStack container) {
      if (container == null) {
         return null;
      } else {
         FluidContainerData data = (FluidContainerData)containerFluidMap.get(Arrays.asList(container.itemID, container.getItemDamage()));
         return data == null ? null : data.fluid.copy();
      }
   }

   public static ItemStack fillFluidContainer(FluidStack fluid, ItemStack container) {
      if (container != null && fluid != null) {
         FluidContainerData data = (FluidContainerData)filledContainerMap.get(Arrays.asList(container.itemID, container.getItemDamage(), fluid.fluidID));
         return data != null && fluid.amount >= data.fluid.amount ? data.filledContainer.copy() : null;
      } else {
         return null;
      }
   }

   public static boolean containsFluid(ItemStack container, FluidStack fluid) {
      if (container != null && fluid != null) {
         FluidContainerData data = (FluidContainerData)filledContainerMap.get(Arrays.asList(container.itemID, container.getItemDamage(), fluid.fluidID));
         return data == null ? false : data.fluid.isFluidEqual(fluid);
      } else {
         return false;
      }
   }

   public static boolean isBucket(ItemStack container) {
      if (container == null) {
         return false;
      } else if (container.isItemEqual(EMPTY_BUCKET)) {
         return true;
      } else {
         FluidContainerData data = (FluidContainerData)containerFluidMap.get(Arrays.asList(container.itemID, container.getItemDamage()));
         return data != null && data.emptyContainer.isItemEqual(EMPTY_BUCKET);
      }
   }

   public static boolean isContainer(ItemStack container) {
      return isEmptyContainer(container) || isFilledContainer(container);
   }

   public static boolean isEmptyContainer(ItemStack container) {
      return container != null && emptyContainers.contains(Arrays.asList(container.itemID, container.getItemDamage()));
   }

   public static boolean isFilledContainer(ItemStack container) {
      return container != null && getFluidForFilledItem(container) != null;
   }

   public static FluidContainerData[] getRegisteredFluidContainerData() {
      return (FluidContainerData[])containerFluidMap.values().toArray(new FluidContainerData[containerFluidMap.size()]);
   }

   static {
      EMPTY_BUCKET = new ItemStack(Item.bucketEmpty);
      EMPTY_BOTTLE = new ItemStack(Item.glassBottle);
      NULL_EMPTYCONTAINER = new ItemStack(Item.bucketEmpty);
      registerFluidContainer(FluidRegistry.WATER, new ItemStack(Item.bucketWater), EMPTY_BUCKET);
      registerFluidContainer(FluidRegistry.LAVA, new ItemStack(Item.bucketLava), EMPTY_BUCKET);
      registerFluidContainer(FluidRegistry.WATER, new ItemStack(Item.potion), EMPTY_BOTTLE);
   }

/*   public static class FluidContainerRegisterEvent extends Event {
      *//**
       * The string value for the tag (cannot be empty).
       *//*
      public final FluidContainerData data;

      public FluidContainerRegisterEvent(FluidContainerData data) {
         this.data = data.copy();
      }
   }*/

   public static class FluidContainerData {
      public final FluidStack fluid;
      public final ItemStack filledContainer;
      public final ItemStack emptyContainer;

      public FluidContainerData(FluidStack stack, ItemStack filledContainer, ItemStack emptyContainer) {
         this(stack, filledContainer, emptyContainer, false);
      }

      public FluidContainerData(FluidStack stack, ItemStack filledContainer, ItemStack emptyContainer, boolean nullEmpty) {
         this.fluid = stack;
         this.filledContainer = filledContainer;
         this.emptyContainer = emptyContainer == null ? FluidContainerRegistry.NULL_EMPTYCONTAINER : emptyContainer;
         if (stack == null || filledContainer == null || emptyContainer == null && !nullEmpty) {
            throw new RuntimeException("Invalid FluidContainerData - a parameter was null.");
         }
      }

      /**
       * Returns a copy of the bounding box.
       */
      public FluidContainerData copy() {
         return new FluidContainerData(this.fluid, this.filledContainer, this.emptyContainer, true);
      }
   }
}
