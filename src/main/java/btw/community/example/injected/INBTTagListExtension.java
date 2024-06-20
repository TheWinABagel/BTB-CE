package btw.community.example.injected;

import net.minecraft.src.NBTTagCompound;

public interface INBTTagListExtension {
    NBTTagCompound getCompoundTagAt(int i);

    String getStringTagAt(int i);

    double getDoubleTagAt(int i);

    float getFloatTagAt(int i);
}
