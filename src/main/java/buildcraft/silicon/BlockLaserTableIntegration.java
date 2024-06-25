package buildcraft.silicon;

import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockLaserTableIntegration extends BlockLaserTable {
    public BlockLaserTableIntegration(int i) {
        super(i);
    }

    @Override
    public TileEntity createNewTileEntity(World var1) {
        return new TileIntegrationTable();
    }
}
