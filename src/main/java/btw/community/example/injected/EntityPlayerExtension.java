package btw.community.example.injected;

import net.minecraft.src.World;

public interface EntityPlayerExtension {

    void openGui(String mod, int modGuiId, World world, int x, int y, int z);
}
