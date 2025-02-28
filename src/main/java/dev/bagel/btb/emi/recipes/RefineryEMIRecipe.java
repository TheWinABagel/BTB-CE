package dev.bagel.btb.emi.recipes;

import buildcraft.core.recipes.RefineryRecipeManager;
import dev.bagel.btb.emi.BuildcraftEmiCompat;
import dev.bagel.btb.emi.fluid.FluidEmiStack;
import emi.dev.emi.emi.api.recipe.EmiRecipe;
import emi.dev.emi.emi.api.recipe.EmiRecipeCategory;
import emi.dev.emi.emi.api.stack.EmiIngredient;
import emi.dev.emi.emi.api.stack.EmiStack;
import emi.dev.emi.emi.api.widget.WidgetHolder;
import emi.shims.java.net.minecraft.text.Text;
import net.minecraft.src.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RefineryEMIRecipe implements EmiRecipe {
    private final RefineryRecipeManager.RefineryRecipe recipe;
    private final EmiIngredient first;
    private final EmiIngredient second;
    private final EmiStack output;
    private final int energy;
    private final int time;

    public RefineryEMIRecipe(RefineryRecipeManager.RefineryRecipe recipe) {
        this.recipe = recipe;
        this.first = FluidEmiStack.of(recipe.ingredient1);
        this.second = FluidEmiStack.of(recipe.ingredient2);
        this.output = FluidEmiStack.of(recipe.result);
        this.energy = recipe.energyCost;
        this.time = recipe.timeRequired;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return BuildcraftEmiCompat.REFINERY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return recipe.getId();
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(this.first, this.second);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(this.output);
    }

    @Override
    public int getDisplayWidth() {
        return 70;
    }

    @Override
    public int getDisplayHeight() {
        return 80;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(first,10, 40);
        widgets.addSlot(second,40, 40);
        widgets.addSlot(output,22, 10).recipeContext(this);

        widgets.addText(Text.literal("Time: %d".formatted(time)), 0, 60, 0, true);
        widgets.addText(Text.literal("Energy: %d".formatted(this.energy)), 0, 70, 0, true);
    }
}
