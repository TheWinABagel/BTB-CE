package buildcraft.silicon;

import net.minecraft.src.Icon;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockLaserTableAssembly extends BlockLaserTable {
    public BlockLaserTableAssembly(int i) {
        super(i);
    }

    @Override
    protected int getOldMeta() {
        return 0;
    }

    @Override
    public TileEntity createNewTileEntity(World var1) {
        return new TileAssemblyTable();
    }

    @Override
    public Icon getIcon(int side, int meta) {
        int s = side > 1 ? 2 : side;
        return icons[0][s];
    }
}
