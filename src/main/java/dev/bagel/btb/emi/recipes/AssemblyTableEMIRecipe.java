package dev.bagel.btb.emi.recipes;

import btw.item.tag.TagOrStack;
import buildcraft.api.recipes.IAssemblyRecipeManager;
import buildcraft.core.recipes.AssemblyRecipeManager;
import dev.bagel.btb.emi.BuildcraftEmiCompat;
import emi.dev.emi.emi.api.recipe.EmiRecipe;
import emi.dev.emi.emi.api.recipe.EmiRecipeCategory;
import emi.dev.emi.emi.api.stack.EmiIngredient;
import emi.dev.emi.emi.api.stack.EmiStack;
import emi.dev.emi.emi.api.widget.WidgetHolder;
import emi.shims.java.com.unascribed.retroemi.ItemStacks;
import emi.shims.java.com.unascribed.retroemi.RetroEMI;
import emi.shims.java.net.minecraft.text.Text;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ResourceLocation;

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
    public ResourceLocation getId() {
        return recipe.getId();
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
        return 130;
    }

    @Override
    public int getDisplayHeight() {
        return 46;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(output, 107, 14).recipeContext(this);
        widgets.addFillingArrow(77, 15, (int) recipe.getEnergyCost() * 3).tooltipText(List.of(Text.literal("todo replace, cost: " + recipe.getEnergyCost())));
        for (int i = 0; i < Math.max(this.inputs.size(), 8); ++i) {
            if (i < this.inputs.size()) {
                widgets.addSlot(this.inputs.get(i), i % 4 * 18, 4 + i / 4 * 18);
            }
            else {
                widgets.addSlot(EmiStack.of(ItemStacks.EMPTY), i % 4 * 18, 4 + i / 4 * 18);
            }
        }
    }
    private static List<EmiIngredient> fixIngredients(Object[] inputs) {

        List<EmiIngredient> list = Arrays.stream(inputs).filter(Objects::nonNull)
                .map(o -> {
                    if (o instanceof TagOrStack tagOrStack) {
                        return tagOrStack;
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
