package buildcraft.core;

import buildcraft.BuildCraftCore;
import buildcraft.api.transport.IPipeTile;
import buildcraft.core.lib.block.BlockBuildCraft;
import buildcraft.core.lib.utils.ResourceUtils;
import buildcraft.core.lib.utils.Utils;
import net.minecraft.src.Block;
import net.minecraft.src.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.src.Icon;
import net.minecraft.src.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class BlockHatched extends BlockBuildCraft {

    private Icon itemHatch;

    protected BlockHatched(Material material) {
        super(material);

        setRotatable(true);
        setPassCount(2);
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return 1;
    }

    @Override
    public void registerBlockIcons(IconRegister register) {
        super.registerBlockIcons(register);
        String base = ResourceUtils.getObjectPrefix(Block.blockRegistry.getNameForObject(this));
        itemHatch = register.registerIcon(base + "/item_hatch");
    }

    @Override
    public Icon getIcon(IBlockAccess access, int x, int y, int z, int side) {
        // The quarry's pipe connection method has no idea about "sides".
        if (renderPass == 1) {
            return Utils.isPipeConnected(access, x, y, z, ForgeDirection.getOrientation(side), IPipeTile.PipeType.ITEM)
                    ? itemHatch
                    : BuildCraftCore.transparentTexture;
        } else {
            return super.getIcon(access, x, y, z, side);
        }
    }

    @Override
    public Icon getIconAbsolute(IBlockAccess access, int x, int y, int z, int side, int meta) {
        if (renderPass == 0) {
            return super.getIconAbsolute(access, x, y, z, side, meta);
        } else {
            return null;
        }
    }

    @Override
    public Icon getIconAbsolute(int side, int meta) {
        if (renderPass == 0) {
            return super.getIconAbsolute(side, meta);
        } else {
            return side == 1 ? itemHatch : null;
        }
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
}
