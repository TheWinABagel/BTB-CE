package net.minecraftforge.fluids;

import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

import java.util.Random;

public class BlockFluidClassic extends BlockFluidBase {
   /**
    * Indicates whether the flow direction is optimal. Each array index corresponds to one of the four cardinal directions.
    */
   protected boolean[] isOptimalFlowDirection = new boolean[4];
   /**
    * The estimated cost to flow in a given direction from the current point. Each array index corresponds to one of the four cardinal directions.
    */
   protected int[] flowCost = new int[4];
   protected FluidStack stack;

   public BlockFluidClassic(int id, Fluid fluid, Material material) {
      super(id, fluid, material);
      this.stack = new FluidStack(fluid, 1000);
   }

   public BlockFluidClassic setFluidStack(FluidStack stack) {
      this.stack = stack;
      return this;
   }

   public BlockFluidClassic setFluidStackAmount(int amount) {
      this.stack.amount = amount;
      return this;
   }

   public int getQuantaValue(IBlockAccess world, int x, int y, int z) {
      if (world.getBlockId(x, y, z) == 0) {
         return 0;
      } else if (world.getBlockId(x, y, z) != this.blockID) {
         return -1;
      } else {
         int quantaRemaining = this.quantaPerBlock - world.getBlockMetadata(x, y, z);
         return quantaRemaining;
      }
   }

   /**
    * Returns whether this block is collideable based on the arguments passed in \n@param par1 block metaData \n@param par2 whether the player right-clicked while holding a boat
    */
   public boolean canCollideCheck(int meta, boolean fullHit) {
      return fullHit && meta == 0;
   }

   public int getMaxRenderHeightMeta() {
      return 0;
   }

   /**
    * "Gets the light value of the specified block coords. Args: x, y, z"
    */
   public int getLightValue(IBlockAccess world, int x, int y, int z) {
      if (this.maxScaledLight == 0) {
         return super.getLightValue(world, x, y, z);
      } else {
         int data = this.quantaPerBlock - world.getBlockMetadata(x, y, z) - 1;
         return (int)((float)data / this.quantaPerBlockFloat * (float)this.maxScaledLight);
      }
   }

   /**
    * Ticks the block if it's been scheduled
    */
   public void updateTick(World world, int x, int y, int z, Random rand) {
      int quantaRemaining = this.quantaPerBlock - world.getBlockMetadata(x, y, z);
      int flowMeta;
      if (quantaRemaining < this.quantaPerBlock) {
         flowMeta = y - this.densityDir;
         int expQuanta;
         if (world.getBlockId(x, flowMeta, z) != this.blockID && world.getBlockId(x - 1, flowMeta, z) != this.blockID && world.getBlockId(x + 1, flowMeta, z) != this.blockID && world.getBlockId(x, flowMeta, z - 1) != this.blockID && world.getBlockId(x, flowMeta, z + 1) != this.blockID) {
            int maxQuanta = -100;
            maxQuanta = this.getLargerQuanta(world, x - 1, y, z, maxQuanta);
            maxQuanta = this.getLargerQuanta(world, x + 1, y, z, maxQuanta);
            maxQuanta = this.getLargerQuanta(world, x, y, z - 1, maxQuanta);
            maxQuanta = this.getLargerQuanta(world, x, y, z + 1, maxQuanta);
            expQuanta = maxQuanta - 1;
         } else {
            expQuanta = this.quantaPerBlock - 1;
         }

         if (expQuanta != quantaRemaining) {
            quantaRemaining = expQuanta;
            if (expQuanta <= 0) {
               world.setBlockToAir(x, y, z);
            } else {
               world.setBlockMetadataWithNotify(x, y, z, this.quantaPerBlock - expQuanta, 3);
               world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate);
               world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
            }
         }
      } else if (quantaRemaining >= this.quantaPerBlock) {
         world.setBlockMetadataWithNotify(x, y, z, 0, 2);
      }

