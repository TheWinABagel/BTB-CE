package net.minecraftforge.fluids;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import net.minecraft.src.Block;
import net.minecraft.src.StatCollector;

import java.util.HashMap;
import java.util.Map;

public abstract class FluidRegistry {
   static int maxID = 0;
   static HashMap<String, Fluid> fluids = new HashMap<>();
   static BiMap<String, Integer> fluidIDs = HashBiMap.create();
   static BiMap<Block, Fluid> fluidBlocks;
   public static final Fluid WATER;
   public static final Fluid LAVA;
   public static int renderIdFluid;

   private FluidRegistry() {
   }

   static void initFluidIDs(BiMap<String, Integer> newfluidIDs) {
      maxID = newfluidIDs.size();
      fluidIDs.clear();
      fluidIDs.putAll(newfluidIDs);
   }

   public static boolean registerFluid(Fluid fluid) {
      if (fluidIDs.containsKey(fluid.getName())) {
         return false;
      } else {
         fluids.put(fluid.getName(), fluid);
         fluidIDs.put(fluid.getName(), ++maxID);
         return true;
      }
   }

   public static boolean isFluidRegistered(Fluid fluid) {
      return fluidIDs.containsKey(fluid.getName());
   }

   public static boolean isFluidRegistered(String fluidName) {
      return fluidIDs.containsKey(fluidName);
   }

   public static Fluid getFluid(String fluidName) {
      return fluids.get(fluidName);
   }

   public static Fluid getFluid(int fluidID) {
      return (Fluid)fluids.get(getFluidName(fluidID));
   }

   public static String getFluidName(int fluidID) {
      return (String)fluidIDs.inverse().get(fluidID);
   }

   public static String getFluidName(FluidStack stack) {
      return getFluidName(stack.fluidID);
   }

   public static int getFluidID(String fluidName) {
      return (Integer)fluidIDs.get(fluidName);
   }

   public static FluidStack getFluidStack(String fluidName, int amount) {
      return !fluidIDs.containsKey(fluidName) ? null : new FluidStack(getFluidID(fluidName), amount);
   }

   public static Map<String, Fluid> getRegisteredFluids() {
      return ImmutableMap.copyOf(fluids);
   }

   public static Map<String, Integer> getRegisteredFluidIDs() {
      return ImmutableMap.copyOf(fluidIDs);
   }

   public static Fluid lookupFluidForBlock(Block block) {
      if (fluidBlocks == null) {
         fluidBlocks = HashBiMap.create();

          for (Fluid fluid : fluids.values()) {
              if (fluid.canBePlacedInWorld() && Block.blocksList[fluid.getBlockID()] != null) {
                  fluidBlocks.put(Block.blocksList[fluid.getBlockID()], fluid);
              }
          }
      }

      return fluidBlocks.get(block);
   }

   static {
      WATER = new Fluid("water") {
         /**
          * Gets the localized name of this block. Used for the statistics page.
          */
         public String getLocalizedName() {
            return StatCollector.translateToLocal("tile.water.name");
         }
      }.setBlockID(Block.waterStill.blockID).setUnlocalizedName(Block.waterStill.getUnlocalizedName());
      LAVA = new Fluid("lava") {
         /**
          * Gets the localized name of this block. Used for the statistics page.
          */
         public String getLocalizedName() {
            return StatCollector.translateToLocal("tile.lava.name");
         }
      }.setBlockID(Block.lavaStill.blockID).setLuminosity(15).setDensity(3000).setViscosity(6000).setTemperature(1300).setUnlocalizedName(Block.lavaStill.getUnlocalizedName());
      renderIdFluid = -1;
      registerFluid(WATER);
      registerFluid(LAVA);
   }
}
