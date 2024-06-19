package buildcraft.api.interfaces;

import net.minecraft.src.Block;

public interface IWorldExtension {

    Block getBlock(int x, int y, int z);
}
