package buildcraft.core;

import buildcraft.core.lib.engines.BlockEngineBase;
import buildcraft.core.lib.engines.TileEngineBase;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

import java.util.List;

public class BlockEngine extends BlockEngineBase {

    private final Class[] engineTiles;
    private final String[] names;
    private final String[] texturePaths;

    public BlockEngine() {
        super();
        setBlockName("engineBlock");

        engineTiles = new Class[16];
        names = new String[16];
        texturePaths = new String[16];
    }

    @Override
    public String getTexturePrefix(int meta, boolean addPrefix) {
        if (texturePaths[meta] != null) {
            if (addPrefix) {
                return texturePaths[meta].replaceAll(":", ":textures/blocks/");
            } else {
                return texturePaths[meta];
            }
        } else {
            return null;
        }
    }

    @Override
    public String getUnlocalizedName(int metadata) {
        return names[metadata] != null ? names[metadata] : "unknown";
    }

    public void registerTile(Class<? extends TileEngineBase> engineTile, int meta, String name, String texturePath) {
        if (BCRegistry.INSTANCE.isEnabled("engines", name)) {
            engineTiles[meta] = engineTile;
            names[meta] = name;
            texturePaths[meta] = texturePath;
        }
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        if (engineTiles[metadata] == null) {
            return null;
        }
        try {
            return (TileEntity) engineTiles[metadata].newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(Item item, CreativeTabs par2CreativeTabs, List itemList) {
        for (int i = 0; i < 16; i++) {
            if (engineTiles[i] != null) {
                itemList.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public boolean hasEngine(int meta) {
        return engineTiles[meta] != null;
    }
}
