package cpw.mods.fml.common.network;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;

public interface IGuiHandler {
    Object getServerGuiElement(int modGuiId, EntityPlayer entityPlayer, World world, int x, int y, int z);

    Object getClientGuiElement(int modGuiId, EntityPlayer entityPlayer, World world, int x, int y, int z);

    int getId();
}
