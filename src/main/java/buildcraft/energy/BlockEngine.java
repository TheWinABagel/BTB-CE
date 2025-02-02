/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 * <p>
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.energy;

import buildcraft.BuildCraftCore;
import buildcraft.core.BlockBuildCraft;
import buildcraft.core.IItemPipe;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;
import net.minecraftforge.common.ForgeDirection;

import java.util.Random;

public abstract class BlockEngine extends BlockBuildCraft {

    protected static Icon woodTexture;
    protected static Icon stoneTexture;
    protected static Icon ironTexture;

    public BlockEngine(int i) {
        super(i, Material.iron);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void registerIcons(IconRegister par1IconRegister) {
        woodTexture = par1IconRegister.registerIcon("buildcraft:engineWoodBottom");
        stoneTexture = par1IconRegister.registerIcon("buildcraft:engineStoneBottom");
        ironTexture = par1IconRegister.registerIcon("buildcraft:engineIronBottom");
    }

    @Override
    public int getRenderType() {
        return BuildCraftCore.blockByEntityModel;
    }

    @Override
    public boolean doesItemRenderAsBlock(int iItemDamage) {
        return true;
    }

    @Override
    public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
        RenderingRegistry.instance().renderInventoryBlock(renderBlocks, this, iItemDamage, getRenderType());
    }

    @Override
    public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile instanceof TileEngine) {
            return ((TileEngine) tile).orientation.getOpposite() == side;
        }
        return false;
    }

    @Override
    public boolean canRotateOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
        return true;
    }

    @Override
    public boolean rotateAroundJAxis(World world, int x, int y, int z, boolean bReverse) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile instanceof TileEngine engine) {
            return engine.switchOrientation(false);
        }
        return false;
    }

    @Override
    public int rotateOnTurntable(World world, int x, int y, int z, boolean reverse, int craftingCounter) {
        return super.rotateOnTurntable(world, x, y, z, reverse, craftingCounter);
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int side, float par7, float par8, float par9) {

        TileEntity tile = world.getBlockTileEntity(i, j, k);

        // Drop through if the player is sneaking
        if (player.isSneaking()) {
            return false;
        }

        // Do not open guis when having a pipe in hand
        if (player.getCurrentEquippedItem() != null) {
            if (player.getCurrentEquippedItem().getItem() instanceof IItemPipe) {
                return false;
            }
        }

        if (tile instanceof TileEngine tileEngine) {
            return tileEngine.onBlockActivated(player, ForgeDirection.getOrientation(side));
        }

        return false;
    }

    @Override
    public void onPostBlockPlaced(World world, int x, int y, int z, int par5) {
        TileEngine tile = (TileEngine) world.getBlockTileEntity(x, y, z);
        tile.orientation = ForgeDirection.UP;
        if (!tile.isOrientationValid()) {
            tile.switchOrientation(true);
        }
    }

    @Override
    public int damageDropped(int i) {
        return i;
    }

    @SuppressWarnings({"all"})
    @Override
    public void randomDisplayTick(World world, int i, int j, int k, Random random) {
        TileEngine tile = (TileEngine) world.getBlockTileEntity(i, j, k);

        if (!tile.isBurning()) {
            return;
        }

        float f = (float) i + 0.5F;
        float f1 = (float) j + 0.0F + (random.nextFloat() * 6F) / 16F;
        float f2 = (float) k + 0.5F;
        float f3 = 0.52F;
        float f4 = random.nextFloat() * 0.6F - 0.3F;

        world.spawnParticle("reddust", f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
        world.spawnParticle("reddust", f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
        world.spawnParticle("reddust", f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
        world.spawnParticle("reddust", f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
        TileEngine tile = (TileEngine) world.getBlockTileEntity(i, j, k);

        if (tile != null) {
            tile.checkRedstonePower();
        }
    }
}
