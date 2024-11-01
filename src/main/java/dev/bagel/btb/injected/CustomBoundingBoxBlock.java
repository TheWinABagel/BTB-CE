package dev.bagel.btb.injected;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.World;

import java.util.List;

public interface CustomBoundingBoxBlock {
    List<AxisAlignedBB> getCustomSelectionBoxes(World world, int x, int y, int z);

    int getFacing(World world, int x, int y, int z);

}
