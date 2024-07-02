package btw.community.example.emi;

import btw.community.example.emi.recipes.AssemblyTableEMIRecipe;
import buildcraft.BuildCraftFactory;
import buildcraft.BuildCraftSilicon;
import buildcraft.api.recipes.BuildcraftRecipes;
import buildcraft.api.recipes.IAssemblyRecipeManager;
import buildcraft.core.gui.GuiBuildCraft;
import buildcraft.core.gui.slots.SlotPhantom;
import buildcraft.core.recipes.AssemblyRecipeManager;
import buildcraft.core.utils.BCLog;
import buildcraft.energy.gui.GuiCombustionEngine;
import buildcraft.energy.gui.GuiStoneEngine;
import buildcraft.silicon.gui.GuiAdvancedCraftingTable;
import buildcraft.silicon.gui.GuiAssemblyTable;
import buildcraft.silicon.gui.GuiIntegrationTable;
import buildcraft.transport.gui.GuiDiamondPipe;
import buildcraft.transport.gui.GuiEmeraldPipe;
import buildcraft.transport.gui.GuiEmzuliPipe;
import buildcraft.transport.gui.GuiFilteredBuffer;
import emi.dev.emi.emi.api.EmiDragDropHandler;
import emi.dev.emi.emi.api.EmiExclusionArea;
import emi.dev.emi.emi.api.EmiPlugin;
import emi.dev.emi.emi.api.EmiRegistry;
import emi.dev.emi.emi.api.recipe.EmiRecipeCategory;
import emi.dev.emi.emi.api.stack.EmiStack;
import emi.dev.emi.emi.screen.Bounds;
import net.minecraft.src.Minecraft;
import net.minecraft.src.Slot;

public class BuildcraftEmiCompat implements EmiPlugin {
    public static EmiRecipeCategory ASSEMBLY_TABLE = EmiUtils.category("asssembly_table", EmiStack.of(BuildCraftSilicon.assemblyTableBlock));
    public static EmiRecipeCategory INTEGRATION_TABLE = EmiUtils.category("integration_table", EmiStack.of(BuildCraftSilicon.assemblyTableBlock));
    public static EmiRecipeCategory REFINERY = EmiUtils.category("refinery", EmiStack.of(BuildCraftFactory.refineryBlock));

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

        EmiUtils.addClickStackHandler(GuiDiamondPipe.class, reg);
        EmiUtils.addClickStackHandler(GuiEmeraldPipe.class, reg);
        EmiUtils.addClickStackHandler(GuiEmzuliPipe.class, reg);
        EmiUtils.addClickStackHandler(GuiAdvancedCraftingTable.class, reg);
        EmiUtils.addClickStackHandler(GuiFilteredBuffer.class, reg);
/*        reg.addDragDropHandler(GuiDiamondPipe.class, (gui, stack, x, y) -> {
            Slot slot = gui.getSlotAtPosition(x, y);
            if (slot instanceof SlotPhantom phantom) {
                gui.container.slotClickPhantom(phantom, 1, 0, Minecraft.getMinecraft().thePlayer, stack.getEmiStacks().get(0).getItemStack());
                return true;
            }
            return false;
        });*/


        for (IAssemblyRecipeManager.IAssemblyRecipe recipe : AssemblyRecipeManager.INSTANCE.getRecipes()) {
            EmiUtils.addRecipeSafe(reg, () -> new AssemblyTableEMIRecipe(recipe));
        }

    }




}
