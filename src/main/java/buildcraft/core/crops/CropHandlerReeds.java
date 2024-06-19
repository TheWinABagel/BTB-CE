package buildcraft.core.crops;

import java.util.List;

import net.minecraft.src.*;
import net.minecraftforge.common.util.ForgeDirection;

import buildcraft.api.crops.CropManager;
import buildcraft.api.crops.ICropHandler;

public class CropHandlerReeds implements ICropHandler {

    @Override
    public boolean isSeed(ItemStack stack) {
        return stack.getItem() == Item.reed;
    }

    @Override
    public boolean canSustainPlant(World world, ItemStack seed, int x, int y, int z) {
//        Block block = world.getBlock(x, y, z);
//        return block.canSustainPlant(world, x, y, z, ForgeDirection.UP, (IPlantable) Blocks.reeds)
//                && block != Blocks.reeds
//                &&
               return world.isAirBlock(x, y + 1, z);
    }

    @Override
    public boolean plantCrop(World world, EntityPlayer player, ItemStack seed, int x, int y, int z) {
        return CropManager.getDefaultHandler().plantCrop(world, player, seed, x, y, z);
    }

    @Override
    public boolean isMature(IBlockAccess blockAccess, Block block, int meta, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean harvestCrop(World world, int x, int y, int z, List<ItemStack> drops) {
        return false;
    }
}
