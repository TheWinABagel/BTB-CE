package buildcraft.silicon;

import net.minecraft.src.Icon;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockLaserTableAdvancedCrafting extends BlockLaserTable {
    public BlockLaserTableAdvancedCrafting(int i) {
        super(i);
    }

    @Override
    protected int getOldMeta() {
        return 1;
    }

    @Override
    public TileEntity createNewTileEntity(World var1) {
        return new TileAdvancedCraftingTable();
    }

    @Override
    public Icon getIcon(int side, int meta) {
        int s = side > 1 ? 2 : side;
        return icons[1][s];
    }
}
