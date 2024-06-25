package buildcraft.silicon;

import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockLaserTableAdvancedCrafting extends BlockLaserTable {
    public BlockLaserTableAdvancedCrafting(int i) {
        super(i);
    }

    @Override
    public TileEntity createNewTileEntity(World var1) {
        return new TileAdvancedCraftingTable();
    }
}
