package buildcraft;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.TextureMap;

public interface IBuildcraftModule {

    default void preInit() {

    }

    default void postInit() {

    }



    default void initRecipes() {

    }

    default void postSetup() {

    }

    void init();

    void handleConfigProps();

    void registerConfigForSettings(BuildCraftAddon addon);

    void registerConfigForIds(BuildCraftAddon addon);

    @Environment(EnvType.CLIENT)
    default void textureHook(TextureMap map) {

    }

    String getModId();
}
