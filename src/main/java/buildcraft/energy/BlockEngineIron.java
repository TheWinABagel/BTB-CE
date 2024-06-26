package buildcraft.energy;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Icon;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockEngineIron extends BlockEngine {
    public BlockEngineIron(int i) {
        super(i);
        setUnlocalizedName("engineIron");
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileEngineIron();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Icon getIcon(int side, int meta) {
        return ironTexture;
    }
}
