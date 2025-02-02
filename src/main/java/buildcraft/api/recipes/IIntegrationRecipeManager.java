/*
 * Copyright (c) SpaceToad, 2011-2012
 * http://www.mod-buildcraft.com
 * 
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.api.recipes;

import net.minecraft.src.ItemStack;

import java.util.List;

/**
 * The Integration Table's primary purpose is to modify an input item's NBT
 * data. As such its not a "traditional" type of recipe. Rather than predefined
 * inputs and outputs, it takes an input and transforms it.
 *
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public interface IIntegrationRecipeManager {

	public static interface IIntegrationRecipe {

		double getEnergyCost();

		boolean isValidInputA(ItemStack inputA);

		boolean isValidInputB(ItemStack inputB);

		ItemStack getOutputForInputs(ItemStack inputA, ItemStack inputB);

		ItemStack[] getExampleInputsA();

		ItemStack[] getExampleInputsB();
	}

	/**
	 * Add an Integration Table recipe.
	 *
	 */
	void addRecipe(IIntegrationRecipe recipe);

	List<? extends IIntegrationRecipe> getRecipes();
}
