/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.gui;

import buildcraft.core.gui.slots.IPhantomSlot;
import buildcraft.core.gui.slots.SlotBase;
import buildcraft.core.gui.widgets.Widget;
import buildcraft.core.inventory.StackHelper;
import buildcraft.core.network.PacketGuiWidget;
import emi.dev.emi.emi.api.stack.EmiStack;
import emi.dev.emi.emi.screen.EmiScreenManager;
import net.minecraft.src.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class BuildCraftContainer extends Container {

	private final List<Widget> widgets = new ArrayList<>();
	private final int inventorySize;

	public BuildCraftContainer(int inventorySize) {
		this.inventorySize = inventorySize;
	}

	public List<Widget> getWidgets() {
		return widgets;
	}

	public void addSlot(Slot slot) {
		addSlotToContainer(slot);
	}

	public void addWidget(Widget widget) {
		widget.addToContainer(this);
		widgets.add(widget);
	}

	public void sendWidgetDataToClient(Widget widget, ICrafting player, byte[] data) {
		PacketGuiWidget pkt = new PacketGuiWidget(windowId, widgets.indexOf(widget), data);
		if (player instanceof EntityPlayerMP) {
			((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(pkt.getPacket());
		}
	}

	public void handleWidgetClientData(int widgetId, DataInputStream data) throws IOException {
		widgets.get(widgetId).handleClientPacketData(data);
	}

	@Override
	public void onCraftGuiOpened(ICrafting player) {
		super.onCraftGuiOpened(player);
		for (Widget widget : widgets) {
			widget.initWidget(player);
		}
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (Widget widget : widgets) {
			for (ICrafting player : (List<ICrafting>) crafters) {
				widget.updateWidget(player);
			}
		}
	}

	@Override
	public ItemStack slotClick(int slotNum, int mouseButton, int modifier, EntityPlayer player) {
		Slot slot = slotNum < 0 ? null : (Slot) this.inventorySlots.get(slotNum);
		if (slot instanceof IPhantomSlot) {
			return slotClickPhantom(slot, mouseButton, modifier, player, player.inventory.getItemStack());
		}
		return super.slotClick(slotNum, mouseButton, modifier, player);
	}

	public ItemStack slotClickPhantom(Slot slot, int mouseButton, int modifier, EntityPlayer player, ItemStack stackHeld) {
		ItemStack stack = null;
		if (mouseButton == 2) {
			if (((IPhantomSlot) slot).canAdjust()) {
				slot.putStack(null);
			}
		} else if (mouseButton == 0 || mouseButton == 1) {
			InventoryPlayer playerInv = player.inventory;
			slot.onSlotChanged();
			ItemStack stackSlot = slot.getStack();
			if (stackSlot != null) {
				stack = stackSlot.copy();
			}

			if (stackSlot == null) {
				if (stackHeld != null && slot.isItemValid(stackHeld)) {
					fillPhantomSlot(slot, stackHeld, mouseButton, modifier);
				}
			} else if (stackHeld == null) {
				adjustPhantomSlot(slot, mouseButton, modifier);
				slot.onPickupFromSlot(player, playerInv.getItemStack());
			} else if (slot.isItemValid(stackHeld)) {
				if (StackHelper.instance().canStacksMerge(stackSlot, stackHeld)) {
					adjustPhantomSlot(slot, mouseButton, modifier);
				} else {
					fillPhantomSlot(slot, stackHeld, mouseButton, modifier);
				}
			}
		}
		return stack;
	}

	protected void adjustPhantomSlot(Slot slot, int mouseButton, int modifier) {
		if (!((IPhantomSlot) slot).canAdjust()) {
			return;
		}
		ItemStack stackSlot = slot.getStack();
		int stackSize;
		if (modifier == 1) {
			stackSize = mouseButton == 0 ? (stackSlot.stackSize + 1) / 2 : stackSlot.stackSize * 2;
		} else {
			stackSize = mouseButton == 0 ? stackSlot.stackSize - 1 : stackSlot.stackSize + 1;
		}

		if (stackSize > slot.getSlotStackLimit()) {
			stackSize = slot.getSlotStackLimit();
		}

		stackSlot.stackSize = stackSize;

		if (stackSlot.stackSize <= 0) {
			slot.putStack((ItemStack) null);
		}
	}

	protected void fillPhantomSlot(Slot slot, ItemStack stackHeld, int mouseButton, int modifier) {
		if (!((IPhantomSlot) slot).canAdjust()) {
			return;
		}
		int stackSize = mouseButton == 0 ? stackHeld.stackSize : 1;
		if (stackSize > slot.getSlotStackLimit()) {
			stackSize = slot.getSlotStackLimit();
		}
		ItemStack phantomStack = stackHeld.copy();
		phantomStack.stackSize = stackSize;

		slot.putStack(phantomStack);
	}

	protected boolean shiftItemStack(ItemStack stackToShift, int start, int end) {
		boolean changed = false;
		if (stackToShift.isStackable()) {
			for (int slotIndex = start; stackToShift.stackSize > 0 && slotIndex < end; slotIndex++) {
				Slot slot = (Slot) inventorySlots.get(slotIndex);
				ItemStack stackInSlot = slot.getStack();
				if (stackInSlot != null && StackHelper.instance().canStacksMerge(stackInSlot, stackToShift)) {
					int resultingStackSize = stackInSlot.stackSize + stackToShift.stackSize;
					int max = Math.min(stackToShift.getMaxStackSize(), slot.getSlotStackLimit());
					if (resultingStackSize <= max) {
						stackToShift.stackSize = 0;
						stackInSlot.stackSize = resultingStackSize;
						slot.onSlotChanged();
						changed = true;
					} else if (stackInSlot.stackSize < max) {
						stackToShift.stackSize -= max - stackInSlot.stackSize;
						stackInSlot.stackSize = max;
						slot.onSlotChanged();
						changed = true;
					}
				}
			}
		}
		if (stackToShift.stackSize > 0) {
			for (int slotIndex = start; stackToShift.stackSize > 0 && slotIndex < end; slotIndex++) {
				Slot slot = (Slot) inventorySlots.get(slotIndex);
				ItemStack stackInSlot = slot.getStack();
				if (stackInSlot == null) {
					int max = Math.min(stackToShift.getMaxStackSize(), slot.getSlotStackLimit());
					stackInSlot = stackToShift.copy();
					stackInSlot.stackSize = Math.min(stackToShift.stackSize, max);
					stackToShift.stackSize -= stackInSlot.stackSize;
					slot.putStack(stackInSlot);
					slot.onSlotChanged();
					changed = true;
				}
			}
		}
		return changed;
	}

	private boolean tryShiftItem(ItemStack stackToShift, int numSlots) {
		for (int machineIndex = 0; machineIndex < numSlots - 9 * 4; machineIndex++) {
			Slot slot = (Slot) inventorySlots.get(machineIndex);
			if (slot instanceof SlotBase && !((SlotBase) slot).canShift()) {
				continue;
			}
			if (slot instanceof IPhantomSlot) {
				continue;
			}
			if (!slot.isItemValid(stackToShift)) {
				continue;
			}
			if (shiftItemStack(stackToShift, machineIndex, machineIndex + 1)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack originalStack = null;
		Slot slot = (Slot) inventorySlots.get(slotIndex);
		int numSlots = inventorySlots.size();
		if (slot != null && slot.getHasStack()) {
			ItemStack stackInSlot = slot.getStack();
			originalStack = stackInSlot.copy();
			if (slotIndex >= numSlots - 9 * 4 && tryShiftItem(stackInSlot, numSlots)) {
				// NOOP
			} else if (slotIndex >= numSlots - 9 * 4 && slotIndex < numSlots - 9) {
				if (!shiftItemStack(stackInSlot, numSlots - 9, numSlots)) {
					return null;
				}
			} else if (slotIndex >= numSlots - 9 && slotIndex < numSlots) {
				if (!shiftItemStack(stackInSlot, numSlots - 9 * 4, numSlots - 9)) {
					return null;
				}
			} else if (!shiftItemStack(stackInSlot, numSlots - 9 * 4, numSlots)) {
				return null;
			}
			slot.onSlotChange(stackInSlot, originalStack);
			if (stackInSlot.stackSize <= 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
			if (stackInSlot.stackSize == originalStack.stackSize) {
				return null;
			}
			slot.onPickupFromSlot(player, stackInSlot);
		}
		return originalStack;
	}

	public int getInventorySize() {
		return inventorySize;
	}
}
