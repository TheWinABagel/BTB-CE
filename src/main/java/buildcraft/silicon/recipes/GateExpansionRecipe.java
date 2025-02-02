/*
 * Copyright (c) SpaceToad, 2011-2012
 * http://www.mod-buildcraft.com
 * 
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.silicon.recipes;

import buildcraft.api.gates.IGateExpansion;
import buildcraft.api.recipes.IIntegrationRecipeManager.IIntegrationRecipe;
import buildcraft.core.inventory.StackHelper;
import buildcraft.transport.gates.ItemGate;
import net.minecraft.src.ItemStack;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public class GateExpansionRecipe implements IIntegrationRecipe {

	private final IGateExpansion expansion;
	private final ItemStack chipset;
	private final ItemStack[] exampleA;
	private final ItemStack[] exampleB;

	public GateExpansionRecipe(IGateExpansion expansion, ItemStack chipset) {
		this.expansion = expansion;
		this.chipset = chipset.copy();
		exampleA = ItemGate.getGateVarients();
		exampleB = new ItemStack[]{chipset};
	}

	@Override
	public double getEnergyCost() {
		return 10000;
	}

	@Override
	public boolean isValidInputA(ItemStack inputA) {
		if (inputA == null)
			return false;
		if (!(inputA.getItem() instanceof ItemGate))
			return false;
		return !ItemGate.hasGateExpansion(inputA, expansion);
	}

	@Override
	public boolean isValidInputB(ItemStack inputB) {
		return StackHelper.instance().isMatchingItem(inputB, chipset);
	}

	@Override
	public ItemStack getOutputForInputs(ItemStack inputA, ItemStack inputB) {
		if (!isValidInputA(inputA))
			return null;
		if (!isValidInputB(inputB))
			return null;
		ItemStack output = inputA.copy();
		output.stackSize = 1;
		ItemGate.addGateExpansion(output, expansion);
		return output;
	}

	@Override
	public ItemStack[] getExampleInputsA() {
		return exampleA;
	}

	@Override
	public ItemStack[] getExampleInputsB() {
		return exampleB;
	}
}
