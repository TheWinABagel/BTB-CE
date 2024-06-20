/*
 * Copyright (c) SpaceToad, 2011-2012
 * http://www.mod-buildcraft.com
 * 
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.api.recipes;

import net.minecraft.item.ItemStack;

import java.util.List;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public interface IAssemblyRecipeManager {

	public static interface IAssemblyRecipe {

		ItemStack getOutput();

		Object[] getInputs();

		double getEnergyCost();
	}

	/**
	 * Add an Assembly Table recipe.
	 *
	 * @param input Object... containing either an ItemStack, or a paired string
	 * and integer(ex: "dyeBlue", 1)
	 * @param energy MJ cost to produce
	 * @param output resulting ItemStack
	 */
	void addRecipe(double energyCost, ItemStack output, Object... input);

	List<? extends IAssemblyRecipe> getRecipes();
}
