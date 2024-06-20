package net.minecraftforge.fluids;

import net.minecraft.src.Block;
import net.minecraft.src.material.Material;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

import java.util.Random;

public class BlockFluidFinite extends BlockFluidBase {
   public BlockFluidFinite(int id, Fluid fluid, Material material) {
      super(id, fluid, material);
   }

   public int getQuantaValue(IBlockAccess world, int x, int y, int z) {
      if (world.isAirBlock(x, y, z)) {
         return 0;
      } else if (world.getBlockId(x, y, z) != this.blockID) {
         return -1;
      } else {
         int quantaRemaining = world.getBlockMetadata(x, y, z) + 1;
         return quantaRemaining;
      }
   }

   /**
    * Returns whether this block is collideable based on the arguments passed in \n@param par1 block metaData \n@param par2 whether the player right-clicked while holding a boat
    */
   public boolean canCollideCheck(int meta, boolean fullHit) {
      return fullHit && meta == this.quantaPerBlock - 1;
   }

   public int getMaxRenderHeightMeta() {
      return this.quantaPerBlock - 1;
   }

   /**
    * Ticks the block if it's been scheduled
    */
   public void updateTick(World world, int x, int y, int z, Random rand) {
      boolean changed = false;
      int quantaRemaining = world.getBlockMetadata(x, y, z) + 1;
      int prevRemaining = quantaRemaining;
      quantaRemaining = this.tryToFlowVerticallyInto(world, x, y, z, quantaRemaining);
      if (quantaRemaining >= 1) {
         if (quantaRemaining != prevRemaining) {
            changed = true;
            if (quantaRemaining == 1) {
               world.setBlockMetadataWithNotify(x, y, z, quantaRemaining - 1, 2);
               return;
            }
         } else if (quantaRemaining == 1) {
            return;
         }

         int lowerthan = quantaRemaining - 1;
         if (this.displaceIfPossible(world, x, y, z - 1)) {
            world.setBlock(x, y, z - 1, 0);
         }

         if (this.displaceIfPossible(world, x, y, z + 1)) {
            world.setBlock(x, y, z + 1, 0);
         }

         if (this.displaceIfPossible(world, x - 1, y, z)) {
            world.setBlock(x - 1, y, z, 0);
         }

         if (this.displaceIfPossible(world, x + 1, y, z)) {
            world.setBlock(x + 1, y, z, 0);
         }

         int north = this.getQuantaValueBelow(world, x, y, z - 1, lowerthan);
         int south = this.getQuantaValueBelow(world, x, y, z + 1, lowerthan);
         int west = this.getQuantaValueBelow(world, x - 1, y, z, lowerthan);
         int east = this.getQuantaValueBelow(world, x + 1, y, z, lowerthan);
         int total = quantaRemaining;
         int count = 1;
         if (north >= 0) {
            ++count;
            total = quantaRemaining + north;
         }

         if (south >= 0) {
            ++count;
            total += south;
         }

         if (west >= 0) {
            ++count;
            total += west;
         }

         if (east >= 0) {
            ++count;
            total += east;
         }

         if (count == 1) {
            if (changed) {
               world.setBlockMetadataWithNotify(x, y, z, quantaRemaining - 1, 2);
            }

         } else {
            int each = total / count;
            int rem = total % count;
            int neweast;
            if (north >= 0) {
               neweast = each;
               if (rem == count || rem > 1 && rand.nextInt(count - rem) != 0) {
                  neweast = each + 1;
                  --rem;
               }

               if (neweast != north) {
                  if (neweast == 0) {
                     world.setBlock(x, y, z - 1, 0);
                  } else {
                     world.setBlock(x, y, z - 1, this.blockID, neweast - 1, 2);
                  }

                  world.scheduleBlockUpdate(x, y, z - 1, this.blockID, this.tickRate);
               }

               --count;
            }

            if (south >= 0) {
               neweast = each;
               if (rem == count || rem > 1 && rand.nextInt(count - rem) != 0) {
                  neweast = each + 1;
                  --rem;
               }

               if (neweast != south) {
                  if (neweast == 0) {
                     world.setBlock(x, y, z + 1, 0);
                  } else {
                     world.setBlock(x, y, z + 1, this.blockID, neweast - 1, 2);
                  }

                  world.scheduleBlockUpdate(x, y, z + 1, this.blockID, this.tickRate);
               }

               --count;
            }

            if (west >= 0) {
               neweast = each;
               if (rem == count || rem > 1 && rand.nextInt(count - rem) != 0) {
                  neweast = each + 1;
                  --rem;
               }

               if (neweast != west) {
                  if (neweast == 0) {
                     world.setBlock(x - 1, y, z, 0);
                  } else {
                     world.setBlock(x - 1, y, z, this.blockID, neweast - 1, 2);
                  }

                  world.scheduleBlockUpdate(x - 1, y, z, this.blockID, this.tickRate);
               }

               --count;
            }

            if (east >= 0) {
               neweast = each;
               if (rem == count || rem > 1 && rand.nextInt(count - rem) != 0) {
                  neweast = each + 1;
                  --rem;
               }

               if (neweast != east) {
                  if (neweast == 0) {
                     world.setBlock(x + 1, y, z, 0);
                  } else {
                     world.setBlock(x + 1, y, z, this.blockID, neweast - 1, 2);
                  }

                  world.scheduleBlockUpdate(x + 1, y, z, this.blockID, this.tickRate);
               }

               --count;
            }

            if (rem > 0) {
               ++each;
            }

            world.setBlockMetadataWithNotify(x, y, z, each - 1, 2);
         }
      }
   }

