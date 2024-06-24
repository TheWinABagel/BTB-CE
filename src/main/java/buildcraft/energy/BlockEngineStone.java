package buildcraft.energy;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Icon;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockEngineStone extends BlockEngine {
    public BlockEngineStone(int i) {
        super(i);
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileEngineStone();
    }


    @Override
    @Environment(EnvType.CLIENT)
    public Icon getIcon(int side, int meta) {
        return stoneTexture;
    }
}
