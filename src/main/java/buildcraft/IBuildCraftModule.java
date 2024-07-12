package buildcraft;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.TextureMap;

public interface IBuildCraftModule {

    default void preInit() {

    }

    default void postInit() {

    }

    default void postSetup() {

    }

    @Environment(EnvType.CLIENT)
    default void textureHook(TextureMap map) {

    }

    void init();

    void handleConfigProps();

    void registerConfigForSettings(BuildCraftAddon addon);

    void registerConfigForIds(BuildCraftAddon addon);

    void initRecipes();

    String getModId();
}
