package net.minecraftforge.fluids;

import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraft.src.Entity;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Vec3;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class BlockFluidBase extends Block implements IFluidBlock {
   protected static final Map<Integer, Boolean> defaultDisplacementIds = new HashMap();
   protected Map<Integer, Boolean> displacementIds = new HashMap();
   protected int quantaPerBlock = 8;
   protected float quantaPerBlockFloat = 8.0F;
   protected int density = 1;
   protected int densityDir = -1;
   /**
    * The temperature of this biome.
    */
   protected int temperature = 295;
   protected int tickRate = 20;
   protected int renderPass = 1;
   protected int maxScaledLight = 0;
   protected final String fluidName;

   public BlockFluidBase(int id, Fluid fluid, Material material) {
      super(id, material);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      this.setTickRandomly(true);
      this.disableStats();
      this.fluidName = fluid.getName();
      this.density = fluid.density;
      this.temperature = fluid.temperature;
      this.maxScaledLight = fluid.luminosity;
      this.tickRate = fluid.viscosity / 200;
      this.densityDir = fluid.density > 0 ? -1 : 1;
      fluid.setBlockID(id);
      this.displacementIds.putAll(defaultDisplacementIds);
   }

   public BlockFluidBase setQuantaPerBlock(int quantaPerBlock) {
      if (quantaPerBlock > 16 || quantaPerBlock < 1) {
         quantaPerBlock = 8;
      }

      this.quantaPerBlock = quantaPerBlock;
      this.quantaPerBlockFloat = (float)quantaPerBlock;
      return this;
   }

   public BlockFluidBase setDensity(int density) {
      if (density == 0) {
         density = 1;
      }

      this.density = density;
      this.densityDir = density > 0 ? -1 : 1;
      return this;
   }

   public BlockFluidBase setTemperature(int temperature) {
      this.temperature = temperature;
      return this;
   }

   public BlockFluidBase setTickRate(int tickRate) {
      if (tickRate <= 0) {
         tickRate = 20;
      }

      this.tickRate = tickRate;
      return this;
   }

   public BlockFluidBase setRenderPass(int renderPass) {
      this.renderPass = renderPass;
      return this;
   }

   public BlockFluidBase setMaxScaledLight(int maxScaledLight) {
      this.maxScaledLight = maxScaledLight;
      return this;
   }

   public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
      if (world.isAirBlock(x, y, z)) {
         return true;
      } else {
         int bId = world.getBlockId(x, y, z);
         if (bId == this.blockID) {
            return false;
         } else if (this.displacementIds.containsKey(bId)) {
            return (Boolean)this.displacementIds.get(bId);
         } else {
            Material material = Block.blocksList[bId].blockMaterial;
            if (!material.blocksMovement() && material != Material.portal) {
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

   public boolean displaceIfPossible(World world, int x, int y, int z) {
      if (world.isAirBlock(x, y, z)) {
         return true;
      } else {
         int bId = world.getBlockId(x, y, z);
         if (bId == this.blockID) {
            return false;
         } else if (this.displacementIds.containsKey(bId)) {
            if ((Boolean)this.displacementIds.get(bId)) {
               Block.blocksList[bId].dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
               return true;
            } else {
               return false;
            }
         } else {
            Material material = Block.blocksList[bId].blockMaterial;
            if (!material.blocksMovement() && material != Material.portal) {
               int density = getDensity(world, x, y, z);
               if (density == Integer.MAX_VALUE) {
                  Block.blocksList[bId].dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
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

   public abstract int getQuantaValue(IBlockAccess iBlockAccess, int i, int j, int k);

   /**
    * Returns whether this block is collideable based on the arguments passed in \n@param par1 block metaData \n@param par2 whether the player right-clicked while holding a boat
    */
   public abstract boolean canCollideCheck(int i, boolean bl);

   public abstract int getMaxRenderHeightMeta();

   /**
    * "Called whenever the block is added into the world. Args: world, x, y, z"
    */
   public void onBlockAdded(World world, int x, int y, int z) {
      world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate);
   }

   /**
    * "Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are their own) Args: x, y, z, neighbor blockID"
    */
   public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
      world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate);
   }

   public boolean func_82506_l() {
      return false;
   }

   public boolean getBlocksMovement(IBlockAccess world, int x, int y, int z) {
      return true;
   }

   /**
    * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been cleared to be reused)
    */
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      return null;
   }

   /**
    * Returns the ID of the items to drop on destruction.
    */
   public int idDropped(int par1, Random par2Random, int par3) {
      return 0;
   }

   /**
    * Returns the quantity of items to drop on block destruction.
    */
   public int quantityDropped(Random par1Random) {
      return 0;
   }

   /**
    * How many world ticks before ticking
    */
   public int tickRate(World world) {
      return this.tickRate;
   }

   /**
    * "Can add to the passed in vector for a movement vector to be applied to the entity. Args: x, y, z, entity, vec3d"
    */
   public void velocityToAddToEntity(World world, int x, int y, int z, Entity entity, Vec3 vec) {
      if (this.densityDir <= 0) {
         Vec3 vec_flow = this.getFlowVector(world, x, y, z);
         vec.xCoord += vec_flow.xCoord * (double)(this.quantaPerBlock * 4);
         vec.yCoord += vec_flow.yCoord * (double)(this.quantaPerBlock * 4);
         vec.zCoord += vec_flow.zCoord * (double)(this.quantaPerBlock * 4);
      }
   }

   /**
    * "Gets the light value of the specified block coords. Args: x, y, z"
    */
   public int getLightValue(IBlockAccess world, int x, int y, int z) {
      if (this.maxScaledLight == 0) {
         return super.getLightValue(world, x, y, z);
      } else {
         int data = world.getBlockMetadata(x, y, z);
         return (int)((float)data / this.quantaPerBlockFloat * (float)this.maxScaledLight);
      }
   }

   /**
    * The type of render function that is called for this block
    */
   public int getRenderType() {
      return FluidRegistry.renderIdFluid;
   }

   /**
    * "Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block."
    */
   public boolean isOpaqueCube() {
      return false;
   }

   /**
    * "If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)"
    */
   public boolean renderAsNormalBlock() {
      return false;
   }

   /**
    * "How bright to render this block based on the light its receiving. Args: iBlockAccess, x, y, z"
    */
   public float getBlockBrightness(IBlockAccess world, int x, int y, int z) {
      float lightThis = world.getLightBrightness(x, y, z);
      float lightUp = world.getLightBrightness(x, y + 1, z);
      return lightThis > lightUp ? lightThis : lightUp;
   }

   /**
    * "Goes straight to getLightBrightnessForSkyBlocks for Blocks, does some fancy computing for Fluids"
    */
   public int getMixedBrightnessForBlock(IBlockAccess world, int x, int y, int z) {
      int lightThis = world.getLightBrightnessForSkyBlocks(x, y, z, 0);
      int lightUp = world.getLightBrightnessForSkyBlocks(x, y + 1, z, 0);
      int lightThisBase = lightThis & 255;
      int lightUpBase = lightUp & 255;
      int lightThisExt = lightThis >> 16 & 255;
      int lightUpExt = lightUp >> 16 & 255;
      return (lightThisBase > lightUpBase ? lightThisBase : lightUpBase) | (lightThisExt > lightUpExt ? lightThisExt : lightUpExt) << 16;
   }

   /**
    * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
    */
   public int getRenderBlockPass() {
      return this.renderPass;
   }

   /**
    * "Returns true if the given side of this block type should be rendered, if the adjacent block is at the given coordinates.  Args: blockAccess, x, y, z, side"
    */
   public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
      if (world.getBlockId(x, y, z) != this.blockID) {
         return !world.isBlockOpaqueCube(x, y, z);
      } else {
         Material mat = world.getBlockMaterial(x, y, z);
         return mat == this.blockMaterial ? false : super.shouldSideBeRendered(world, x, y, z, side);
      }
   }

   public static final int getDensity(IBlockAccess world, int x, int y, int z) {
      Block block = Block.blocksList[world.getBlockId(x, y, z)];
      return !(block instanceof BlockFluidBase) ? Integer.MAX_VALUE : ((BlockFluidBase)block).density;
   }

   public static final int getTemperature(IBlockAccess world, int x, int y, int z) {
      Block block = Block.blocksList[world.getBlockId(x, y, z)];
      return !(block instanceof BlockFluidBase) ? Integer.MAX_VALUE : ((BlockFluidBase)block).temperature;
   }

   /**
    * the sin and cos of this number determine the surface gradient of the flowing block.
    */
   public static double getFlowDirection(IBlockAccess world, int x, int y, int z) {
      Block block = Block.blocksList[world.getBlockId(x, y, z)];
      if (!world.getBlockMaterial(x, y, z).isLiquid()) {
         return -1000.0D;
      } else {
         Vec3 vec = ((BlockFluidBase)block).getFlowVector(world, x, y, z);
         return vec.xCoord == 0.0D && vec.zCoord == 0.0D ? -1000.0D : Math.atan2(vec.zCoord, vec.xCoord) - 1.5707963267948966D;
      }
   }

   public final int getQuantaValueBelow(IBlockAccess world, int x, int y, int z, int belowThis) {
      int quantaRemaining = this.getQuantaValue(world, x, y, z);
      return quantaRemaining >= belowThis ? -1 : quantaRemaining;
   }

   public final int getQuantaValueAbove(IBlockAccess world, int x, int y, int z, int aboveThis) {
      int quantaRemaining = this.getQuantaValue(world, x, y, z);
      return quantaRemaining <= aboveThis ? -1 : quantaRemaining;
   }

   public final float getQuantaPercentage(IBlockAccess world, int x, int y, int z) {
      int quantaRemaining = this.getQuantaValue(world, x, y, z);
      return (float)quantaRemaining / this.quantaPerBlockFloat;
   }

   /**
    * Returns a vector indicating the direction and intensity of fluid flow.
    */
   public Vec3 getFlowVector(IBlockAccess world, int x, int y, int z) {
      Vec3 vec = world.getWorldVec3Pool().getVecFromPool(0.0D, 0.0D, 0.0D);
      int decay = this.quantaPerBlock - this.getQuantaValue(world, x, y, z);

      for(int side = 0; side < 4; ++side) {
         int x2 = x;
         int z2 = z;
         switch(side) {
         case 0:
            x2 = x - 1;
            break;
         case 1:
            z2 = z - 1;
            break;
         case 2:
            x2 = x + 1;
            break;
         case 3:
            z2 = z + 1;
         }

         int otherDecay = this.quantaPerBlock - this.getQuantaValue(world, x2, y, z2);
         int power;
         if (otherDecay >= this.quantaPerBlock) {
            if (!world.getBlockMaterial(x2, y, z2).blocksMovement()) {
               otherDecay = this.quantaPerBlock - this.getQuantaValue(world, x2, y - 1, z2);
               if (otherDecay >= 0) {
                  power = otherDecay - (decay - this.quantaPerBlock);
                  vec = vec.addVector((double)((x2 - x) * power), (double)((y - y) * power), (double)((z2 - z) * power));
               }
            }
         } else if (otherDecay >= 0) {
            power = otherDecay - decay;
            vec = vec.addVector((double)((x2 - x) * power), (double)((y - y) * power), (double)((z2 - z) * power));
         }
      }

      if (world.getBlockId(x, y + 1, z) == this.blockID) {
         boolean flag = this.isBlockSolid(world, x, y, z - 1, 2) || this.isBlockSolid(world, x, y, z + 1, 3) || this.isBlockSolid(world, x - 1, y, z, 4) || this.isBlockSolid(world, x + 1, y, z, 5) || this.isBlockSolid(world, x, y + 1, z - 1, 2) || this.isBlockSolid(world, x, y + 1, z + 1, 3) || this.isBlockSolid(world, x - 1, y + 1, z, 4) || this.isBlockSolid(world, x + 1, y + 1, z, 5);
         if (flag) {
            vec = vec.normalize().addVector(0.0D, -6.0D, 0.0D);
         }
      }

      vec = vec.normalize();
      return vec;
   }

   public Fluid getFluid() {
      return FluidRegistry.getFluid(this.fluidName);
   }

   public float getFilledPercentage(World world, int x, int y, int z) {
      int quantaRemaining = this.getQuantaValue(world, x, y, z) + 1;
      float remaining = (float)quantaRemaining / this.quantaPerBlockFloat;
      if (remaining > 1.0F) {
         remaining = 1.0F;
      }

      return remaining * (float)(this.density > 0 ? 1 : -1);
   }

   static {
      defaultDisplacementIds.put(Block.doorWood.blockID, false);
      defaultDisplacementIds.put(Block.doorIron.blockID, false);
      defaultDisplacementIds.put(Block.signPost.blockID, false);
      defaultDisplacementIds.put(Block.signWall.blockID, false);
      defaultDisplacementIds.put(Block.reed.blockID, false);
   }
}
