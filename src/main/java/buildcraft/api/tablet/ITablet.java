package buildcraft.api.tablet;

import net.fabricmc.api.EnvType;
import net.minecraft.src.NBTTagCompound;

public interface ITablet {

    EnvType getSide();

    void refreshScreen(TabletBitmap data);

    int getScreenWidth();

    int getScreenHeight();

    void launchProgram(String name);

    void sendMessage(NBTTagCompound compound);
}
