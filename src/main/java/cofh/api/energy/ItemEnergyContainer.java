package cofh.api.energy;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

public class ItemEnergyContainer extends Item implements IEnergyContainerItem {
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;

    public ItemEnergyContainer(int var1) {
        this(var1, var1, var1);
    }

    public ItemEnergyContainer(int var1, int var2) {
        this(var1, var2, var2);
    }

    public ItemEnergyContainer(int capacity, int maxReceive, int maxExtract) {
        super(3203); //todo itemId
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }

    public ItemEnergyContainer setCapacity(int var1) {
        this.capacity = var1;
        return this;
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

    public int receiveEnergy(ItemStack var1, int var2, boolean var3) {
        if (var1.stackTagCompound == null) {
            var1.stackTagCompound = new NBTTagCompound();
        }

        int var4 = var1.stackTagCompound.getInteger("Energy");
        int var5 = Math.min(this.capacity - var4, Math.min(this.maxReceive, var2));
        if (!var3) {
            var4 += var5;
            var1.stackTagCompound.setInteger("Energy", var4);
        }

        return var5;
    }

    public int extractEnergy(ItemStack var1, int var2, boolean var3) {
        if (var1.stackTagCompound != null && var1.stackTagCompound.hasKey("Energy")) {
            int var4 = var1.stackTagCompound.getInteger("Energy");
            int var5 = Math.min(var4, Math.min(this.maxExtract, var2));
            if (!var3) {
                var4 -= var5;
                var1.stackTagCompound.setInteger("Energy", var4);
            }

            return var5;
        } else {
            return 0;
        }
    }

    public int getEnergyStored(ItemStack var1) {
        return var1.stackTagCompound != null && var1.stackTagCompound.hasKey("Energy") ? var1.stackTagCompound.getInteger("Energy") : 0;
    }

    public int getMaxEnergyStored(ItemStack var1) {
        return this.capacity;
    }
}
