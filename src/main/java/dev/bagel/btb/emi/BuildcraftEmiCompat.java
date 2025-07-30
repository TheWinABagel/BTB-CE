package dev.bagel.btb.emi;

import buildcraft.BuildCraftEnergy;
import buildcraft.BuildCraftFactory;
import buildcraft.BuildCraftSilicon;
import buildcraft.BuildCraftTransport;
import buildcraft.api.recipes.IAssemblyRecipeManager;
import buildcraft.api.recipes.IIntegrationRecipeManager;
import buildcraft.core.recipes.AssemblyRecipeManager;
import buildcraft.core.recipes.IntegrationRecipeManager;
import buildcraft.core.recipes.RefineryRecipeManager;
import buildcraft.core.utils.BCLog;
import buildcraft.energy.gui.GuiCombustionEngine;
import buildcraft.energy.gui.GuiStoneEngine;
import buildcraft.silicon.gui.GuiAdvancedCraftingTable;
import buildcraft.silicon.gui.GuiAssemblyTable;
import buildcraft.silicon.gui.GuiIntegrationTable;
import buildcraft.transport.ItemFacade;
import buildcraft.transport.gui.GuiDiamondPipe;
import buildcraft.transport.gui.GuiEmeraldPipe;
import buildcraft.transport.gui.GuiEmzuliPipe;
import buildcraft.transport.gui.GuiFilteredBuffer;
import dev.bagel.btb.emi.fluid.FluidEmiStack;
import dev.bagel.btb.emi.recipes.AssemblyTableEMIRecipe;
import dev.bagel.btb.emi.recipes.IntegrationTableEMIRecipe;
import dev.bagel.btb.emi.recipes.RefineryEMIRecipe;
import emi.dev.emi.emi.api.EmiPlugin;
import emi.dev.emi.emi.api.EmiRegistry;
import emi.dev.emi.emi.api.recipe.EmiRecipeCategory;
import emi.dev.emi.emi.api.stack.Comparison;
import emi.dev.emi.emi.api.stack.EmiStack;
import emi.dev.emi.emi.data.EmiRemoveFromIndex;
import emi.dev.emi.emi.data.IndexStackData;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;

import java.util.*;

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
        var comp = Comparison.of((a, b) -> {
            NBTTagCompound an = a.getNbt();
            NBTTagCompound bn = b.getNbt();
            if (an == null || bn == null) {
                return an == bn;
            }
            return an.equals(bn);
        });
        reg.setDefaultComparison(EmiStack.of(BuildCraftTransport.facadeItem), comp);
        EmiStack normalFacade = EmiStack.of(BuildCraftTransport.facadeItem);
        var facades = new LinkedList<>(ItemFacade.allFacades);
        Collections.reverse(facades);
        facades.removeLast();
        facades.forEach(itemStack -> {
            reg.addEmiStackAfter(EmiStack.of(itemStack).comparison(comp), normalFacade);
        });

        List<ItemStack> gates = new ArrayList<>();
        BuildCraftTransport.pipeGate.getSubItems(0, null, gates);
        EmiStack normalGate = EmiStack.of(BuildCraftTransport.pipeGate);
        Collections.reverse(gates);
        gates.remove(gates.size() - 1);
        gates.forEach(itemStack -> {
            reg.addEmiStackAfter(EmiStack.of(itemStack).comparison(comp), normalGate);
        });

        reg.addCategory(ASSEMBLY_TABLE);
        reg.addWorkstation(ASSEMBLY_TABLE, EmiStack.of(BuildCraftSilicon.assemblyTableBlock));
        reg.addCategory(INTEGRATION_TABLE);
        reg.addWorkstation(INTEGRATION_TABLE, EmiStack.of(BuildCraftSilicon.integrationTableBlock));
        reg.addCategory(REFINERY);
        reg.addWorkstation(REFINERY, EmiStack.of(BuildCraftFactory.refineryBlock));

        EmiUtils.addClickStackHandler(GuiDiamondPipe.class, reg);
        EmiUtils.addClickStackHandler(GuiEmeraldPipe.class, reg);
        EmiUtils.addClickStackHandler(GuiEmzuliPipe.class, reg);
        EmiUtils.addClickStackHandler(GuiAdvancedCraftingTable.class, reg);
        EmiUtils.addClickStackHandler(GuiFilteredBuffer.class, reg);

        EmiRemoveFromIndex.added.add(new IndexStackData.Added(FluidEmiStack.of(BuildCraftEnergy.fluidOil, 2500), EmiStack.EMPTY));
        EmiRemoveFromIndex.added.add(new IndexStackData.Added(FluidEmiStack.of(BuildCraftEnergy.fluidFuel), EmiStack.EMPTY));

        for (IAssemblyRecipeManager.IAssemblyRecipe recipe : AssemblyRecipeManager.INSTANCE.getRecipes()) {
            EmiUtils.addRecipeSafe(reg, () -> new AssemblyTableEMIRecipe(recipe));
        }

        for (RefineryRecipeManager.RefineryRecipe recipe : RefineryRecipeManager.INSTANCE.getRecipes()) {
            EmiUtils.addRecipeSafe(reg, () -> new RefineryEMIRecipe(recipe));
        }

        for (IIntegrationRecipeManager.IIntegrationRecipe recipe : IntegrationRecipeManager.INSTANCE.getRecipes()) {
            EmiUtils.addRecipeSafe(reg, () -> new IntegrationTableEMIRecipe(recipe));
        }

    }




}
