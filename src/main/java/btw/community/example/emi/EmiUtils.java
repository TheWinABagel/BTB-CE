package btw.community.example.emi;

import buildcraft.core.gui.GuiBuildCraft;
import buildcraft.core.gui.slots.SlotPhantom;
import buildcraft.transport.gui.GuiDiamondPipe;
import emi.dev.emi.emi.api.EmiRegistry;
import emi.dev.emi.emi.api.recipe.EmiRecipe;
import emi.dev.emi.emi.api.recipe.EmiRecipeCategory;
import emi.dev.emi.emi.api.render.EmiTexture;
import emi.dev.emi.emi.api.stack.EmiStack;
import emi.dev.emi.emi.runtime.EmiReloadLog;
import emi.dev.emi.emi.screen.Bounds;
import net.minecraft.src.IRecipe;
import net.minecraft.src.Minecraft;
import net.minecraft.src.ResourceLocation;
import net.minecraft.src.Slot;

import java.util.Comparator;
import java.util.function.Supplier;

public class EmiUtils {

    public static EmiRecipeCategory category(String id, EmiStack icon) {
        return new EmiRecipeCategory(new ResourceLocation("buildcraft", id), icon,
                icon);
//                new EmiTexture(new ResourceLocation("emi", "textures/simple_icons/" + id + ".png"), 0, 0, 16, 16, 16, 16, 16, 16));
    }

    public static EmiRecipeCategory category(String id, EmiStack icon, Comparator<EmiRecipe> comp) {
        return new EmiRecipeCategory(new ResourceLocation("buildcraft", id), icon,
                icon, comp);
//                new EmiTexture(new ResourceLocation("emi", "textures/simple_icons/" + id + ".png"), 0, 0, 16, 16, 16, 16, 16, 16), comp);
    }

    public static <T extends GuiBuildCraft> void addExclusion(Class<T> clazz, EmiRegistry reg) {
        reg.addExclusionArea(clazz, (screen, bounds) -> {
            int i = 1;
            for (GuiBuildCraft.Ledger ledger : screen.ledgerManager.ledgers) {
                bounds.accept(new Bounds( screen.getGuiLeft() + screen.getxSize(), screen.getGuiTop() + (8 * i), ledger.getCurrentWidth(), ledger.getHeight()));
                i++;
            }
        });
    }

    public static <T extends GuiBuildCraft> void addClickStackHandler(Class<T> clazz, EmiRegistry reg) {
        reg.addDragDropHandler(clazz, (gui, stack, x, y) -> {
            Slot slot = gui.getSlotAtPosition(x, y);
            if (slot instanceof SlotPhantom phantom) {
                gui.container.slotClickPhantom(phantom, 0, 0, Minecraft.getMinecraft().thePlayer, stack.getEmiStacks().get(0).getItemStack());
                System.out.println("clicked a phantom slot");
                return true;
            }
            return false;
        });
    }

    public static void addRecipeSafe(EmiRegistry registry, Supplier<EmiRecipe> supplier) {
        try {
            registry.addRecipe(supplier.get());
        }
        catch (Throwable e) {
            EmiReloadLog.warn("Exception when parsing EMI recipe (no ID available)");
            EmiReloadLog.error(e);
        }
    }

    public static void addRecipeSafe(EmiRegistry registry, Supplier<EmiRecipe> supplier, IRecipe recipe) {
        try {
            registry.addRecipe(supplier.get());
        }
        catch (Throwable e) {
            EmiReloadLog.warn("Exception when parsing BTW recipe " + recipe);
            EmiReloadLog.error(e);
        }
    }
}
