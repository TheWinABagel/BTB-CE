package net.fabricmc.example.injected;

import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface BlockExtension {
    boolean recolourBlock(World world, int x, int y, int z, ForgeDirection side, int colour);

    boolean isNormalCube(IBlockAccess world, int x, int y, int z);
}
