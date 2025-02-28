package dev.bagel.btb.emi.recipes;

import buildcraft.api.recipes.IIntegrationRecipeManager;
import dev.bagel.btb.emi.BuildcraftEmiCompat;
import emi.dev.emi.emi.api.recipe.EmiRecipe;
import emi.dev.emi.emi.api.recipe.EmiRecipeCategory;
import emi.dev.emi.emi.api.stack.EmiIngredient;
import emi.dev.emi.emi.api.stack.EmiStack;
import emi.dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.src.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IntegrationTableEMIRecipe implements EmiRecipe {
    private final IIntegrationRecipeManager.IIntegrationRecipe recipe;
    private final ResourceLocation id;
    private final List<EmiIngredient> inputOne = new ArrayList<>();
    private final List<EmiIngredient> inputTwo = new ArrayList<>();
    private final EmiStack output;

    public IntegrationTableEMIRecipe(IIntegrationRecipeManager.IIntegrationRecipe recipe) {
        this.recipe = recipe;
        this.id = recipe.getId();
        inputOne.addAll(Arrays.stream(recipe.getExampleInputsA()).map(EmiStack::of).toList());
        inputTwo.addAll(Arrays.stream(recipe.getExampleInputsB()).map(EmiStack::of).toList());
//        this.output = recipe.
        this.output = EmiStack.EMPTY;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return BuildcraftEmiCompat.INTEGRATION_TABLE;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(EmiIngredient.of(inputOne), EmiIngredient.of(inputTwo));
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public int getDisplayWidth() {
        return 80;
    }

    @Override
    public int getDisplayHeight() {
        return 60;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(EmiIngredient.of(inputOne), 0, 0);
        widgets.addSlot(EmiIngredient.of(inputTwo), 30, 0);
        widgets.addFillingArrow(20, 20, 1000);
        widgets.addSlot(output, 62, 0).recipeContext(this);
    }
}
