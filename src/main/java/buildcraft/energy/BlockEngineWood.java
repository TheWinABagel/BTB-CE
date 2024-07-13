package buildcraft.energy;

import btw.block.MechanicalBlock;
import btw.block.util.MechPowerUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Icon;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockEngineWood extends BlockEngine implements MechanicalBlock {
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

    @Override
    public boolean canOutputMechanicalPower() {
        return false;
    }

    @Override
    public boolean canInputMechanicalPower() {
        return true;
    }

    @Override
    public boolean isInputtingMechanicalPower(World world, int x, int y, int z) {
        return MechPowerUtils.isBlockPoweredByAxle(world, x, y, z, this);
    }

    @Override
    public boolean isOutputtingMechanicalPower(World var1, int var2, int var3, int var4) {
        return false;
    }

    @Override
    public boolean canInputAxlePowerToFacing(World world, int x, int y, int z, int iFacing) {
        int iBlockFacing = this.getFacing(world, x, y, z);
        return iFacing != iBlockFacing;
    }

    @Override
    public void overpower(World var1, int var2, int var3, int var4) {
        //todoenergy either break or explode
    }
}
