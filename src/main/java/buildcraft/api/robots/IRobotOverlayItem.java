package buildcraft.api.robots;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.TextureManager;
import net.minecraft.src.ItemStack;

public interface IRobotOverlayItem {

    boolean isValidRobotOverlay(ItemStack stack);

    @Environment(EnvType.CLIENT)
    void renderRobotOverlay(ItemStack stack, TextureManager textureManager);
}
