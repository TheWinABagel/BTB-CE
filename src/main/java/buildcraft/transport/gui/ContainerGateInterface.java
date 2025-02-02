/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.transport.gui;

import buildcraft.api.gates.*;
import buildcraft.core.gui.BuildCraftContainer;
import buildcraft.core.network.*;
import buildcraft.core.proxy.CoreProxy;
import buildcraft.transport.Pipe;
import buildcraft.transport.gates.GateDefinition;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICrafting;
import net.minecraft.src.IInventory;
import net.minecraft.src.Slot;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet;
import net.minecraft.src.TileEntity;
import net.minecraft.src.ResourceLocation;
import net.minecraftforge.common.ForgeDirection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.TreeSet;

public class ContainerGateInterface extends BuildCraftContainer {

	IInventory playerIInventory;
	Pipe pipe;
	private final NavigableSet<ITrigger> _potentialTriggers = new TreeSet<ITrigger>(new Comparator<ITrigger>() {
		@Override
		public int compare(ITrigger o1, ITrigger o2) {
			return o1.getUniqueTag().compareTo(o2.getUniqueTag());
		}
	});
	private final NavigableSet<IAction> _potentialActions = new TreeSet<IAction>(new Comparator<IAction>() {
		@Override
		public int compare(IAction o1, IAction o2) {
			return o1.getUniqueTag().compareTo(o2.getUniqueTag());
		}
	});
	private boolean isSynchronized = false;
	private boolean isNetInitialized = false;
	public boolean[] triggerState = new boolean[8];
	private int lastTriggerState = 0;

