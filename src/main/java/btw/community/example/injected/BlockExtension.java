package btw.community.example.injected;

import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

import java.util.ArrayList;


public interface BlockExtension {
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune);
}
