package net.minecraftforge.fluids;

import net.minecraft.src.World;

public class FluidEvent /*extends Event */{
   public final FluidStack fluid;
   /**
    * The x coordinate of this ChunkPosition
    */
   public final int x;
   /**
    * The y coordinate of this ChunkPosition
    */
   public final int y;
   /**
    * The z coordinate of this ChunkPosition
    */
   public final int z;
   public final World world;

   public FluidEvent(FluidStack fluid, World world, int x, int y, int z) {
      this.fluid = fluid;
      this.world = world;
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public static final void fireEvent(FluidEvent event) {
//      MinecraftForge.EVENT_BUS.post(event);
   }

   public static class FluidSpilledEvent extends FluidEvent {
      public FluidSpilledEvent(FluidStack fluid, World world, int x, int y, int z) {
         super(fluid, world, x, y, z);
      }
   }

   public static class FluidDrainingEvent extends FluidEvent {
      public final IFluidTank tank;

      public FluidDrainingEvent(FluidStack fluid, World world, int x, int y, int z, IFluidTank tank) {
         super(fluid, world, x, y, z);
         this.tank = tank;
      }
   }

   public static class FluidFillingEvent extends FluidEvent {
      public final IFluidTank tank;

      public FluidFillingEvent(FluidStack fluid, World world, int x, int y, int z, IFluidTank tank) {
         super(fluid, world, x, y, z);
         this.tank = tank;
      }
   }

   public static class FluidMotionEvent extends FluidEvent {
      public FluidMotionEvent(FluidStack fluid, World world, int x, int y, int z) {
         super(fluid, world, x, y, z);
      }
   }
}