	public ContainerGateInterface(IInventory playerInventory, Pipe pipe) {
		super(0);
		this.playerIInventory = playerInventory;

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, pipe.gate.material.guiHeight - 84 + y * 18));
			}
		}

		for (int x = 0; x < 9; x++) {
			addSlotToContainer(new Slot(playerInventory, x, 8 + x * 18, pipe.gate.material.guiHeight - 26));
		}

		this.pipe = pipe;

		// Do not attempt to create a list of potential actions and triggers on
		// the client.
		if (!CoreProxy.getProxy().isClientWorld(pipe.container.worldObj)) {
			_potentialActions.addAll(pipe.getActions());
			_potentialTriggers.addAll(ActionManager.getPipeTriggers(pipe.container));

			if (pipe.container instanceof IOverrideDefaultTriggers) {
				_potentialTriggers.addAll(((IOverrideDefaultTriggers) pipe.container).getTriggers());
			}

			for (ForgeDirection o : ForgeDirection.VALID_DIRECTIONS) {
				TileEntity tile = pipe.container.getTile(o);
				int blockID = pipe.container.getBlockId(o);
				Block block = Block.blocksList[blockID];
				_potentialTriggers.addAll(ActionManager.getNeighborTriggers(block, tile));
				_potentialActions.addAll(ActionManager.getNeighborActions(block, tile));
			}

			if (!pipe.gate.material.hasParameterSlot) {
				Iterator<ITrigger> it = _potentialTriggers.iterator();
				while (it.hasNext()) {
					ITrigger trigger = it.next();
					if (trigger.requiresParameter())
						it.remove();
				}
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		if (pipe == null || pipe.gate == null)
			return false;
		return true;
	}

	/**
	 * CLIENT SIDE *
	 */
	/**
	 * Marks client side gate container as needing to be synchronized with the
	 * server.
	 */
	public void markDirty() {
		isSynchronized = false;
	}

	/**
	 * Clears list of potential actions and refills it according to packet.
	 *
	 * @param packet
	 */
	public void updateActions(PacketUpdate packet) {
		_potentialActions.clear();
		PacketPayloadArrays payload = (PacketPayloadArrays) packet.payload;
		int length = payload.intPayload[0];

		for (int i = 0; i < length; i++) {
			_potentialActions.add(ActionManager.actions.get(payload.stringPayload[i]));
		}
	}

	/**
	 * Clears list of potential triggers and refills it according to packet.
	 *
	 * @param packet
	 */
	public void updateTriggers(PacketUpdate packet) {
		_potentialTriggers.clear();
		PacketPayloadArrays payload = (PacketPayloadArrays) packet.payload;
		int length = payload.intPayload[0];

		for (int i = 0; i < length; i++) {
			_potentialTriggers.add(ActionManager.triggers.get(payload.stringPayload[i]));
		}
	}

	/**
	 * Sets the currently selected actions and triggers according to packet.
	 *
	 * @param packet
	 */
	public void setSelection(PacketUpdate packet, boolean notify) {
		try {
			PacketPayloadStream payload = (PacketPayloadStream) packet.payload;
			DataInputStream data = payload.stream;

			int position = data.readInt();

			setTrigger(position, ActionManager.triggers.get(data.readUTF()), notify);
			setAction(position, ActionManager.actions.get(data.readUTF()), notify);

			ItemStack parameter = Packet.readItemStack(data);
			if (parameter != null) {
				ITriggerParameter param = new TriggerParameter();
				param.set(parameter);
				setTriggerParameter(position, param, notify);
			} else {
				setTriggerParameter(position, null, notify);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private PacketPayload getSelectionPayload(final int position) {
		PacketPayloadStream payload = new PacketPayloadStream(new PacketPayloadStream.StreamWriter() {
			@Override
			public void writeData(DataOutputStream data) throws IOException {
				data.writeInt(position);

				if (pipe.gate.triggers[position] != null) {
					data.writeUTF(pipe.gate.triggers[position].getUniqueTag());
				} else {
					data.writeUTF("");
				}

				if (pipe.gate.actions[position] != null) {
					data.writeUTF(pipe.gate.actions[position].getUniqueTag());
				} else {
					data.writeUTF("");
				}

				if (pipe.gate.triggerParameters[position] != null && pipe.gate.triggerParameters[position].getItemStack() != null) {
					Packet.writeItemStack(pipe.gate.triggerParameters[position].getItemStack().copy(), data);
				} else {
					Packet.writeItemStack(null, data);
				}
			}
		});

		return payload;
	}

	public void sendSelectionChange(int position) {
		CoreProxy.getProxy().sendToServer(new PacketUpdate(PacketIds.GATE_SELECTION_CHANGE, pipe.container.xCoord, pipe.container.yCoord, pipe.container.zCoord, getSelectionPayload(position)).getPacket());
	}

	/**
	 * Initializes the list of triggers and actions on the gate and
	 * (re-)requests the current selection on the gate if needed.
	 */
	public void synchronize() {

		if (!isNetInitialized && CoreProxy.getProxy().isClientWorld(pipe.container.worldObj)) {
			isNetInitialized = true;
			CoreProxy.getProxy().sendToServer(new PacketCoordinates(PacketIds.GATE_REQUEST_INIT, pipe.container.xCoord, pipe.container.yCoord, pipe.container.zCoord).getPacket());
		}

		if (!isSynchronized && CoreProxy.getProxy().isClientWorld(pipe.container.worldObj)) {
			isSynchronized = true;
			CoreProxy.getProxy().sendToServer(new PacketCoordinates(PacketIds.GATE_REQUEST_SELECTION, pipe.container.xCoord, pipe.container.yCoord, pipe.container.zCoord).getPacket());
		}
	}

	@Override
	public void updateProgressBar(int id, int state) {
		if (id == 0 /* Trigger state update */) {
			for (int i = 0; i < 8; i++) {
				/* Bit mask of triggers */
				triggerState[i] = ((state >> i) & 0x01) == 0x01;
			}
		}
	}

	/**
	 * SERVER SIDE *
	 */
	private int calculateTriggerState() {
		if (pipe.gate == null)
			return 0;
		int state = 0;
		for (int i = 0; i < triggerState.length; i++) {
			if (pipe.gate.triggers[i] != null) {
				triggerState[i] = isNearbyTriggerActive(pipe.gate.triggers[i], pipe.gate.getTriggerParameter(i));
			}
			state |= triggerState[i] ? 0x01 << i : 0x0;
		}
		return state;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		int state = calculateTriggerState();
		if (state != lastTriggerState) {
			for (int i = 0; i < this.crafters.size(); i++) {
				ICrafting viewingPlayer = (ICrafting) this.crafters.get(i);

				viewingPlayer.sendProgressBarUpdate(this, 0 /* State update */, state);
			}
			lastTriggerState = state;
		}
	}

	public void handleInitRequest(EntityPlayer player) {
		sendActions(player);
		sendTriggers(player);
		sendSelection(player);
	}

	/**
	 * Sends the list of potential actions to the client
	 *
	 * @param player
	 */
	private void sendActions(EntityPlayer player) {

		// Compose update packet
		int length = _potentialActions.size();
		PacketPayloadArrays payload = new PacketPayloadArrays(1, 0, length);

		payload.intPayload[0] = length;
		int i = 0;
		for (IAction action : _potentialActions) {
			payload.stringPayload[i++] = action.getUniqueTag();
		}

		PacketUpdate packet = new PacketUpdate(PacketIds.GATE_ACTIONS, pipe.container.xCoord, pipe.container.yCoord, pipe.container.zCoord, payload);

		// Send to player
		CoreProxy.getProxy().sendToPlayer(player, packet);
	}

	/**
	 * Sends the list of potential triggers to the client
	 *
	 * @param player
	 */
	private void sendTriggers(EntityPlayer player) {

		// Compose update packet
		int length = _potentialTriggers.size();
		PacketPayloadArrays payload = new PacketPayloadArrays(1, 0, length);

		payload.intPayload[0] = length;
		int i = 0;
		for (ITrigger trigger : _potentialTriggers) {
			payload.stringPayload[i++] = trigger.getUniqueTag();
		}

		PacketUpdate packet = new PacketUpdate(PacketIds.GATE_TRIGGERS, pipe.container.xCoord, pipe.container.yCoord, pipe.container.zCoord, payload);

		// Send to player
		CoreProxy.getProxy().sendToPlayer(player, packet);
	}

	/**
	 * Sends the current selection on the gate to the client.
	 *
	 * @param player
	 */
	public void sendSelection(EntityPlayer player) {
		if (pipe == null || pipe.gate == null)
			return;
		for (int position = 0; position < pipe.gate.material.numSlots; position++) {
			CoreProxy.getProxy().sendToPlayer(player, new PacketUpdate(PacketIds.GATE_SELECTION, pipe.container.xCoord, pipe.container.yCoord, pipe.container.zCoord, getSelectionPayload(position)));
		}

		// System.out.println("Sending current selection to player");
	}

	/**
	 * TRIGGERS *
	 */
	public boolean hasTriggers() {
		return _potentialTriggers.size() > 0;
	}

	public ITrigger getFirstTrigger() {
		if (_potentialTriggers.isEmpty())
			return null;
		return _potentialTriggers.first();
	}

	public ITrigger getLastTrigger() {
		if (_potentialTriggers.isEmpty())
			return null;
		return _potentialTriggers.last();
	}

	public Iterator<ITrigger> getTriggerIterator(boolean descending) {
		return descending ? _potentialTriggers.descendingIterator() : _potentialTriggers.iterator();
	}

	public boolean isNearbyTriggerActive(ITrigger trigger, ITriggerParameter parameter) {
		if (pipe.gate == null)
			return false;
		return pipe.gate.isNearbyTriggerActive(trigger, parameter);
	}

	public void setTrigger(int position, ITrigger trigger, boolean notify) {
		if (pipe.gate == null)
			return;
		pipe.gate.setTrigger(position, trigger);
		if (CoreProxy.getProxy().isClientWorld(pipe.container.worldObj) && notify) {
			sendSelectionChange(position);
		}
	}

	public void setTriggerParameter(int position, ITriggerParameter parameter, boolean notify) {
		if (pipe.gate == null)
			return;
		pipe.gate.setTriggerParameter(position, parameter);
		if (CoreProxy.getProxy().isClientWorld(pipe.container.worldObj) && notify) {
			sendSelectionChange(position);
		}
	}

	/**
	 * ACTIONS *
	 */
	public boolean hasActions() {
		return !_potentialActions.isEmpty();
	}

	public IAction getFirstAction() {
		if (_potentialActions.isEmpty())
			return null;
		return _potentialActions.first();
	}

	public IAction getLastAction() {
		if (_potentialActions.isEmpty())
			return null;
		return _potentialActions.last();
	}

	public Iterator<IAction> getActionIterator(boolean descending) {
		return descending ? _potentialActions.descendingIterator() : _potentialActions.iterator();
	}

	public void setAction(int position, IAction action, boolean notify) {
		pipe.gate.setAction(position, action);
		if (CoreProxy.getProxy().isClientWorld(pipe.container.worldObj) && notify) {
			sendSelectionChange(position);
		}
	}

	/**
	 * GATE INFORMATION *
	 */
	public ResourceLocation getGateGuiFile() {
		return pipe.gate.material.guiFile;
	}

	public String getGateName() {
		return GateDefinition.getLocalizedName(pipe.gate.material, pipe.gate.logic);
	}
}