      if (this.canDisplace(world, x, y + this.densityDir, z)) {
         this.flowIntoBlock(world, x, y + this.densityDir, z, 1);
      } else {
         flowMeta = this.quantaPerBlock - quantaRemaining + 1;
         if (flowMeta < this.quantaPerBlock) {
            if (this.isSourceBlock(world, x, y, z) || !this.isFlowingVertically(world, x, y, z)) {
               if (world.getBlockId(x, y - this.densityDir, z) == this.blockID) {
                  flowMeta = 1;
               }

               boolean[] flowTo = this.getOptimalFlowDirections(world, x, y, z);
               if (flowTo[0]) {
                  this.flowIntoBlock(world, x - 1, y, z, flowMeta);
               }

               if (flowTo[1]) {
                  this.flowIntoBlock(world, x + 1, y, z, flowMeta);
               }

               if (flowTo[2]) {
                  this.flowIntoBlock(world, x, y, z - 1, flowMeta);
               }

               if (flowTo[3]) {
                  this.flowIntoBlock(world, x, y, z + 1, flowMeta);
               }
            }

         }
      }
   }

   public boolean isFlowingVertically(IBlockAccess world, int x, int y, int z) {
      return world.getBlockId(x, y + this.densityDir, z) == this.blockID || world.getBlockId(x, y, z) == this.blockID && this.canFlowInto(world, x, y + this.densityDir, z);
   }

   public boolean isSourceBlock(IBlockAccess world, int x, int y, int z) {
      return world.getBlockId(x, y, z) == this.blockID && world.getBlockMetadata(x, y, z) == 0;
   }

   /**
    * Returns a boolean array indicating which flow directions are optimal based on each direction's calculated flow cost. Each array index corresponds to one of the four cardinal directions. A value of true indicates the direction is optimal.
    */
   protected boolean[] getOptimalFlowDirections(World world, int x, int y, int z) {
      int side;
      int x2;
      for(side = 0; side < 4; ++side) {
         this.flowCost[side] = 1000;
         x2 = x;
         int z2 = z;
         switch(side) {
         case 0:
            x2 = x - 1;
            break;
         case 1:
            x2 = x + 1;
            break;
         case 2:
            z2 = z - 1;
            break;
         case 3:
            z2 = z + 1;
         }

         if (this.canFlowInto(world, x2, y, z2) && !this.isSourceBlock(world, x2, y, z2)) {
            if (this.canFlowInto(world, x2, y + this.densityDir, z2)) {
               this.flowCost[side] = 0;
            } else {
               this.flowCost[side] = this.calculateFlowCost(world, x2, y, z2, 1, side);
            }
         }
      }

      side = this.flowCost[0];

      for(x2 = 1; x2 < 4; ++x2) {
         if (this.flowCost[x2] < side) {
            side = this.flowCost[x2];
         }
      }

      for(x2 = 0; x2 < 4; ++x2) {
         this.isOptimalFlowDirection[x2] = this.flowCost[x2] == side;
      }

      return this.isOptimalFlowDirection;
   }

   /**
    * "calculateFlowCost(World world, int x, int y, int z, int accumulatedCost, int previousDirectionOfFlow) - Used to determine the path of least resistance, this method returns the lowest possible flow cost for the direction of flow indicated. Each necessary horizontal flow adds to the flow cost."
    */
   protected int calculateFlowCost(World world, int x, int y, int z, int recurseDepth, int side) {
      int cost = 1000;

      for(int adjSide = 0; adjSide < 4; ++adjSide) {
         if ((adjSide != 0 || side != 1) && (adjSide != 1 || side != 0) && (adjSide != 2 || side != 3) && (adjSide != 3 || side != 2)) {
            int x2 = x;
            int z2 = z;
            switch(adjSide) {
            case 0:
               x2 = x - 1;
               break;
            case 1:
               x2 = x + 1;
               break;
            case 2:
               z2 = z - 1;
               break;
            case 3:
               z2 = z + 1;
            }

            if (this.canFlowInto(world, x2, y, z2) && !this.isSourceBlock(world, x2, y, z2)) {
               if (this.canFlowInto(world, x2, y + this.densityDir, z2)) {
                  return recurseDepth;
               }

               if (recurseDepth < 4) {
                  int min = this.calculateFlowCost(world, x2, y, z2, recurseDepth + 1, adjSide);
                  if (min < cost) {
                     cost = min;
                  }
               }
            }
         }
      }

      return cost;
   }

   /**
    * "flowIntoBlock(World world, int x, int y, int z, int newFlowDecay) - Flows into the block at the coordinates and changes the block type to the liquid."
    */
   protected void flowIntoBlock(World world, int x, int y, int z, int meta) {
      if (meta >= 0) {
         if (this.displaceIfPossible(world, x, y, z)) {
            world.setBlock(x, y, z, this.blockID, meta, 3);
         }

      }
   }

   protected boolean canFlowInto(IBlockAccess world, int x, int y, int z) {
      if (world.isAirBlock(x, y, z)) {
         return true;
      } else {
         int bId = world.getBlockId(x, y, z);
         if (bId == this.blockID) {
            return true;
         } else if (this.displacementIds.containsKey(bId)) {
            return (Boolean)this.displacementIds.get(bId);
         } else {
            Material material = Block.blocksList[bId].blockMaterial;
            if (!material.blocksMovement() && material != Material.water && material != Material.lava && material != Material.portal) {
               int density = getDensity(world, x, y, z);
               if (density == Integer.MAX_VALUE) {
                  return true;
               } else {
                  return this.density > density;
               }
            } else {
               return false;
            }
         }
      }
   }

   protected int getLargerQuanta(IBlockAccess world, int x, int y, int z, int compare) {
      int quantaRemaining = this.getQuantaValue(world, x, y, z);
      if (quantaRemaining <= 0) {
         return compare;
      } else {
         return quantaRemaining >= compare ? quantaRemaining : compare;
      }
   }

   public FluidStack drain(World world, int x, int y, int z, boolean doDrain) {
      if (!this.isSourceBlock(world, x, y, z)) {
         return null;
      } else {
         if (doDrain) {
            world.setBlockToAir(x, y, z);
         }

         return this.stack.copy();
      }
   }

   public boolean canDrain(World world, int x, int y, int z) {
      return this.isSourceBlock(world, x, y, z);
   }
}
