package buildcraft.silicon;

import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockLaserTableAssembly extends BlockLaserTable {
    public BlockLaserTableAssembly(int i) {
        super(i);
    }

    @Override
    public TileEntity createNewTileEntity(World var1) {
        return new TileAssemblyTable();
    }
}
