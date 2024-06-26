package buildcraft.silicon;

import net.minecraft.src.Icon;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockLaserTableIntegration extends BlockLaserTable {
    public BlockLaserTableIntegration(int i) {
        super(i);
    }

    @Override
    protected int getOldMeta() {
        return 2;
    }

    @Override
    public TileEntity createNewTileEntity(World var1) {
        return new TileIntegrationTable();
    }

    @Override
    public Icon getIcon(int side, int meta) {
        int s = side > 1 ? 2 : side;
        return icons[2][s];
    }
}
