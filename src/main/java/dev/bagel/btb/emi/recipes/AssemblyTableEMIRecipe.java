package dev.bagel.btb.emi.recipes;

import dev.bagel.btb.emi.BuildcraftEmiCompat;
import buildcraft.api.recipes.IAssemblyRecipeManager;
import buildcraft.core.recipes.AssemblyRecipeManager;
import emi.dev.emi.emi.api.recipe.EmiRecipe;
import emi.dev.emi.emi.api.recipe.EmiRecipeCategory;
import emi.dev.emi.emi.api.stack.EmiIngredient;
import emi.dev.emi.emi.api.stack.EmiStack;
import emi.dev.emi.emi.api.widget.WidgetHolder;
import emi.shims.java.com.unascribed.retroemi.RetroEMI;
import emi.shims.java.net.minecraft.util.SyntheticIdentifier;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AssemblyTableEMIRecipe implements EmiRecipe {
    private final IAssemblyRecipeManager.IAssemblyRecipe recipe;
    private final List<EmiIngredient> inputs;
    private final EmiStack output;
    public AssemblyTableEMIRecipe(AssemblyRecipeManager.IAssemblyRecipe recipe) {
        this.recipe = recipe;
        this.inputs = fixIngredients(recipe.getProcessedInputs());
        this.output = EmiStack.of(recipe.getOutput());
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return BuildcraftEmiCompat.ASSEMBLY_TABLE;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return new SyntheticIdentifier(recipe);
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return this.inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public int getDisplayWidth() {
        return 120;
    }

    @Override
    public int getDisplayHeight() {
        return 50;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(output, 80, 0);
        int i = 0;
        for (EmiIngredient ingredient : this.inputs) {
            widgets.addSlot(ingredient, 20 * i, 25);
            i++;
        }
    }
    private static List<EmiIngredient> fixIngredients(Object[] inputs) {

        List<EmiIngredient> list = Arrays.stream(inputs).filter(Objects::nonNull)
                .map(o -> {
                    if (o instanceof ItemStack stack) {
                        return stack;
                    }
                    else if (o instanceof Integer integer) {
                        return new ItemStack(Item.itemsList[integer]);
                    }
                    else return null;
                })
                .map(RetroEMI::wildcardIngredientWithStackSize)
                .toList();

        return list;
    }
}