   public int tryToFlowVerticallyInto(World world, int x, int y, int z, int amtToInput) {
      int otherY = y + this.densityDir;
      if (otherY >= 0 && otherY < world.getHeight()) {
         int amt = this.getQuantaValueBelow(world, x, otherY, z, this.quantaPerBlock);
         if (amt >= 0) {
            amt += amtToInput;
            if (amt > this.quantaPerBlock) {
               world.setBlock(x, otherY, z, this.blockID, this.quantaPerBlock - 1, 3);
               world.scheduleBlockUpdate(x, otherY, z, this.blockID, this.tickRate);
               return amt - this.quantaPerBlock;
            } else if (amt > 0) {
               world.setBlock(x, otherY, z, this.blockID, amt - 1, 3);
               world.scheduleBlockUpdate(x, otherY, z, this.blockID, this.tickRate);
               world.setBlockToAir(x, y, z);
               return 0;
            } else {
               return amtToInput;
            }
         } else {
            int density_other = getDensity(world, x, otherY, z);
            if (density_other == Integer.MAX_VALUE) {
               if (this.displaceIfPossible(world, x, otherY, z)) {
                  world.setBlock(x, otherY, z, this.blockID, amtToInput - 1, 3);
                  world.scheduleBlockUpdate(x, otherY, z, this.blockID, this.tickRate);
                  world.setBlockToAir(x, y, z);
                  return 0;
               } else {
                  return amtToInput;
               }
            } else {
               int bId;
               BlockFluidBase block;
               int otherData;
               if (this.densityDir < 0) {
                  if (density_other < this.density) {
                     bId = world.getBlockId(x, otherY, z);
                     block = (BlockFluidBase)Block.blocksList[bId];
                     otherData = world.getBlockMetadata(x, otherY, z);
                     world.setBlock(x, otherY, z, this.blockID, amtToInput - 1, 3);
                     world.setBlock(x, y, z, bId, otherData, 3);
                     world.scheduleBlockUpdate(x, otherY, z, this.blockID, this.tickRate);
                     world.scheduleBlockUpdate(x, y, z, bId, block.tickRate(world));
                     return 0;
                  }
               } else if (density_other > this.density) {
                  bId = world.getBlockId(x, otherY, z);
                  block = (BlockFluidBase)Block.blocksList[bId];
                  otherData = world.getBlockMetadata(x, otherY, z);
                  world.setBlock(x, otherY, z, this.blockID, amtToInput - 1, 3);
                  world.setBlock(x, y, z, bId, otherData, 3);
                  world.scheduleBlockUpdate(x, otherY, z, this.blockID, this.tickRate);
                  world.scheduleBlockUpdate(x, y, z, bId, block.tickRate(world));
                  return 0;
               }

               return amtToInput;
            }
         }
      } else {
         world.setBlockToAir(x, y, z);
         return 0;
      }
   }

   public FluidStack drain(World world, int x, int y, int z, boolean doDrain) {
      return null;
   }

   public boolean canDrain(World world, int x, int y, int z) {
      return false;
   }
}
