package buildcraft.core.lib.gui;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Container;
import net.minecraft.src.IInventory;

public class ContainerDummy extends Container {

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return false;
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventory) {}
}
