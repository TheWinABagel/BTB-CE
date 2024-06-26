package btw.community.example.emi;

import buildcraft.core.gui.GuiBuildCraft;
import buildcraft.energy.gui.GuiCombustionEngine;
import buildcraft.energy.gui.GuiStoneEngine;
import buildcraft.silicon.gui.GuiAdvancedCraftingTable;
import buildcraft.silicon.gui.GuiAssemblyTable;
import buildcraft.silicon.gui.GuiIntegrationTable;
import emi.dev.emi.emi.api.EmiExclusionArea;
import emi.dev.emi.emi.api.EmiPlugin;
import emi.dev.emi.emi.api.EmiRegistry;
import emi.dev.emi.emi.screen.Bounds;

public class BuildcraftEmiCompat implements EmiPlugin {
    @Override
    public void register(EmiRegistry reg) {
        System.out.println("REGISTERING BUILDCRAFT EMI COMPAT");
        addExclusion(GuiAssemblyTable.class, reg);
        addExclusion(GuiCombustionEngine.class, reg);
        addExclusion(GuiStoneEngine.class, reg);
        addExclusion(GuiAdvancedCraftingTable.class, reg);
        addExclusion(GuiIntegrationTable.class, reg);
    }

    private static <T extends GuiBuildCraft> void addExclusion(Class<T> clazz, EmiRegistry reg) {
        reg.addExclusionArea(clazz, (screen, bounds) -> {
            int i = 1;
            for (GuiBuildCraft.Ledger ledger : screen.ledgerManager.ledgers) {
                bounds.accept(new Bounds( screen.getGuiLeft() + screen.getxSize(), screen.getGuiTop() + (8 * i), ledger.getCurrentWidth(), ledger.getHeight()));
                i++;
            }
        });
    }
}
