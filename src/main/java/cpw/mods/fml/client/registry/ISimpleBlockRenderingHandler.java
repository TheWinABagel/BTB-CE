package cpw.mods.fml.client.registry;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;

public interface ISimpleBlockRenderingHandler {
    void renderInventoryBlock(Block block, int i, int j, RenderBlocks renderBlocks);

    boolean renderWorldBlock(IBlockAccess iBlockAccess, int i, int j, int k, Block block, int l, RenderBlocks renderBlocks);

    boolean shouldRender3DInInventory();

    int getRenderId();
}
