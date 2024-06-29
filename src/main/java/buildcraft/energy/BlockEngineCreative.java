package buildcraft.energy;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Icon;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockEngineCreative extends BlockEngine {
    public BlockEngineCreative(int i) {
        super(i);
        setUnlocalizedName("engineCreative");
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileEngineCreative();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Icon getIcon(int side, int meta) {
        return ironTexture;
    }
}