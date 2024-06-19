//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cofh.api.energy;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEnergyHandler extends TileEntity implements IEnergyHandler {
    protected EnergyStorage storage = new EnergyStorage(32000);

    public TileEnergyHandler() {
    }

    public void readFromNBT(NBTTagCompound var1) {
        super.readFromNBT(var1);
        this.storage.readFromNBT(var1);
    }

    public void writeToNBT(NBTTagCompound var1) {
        super.writeToNBT(var1);
        this.storage.writeToNBT(var1);
    }

    public boolean canConnectEnergy(ForgeDirection var1) {
        return true;
    }

    public int receiveEnergy(ForgeDirection var1, int var2, boolean var3) {
        return this.storage.receiveEnergy(var2, var3);
    }

    public int extractEnergy(ForgeDirection var1, int var2, boolean var3) {
        return this.storage.extractEnergy(var2, var3);
    }

    public int getEnergyStored(ForgeDirection var1) {
        return this.storage.getEnergyStored();
    }

    public int getMaxEnergyStored(ForgeDirection var1) {
        return this.storage.getMaxEnergyStored();
    }
}
