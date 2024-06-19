package buildcraft.core;

import buildcraft.BuildCraftCore;
import buildcraft.api.recipes.BuildcraftRecipeRegistry;
import buildcraft.core.lib.utils.Utils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public final class CoreSiliconRecipes {

    private CoreSiliconRecipes() {}

    public static void loadSiliconRecipes() {
        // Lists
        if (FabricLoader.getInstance().isModLoaded("BuildCraft|Silicon"))
        if (Utils.isRegistered(BuildCraftCore.listItem)) {
            BuildcraftRecipeRegistry.assemblyTable.addRecipe(
                    "buildcraft:list",
                    20000,
                    new ItemStack(BuildCraftCore.listItem, 1, 1),
                    "dyeGreen",
                    "dustRedstone",
                    new ItemStack(Item.paper, 8));
        }
    }
}
