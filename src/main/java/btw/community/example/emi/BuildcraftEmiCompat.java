package btw.community.example.emi;

import btw.community.example.emi.recipes.AssemblyTableEMIRecipe;
import buildcraft.BuildCraftSilicon;
import buildcraft.api.recipes.BuildcraftRecipes;
import buildcraft.api.recipes.IAssemblyRecipeManager;
import buildcraft.core.gui.GuiBuildCraft;
import buildcraft.core.recipes.AssemblyRecipeManager;
import buildcraft.core.utils.BCLog;
import buildcraft.energy.gui.GuiCombustionEngine;
import buildcraft.energy.gui.GuiStoneEngine;
import buildcraft.silicon.gui.GuiAdvancedCraftingTable;
import buildcraft.silicon.gui.GuiAssemblyTable;
import buildcraft.silicon.gui.GuiIntegrationTable;
import emi.dev.emi.emi.api.EmiExclusionArea;
import emi.dev.emi.emi.api.EmiPlugin;
import emi.dev.emi.emi.api.EmiRegistry;
import emi.dev.emi.emi.api.recipe.EmiRecipeCategory;
import emi.dev.emi.emi.api.stack.EmiStack;
import emi.dev.emi.emi.screen.Bounds;

public class BuildcraftEmiCompat implements EmiPlugin {
    public static EmiRecipeCategory ASSEMBLY_TABLE = EmiUtils.category("asssembly_table", EmiStack.of(BuildCraftSilicon.assemblyTableBlock));
    public static EmiRecipeCategory INTEGRATION_TABLE;
    public static EmiRecipeCategory REFINERY;

    @Override
    public void register(EmiRegistry reg) {
        BCLog.logger.fine("Initializing Buildcraft EMI compat.");
        EmiUtils.addExclusion(GuiAssemblyTable.class, reg);
        EmiUtils.addExclusion(GuiCombustionEngine.class, reg);
        EmiUtils.addExclusion(GuiStoneEngine.class, reg);
        EmiUtils.addExclusion(GuiAdvancedCraftingTable.class, reg);
        EmiUtils.addExclusion(GuiIntegrationTable.class, reg);

        reg.addCategory(ASSEMBLY_TABLE);

        reg.addWorkstation(ASSEMBLY_TABLE, EmiStack.of(BuildCraftSilicon.assemblyTableBlock));
        //todocore DRAG DROP HANDLER for bc pipe
//        reg.addDragDropHandler();


        for (IAssemblyRecipeManager.IAssemblyRecipe recipe : AssemblyRecipeManager.INSTANCE.getRecipes()) {
            EmiUtils.addRecipeSafe(reg, () -> new AssemblyTableEMIRecipe(recipe));
        }

    }




}
