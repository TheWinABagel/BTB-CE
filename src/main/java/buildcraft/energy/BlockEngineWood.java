package buildcraft.energy;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Icon;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockEngineWood extends BlockEngine {
    public BlockEngineWood(int i) {
        super(i);
        setUnlocalizedName("engineWood");
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileEngineWood();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Icon getIcon(int side, int meta) {
        return woodTexture;
    }
}
