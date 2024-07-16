package dev.bagel.btb.injected;

import net.minecraft.src.World;

public interface EntityPlayerExtension {

    void btb$openGui(String mod, int modGuiId, World world, int x, int y, int z);
}
