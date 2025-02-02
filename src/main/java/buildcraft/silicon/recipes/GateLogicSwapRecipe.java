/*
 * Copyright (c) SpaceToad, 2011-2012
 * http://www.mod-buildcraft.com
 * 
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.silicon.recipes;

import buildcraft.api.recipes.IIntegrationRecipeManager.IIntegrationRecipe;
import buildcraft.silicon.ItemRedstoneChipset;
import buildcraft.core.inventory.StackHelper;
import buildcraft.transport.gates.GateDefinition.GateLogic;
import buildcraft.transport.gates.GateDefinition.GateMaterial;
import buildcraft.transport.gates.ItemGate;
import net.minecraft.src.ItemStack;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public class GateLogicSwapRecipe implements IIntegrationRecipe {

	private final GateMaterial material;
	private final GateLogic logicIn, logicOut;
	private final ItemStack chipset;
	private final ItemStack[] exampleA;
	private final ItemStack[] exampleB;

	public GateLogicSwapRecipe(GateMaterial material, GateLogic logicIn, GateLogic logicOut) {
		this.material = material;
		this.logicIn = logicIn;
		this.logicOut = logicOut;
		this.chipset = ItemRedstoneChipset.Chipset.RED.getStack();
		exampleA = new ItemStack[]{ItemGate.makeGateItem(material, logicIn)};
		exampleB = new ItemStack[]{chipset};
	}

	@Override
	public double getEnergyCost() {
		return 2000;
	}

	@Override
	public boolean isValidInputA(ItemStack inputA) {
		if (inputA == null)
			return false;
		if (!(inputA.getItem() instanceof ItemGate))
			return false;
		if (ItemGate.getMaterial(inputA) != material)
			return false;
		if (ItemGate.getLogic(inputA) != logicIn)
			return false;
		return true;
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
		ItemGate.setLogic(output, logicOut);
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
