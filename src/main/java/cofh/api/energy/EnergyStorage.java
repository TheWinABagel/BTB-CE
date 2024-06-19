package cofh.api.energy;

import net.minecraft.src.NBTTagCompound;

public class EnergyStorage implements IEnergyStorage {
    protected int energy;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;

    public EnergyStorage(int var1) {
        this(var1, var1, var1);
    }

    public EnergyStorage(int var1, int var2) {
        this(var1, var2, var2);
    }

    public EnergyStorage(int var1, int var2, int var3) {
        this.capacity = var1;
        this.maxReceive = var2;
        this.maxExtract = var3;
    }

    public EnergyStorage readFromNBT(NBTTagCompound var1) {
        this.energy = var1.getInteger("Energy");
        if (this.energy > this.capacity) {
            this.energy = this.capacity;
        }

        return this;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound var1) {
        if (this.energy < 0) {
            this.energy = 0;
        }

        var1.setInteger("Energy", this.energy);
        return var1;
    }

    public void setCapacity(int var1) {
        this.capacity = var1;
        if (this.energy > var1) {
            this.energy = var1;
        }

    }

    public void setMaxTransfer(int var1) {
        this.setMaxReceive(var1);
        this.setMaxExtract(var1);
    }

    public void setMaxReceive(int var1) {
        this.maxReceive = var1;
    }

    public void setMaxExtract(int var1) {
        this.maxExtract = var1;
    }

    public int getMaxReceive() {
        return this.maxReceive;
    }

    public int getMaxExtract() {
        return this.maxExtract;
    }

    public void setEnergyStored(int var1) {
        this.energy = var1;
        if (this.energy > this.capacity) {
            this.energy = this.capacity;
        } else if (this.energy < 0) {
            this.energy = 0;
        }

    }

    public void modifyEnergyStored(int var1) {
        this.energy += var1;
        if (this.energy > this.capacity) {
            this.energy = this.capacity;
        } else if (this.energy < 0) {
            this.energy = 0;
        }

    }

    public int receiveEnergy(int var1, boolean var2) {
        int var3 = Math.min(this.capacity - this.energy, Math.min(this.maxReceive, var1));
        if (!var2) {
            this.energy += var3;
        }

        return var3;
    }

    public int extractEnergy(int var1, boolean var2) {
        int var3 = Math.min(this.energy, Math.min(this.maxExtract, var1));
        if (!var2) {
            this.energy -= var3;
        }

        return var3;
    }

    public int getEnergyStored() {
        return this.energy;
    }

    public int getMaxEnergyStored() {
        return this.capacity;
    }
}
