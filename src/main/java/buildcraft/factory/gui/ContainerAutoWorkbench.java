/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.factory.gui;

import buildcraft.core.gui.BuildCraftContainer;
import buildcraft.core.gui.slots.SlotOutput;
import buildcraft.core.gui.slots.SlotUntouchable;
import buildcraft.core.gui.slots.SlotWorkbench;
import buildcraft.core.inventory.InvUtils;
import buildcraft.core.proxy.CoreProxy;
import buildcraft.core.utils.StringUtils;
import buildcraft.factory.TileAutoWorkbench;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ICrafting;
import net.minecraft.src.IInventory;
import net.minecraft.src.InventoryCraftResult;
import net.minecraft.src.Slot;
import net.minecraft.src.ItemStack;
import net.minecraft.src.EnumChatFormatting;

public class ContainerAutoWorkbench extends BuildCraftContainer {

	private final TileAutoWorkbench tile;
	public IInventory craftResult;
	private int lastProgress;
	private ItemStack prevOutput;

	public ContainerAutoWorkbench(InventoryPlayer inventoryplayer, TileAutoWorkbench t) {
		super(t.getSizeInventory());

		craftResult = new InventoryCraftResult() {
			@Override
			public void setInventorySlotContents(int slot, ItemStack stack) {
				super.setInventorySlotContents(slot, stack);
				if (stack != null && tile.isLast() && CoreProxy.getProxy().isClientWorld(tile.worldObj)) {
					InvUtils.addItemToolTip(stack, "tip", EnumChatFormatting.YELLOW + StringUtils.localize("gui.clickcraft"));
				}
			}
		};
		this.tile = t;
		addSlotToContainer(new SlotUntouchable(craftResult, 0, 93, 27) {
			@Override
			public void onPickupFromSlot(EntityPlayer player, ItemStack itemstack) {
				tile.useLast = true;
			}
		});
		addSlotToContainer(new SlotOutput(tile, TileAutoWorkbench.SLOT_RESULT, 124, 35));
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				addSlotToContainer(new SlotWorkbench(tile.craftMatrix, x + y * 3, 30 + x * 18, 17 + y * 18));
			}
		}

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				addSlotToContainer(new Slot(inventoryplayer, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
			}
		}

		for (int x = 0; x < 9; x++) {
			addSlotToContainer(new Slot(inventoryplayer, x, 8 + x * 18, 142));
		}

		onCraftMatrixChanged(tile);
	}

	@Override
	public void onCraftGuiOpened(ICrafting player) {
		super.onCraftGuiOpened(player);
		player.sendProgressBarUpdate(this, 0, tile.progress);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (int i = 0; i < crafters.size(); i++) {
			ICrafting icrafting = (ICrafting) crafters.get(i);

			if (lastProgress != tile.progress) {
				icrafting.sendProgressBarUpdate(this, 0, tile.progress);
			}
		}

		ItemStack output = craftResult.getStackInSlot(0);
		if (output != prevOutput) {
			prevOutput = output;
			onCraftMatrixChanged(tile.craftMatrix);
		}

		lastProgress = tile.progress;
	}

	@Override
	public void updateProgressBar(int id, int data) {
		switch (id) {
			case 0:
				tile.progress = data;
				break;
		}
	}

	@Override
	public final void onCraftMatrixChanged(IInventory inv) {
		super.onCraftMatrixChanged(inv);
		ItemStack output = tile.findRecipeOutput();
		craftResult.setInventorySlotContents(0, output);
	}

	@Override
	public ItemStack slotClick(int i, int j, int modifier, EntityPlayer entityplayer) {
		ItemStack stack = super.slotClick(i, j, modifier, entityplayer);
		onCraftMatrixChanged(tile.craftMatrix);
		return stack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return tile.isUseableByPlayer(entityplayer);
	}
}
