/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.transport;

import dev.bagel.btb.extensions.BlockUnloadExtension;
import dev.bagel.btb.mixin.accessors.ChunkTrackerAccessor;
import dev.bagel.btb.mixin.accessors.ChunkTrackerEntryAccessor;
import buildcraft.BuildCraftCore;
import buildcraft.BuildCraftTransport;
import buildcraft.api.core.IIconProvider;
import buildcraft.api.core.Position;
import buildcraft.api.gates.GateExpansions;
import buildcraft.api.gates.IGateExpansion;
import buildcraft.api.gates.IOverrideDefaultTriggers;
import buildcraft.api.gates.ITrigger;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.IPipeTile;
import buildcraft.api.transport.PipeWire;
import buildcraft.core.DefaultProps;
import buildcraft.core.IDropControlInventory;
import buildcraft.core.ITileBufferHolder;
import buildcraft.core.TileBuffer;
import buildcraft.core.inventory.InvUtils;
import buildcraft.core.network.IClientState;
import buildcraft.core.network.IGuiReturnHandler;
import buildcraft.core.network.ISyncedTile;
import buildcraft.core.network.PacketTileState;
import buildcraft.core.utils.BCLog;
import buildcraft.transport.gates.GateDefinition;
import buildcraft.transport.gates.GateFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Level;
import btw.world.chunk.ChunkTrackerEntry;

public class TileGenericPipe extends TileEntity implements IPowerReceptor, IFluidHandler, IPipeTile, IOverrideDefaultTriggers, ITileBufferHolder,
		IDropControlInventory, ISyncedTile, ISolidSideTile, IGuiReturnHandler, BlockUnloadExtension {

	public class CoreState implements IClientState {

		public int pipeId = -1;
		public int gateMaterial = -1;
		public int gateLogic = -1;
		public final Set<Byte> expansions = new HashSet<>();

		@Override
		public void writeData(DataOutputStream data) throws IOException {
			data.writeInt(pipeId);
			data.writeByte(gateMaterial);
			data.writeByte(gateLogic);
			data.writeByte(expansions.size());
			for (Byte expansion : expansions) {
				data.writeByte(expansion);
			}
		}

		@Override
		public void readData(DataInputStream data) throws IOException {
			pipeId = data.readInt();
			gateMaterial = data.readByte();
			gateLogic = data.readByte();
			expansions.clear();
			int numExp = data.readByte();
			for (int i = 0; i < numExp; i++) {
				expansions.add(data.readByte());
			}
		}
	}
	public final PipeRenderState renderState = new PipeRenderState();
	public final CoreState coreState = new CoreState();
	private boolean deletePipe = false;
	private TileBuffer[] tileBuffer;
	public boolean[] pipeConnectionsBuffer = new boolean[6];
	public Pipe pipe;
	private boolean sendClientUpdate = false;
	private boolean blockNeighborChange = false;
	private boolean refreshRenderState = false;
	private boolean pipeBound = false;
	private boolean resyncGateExpansions = false;
	public int redstoneInput = 0;
	private int[] facadeBlocks = new int[ForgeDirection.VALID_DIRECTIONS.length];
	private int[] facadeMeta = new int[ForgeDirection.VALID_DIRECTIONS.length];
	private boolean[] plugs = new boolean[ForgeDirection.VALID_DIRECTIONS.length];

	public TileGenericPipe() {
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setByte("redstoneInput", (byte)redstoneInput);

		if (pipe != null) {
			nbt.setInteger("pipeId", pipe.itemID);
			pipe.writeToNBT(nbt);
		} else
			nbt.setInteger("pipeId", coreState.pipeId);

		for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
			nbt.setInteger("facadeBlocks[" + i + "]", facadeBlocks[i]);
			nbt.setInteger("facadeMeta[" + i + "]", facadeMeta[i]);
			nbt.setBoolean("plug[" + i + "]", plugs[i]);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		redstoneInput = nbt.getByte("redstoneInput");

		coreState.pipeId = nbt.getInteger("pipeId");
		pipe = BlockGenericPipe.createPipe(coreState.pipeId);
		bindPipe();

		if (pipe != null)
			pipe.readFromNBT(nbt);
		else {
			BCLog.logger.log(Level.WARNING, "Pipe failed to load from NBT at {0},{1},{2}", new Object[]{xCoord, yCoord, zCoord});
			deletePipe = true;
		}

		for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
			facadeBlocks[i] = nbt.getInteger("facadeBlocks[" + i + "]");
			facadeMeta[i] = nbt.getInteger("facadeMeta[" + i + "]");
			plugs[i] = nbt.getBoolean("plug[" + i + "]");
		}
	}

	@Override
	public void invalidate() {
		initialized = false;
		tileBuffer = null;
		if (pipe != null)
			pipe.invalidate();
		super.invalidate();
	}

	@Override
	public void validate() {
		super.validate();
		initialized = false;
		tileBuffer = null;
		bindPipe();
		if (pipe != null)
			pipe.validate();
	}
	public boolean initialized = false;

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (!worldObj.isRemote) {
			if (deletePipe)
				worldObj.setBlockToAir(xCoord, yCoord, zCoord);

			if (pipe == null)
				return;

			if (!initialized)
				initialize(pipe);
		}
		if (!BlockGenericPipe.isValid(pipe))
			return;

		pipe.updateEntity();

		if (worldObj.isRemote) {
			if (resyncGateExpansions)
				syncGateExpansions();
			return;
		}

		if (blockNeighborChange) {
			computeConnections();
			pipe.onNeighborBlockChange(0);
			blockNeighborChange = false;
			refreshRenderState = true;
		}

		if (refreshRenderState) {
			refreshRenderState();
			refreshRenderState = false;
		}

		PowerReceiver provider = getPowerReceiver(null);
		if (provider != null)
			provider.update();

		if (sendClientUpdate) {
			sendClientUpdate = false;
			if (worldObj instanceof WorldServer world) {
                ChunkTrackerEntry playerInstance = ((ChunkTrackerAccessor) world.getChunkTracker()).callGetOrCreateTrackerEntry(xCoord >> 4, zCoord >> 4);
				if (playerInstance != null) {
					((ChunkTrackerEntryAccessor) playerInstance).callSendToPlayersWatchingNotWaitingFullChunk(getDescriptionPacket());
				}
			}
		}
	}

	// PRECONDITION: worldObj must not be null
	private void refreshRenderState() {
		// Pipe connections;
		for (ForgeDirection o : ForgeDirection.VALID_DIRECTIONS) {
			renderState.pipeConnectionMatrix.setConnected(o, this.pipeConnectionsBuffer[o.ordinal()]);
		}

		// Pipe Textures
		for (int i = 0; i < 7; i++) {
			ForgeDirection o = ForgeDirection.getOrientation(i);
			renderState.textureMatrix.setIconIndex(o, pipe.getIconIndex(o));
		}

		// WireState
		for (PipeWire color : PipeWire.values()) {
			renderState.wireMatrix.setWire(color, pipe.wireSet[color.ordinal()]);
			for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
				renderState.wireMatrix.setWireConnected(color, direction, pipe.isWireConnectedTo(this.getTile(direction), color));
			}
			boolean lit = pipe.signalStrength[color.ordinal()] > 0;

			switch (color) {
				case RED:
					renderState.wireMatrix.setWireIndex(color, lit ? WireIconProvider.Texture_Red_Lit : WireIconProvider.Texture_Red_Dark);
					break;
				case BLUE:
					renderState.wireMatrix.setWireIndex(color, lit ? WireIconProvider.Texture_Blue_Lit : WireIconProvider.Texture_Blue_Dark);
					break;
				case GREEN:
					renderState.wireMatrix.setWireIndex(color, lit ? WireIconProvider.Texture_Green_Lit : WireIconProvider.Texture_Green_Dark);
					break;
				case YELLOW:
					renderState.wireMatrix.setWireIndex(color, lit ? WireIconProvider.Texture_Yellow_Lit : WireIconProvider.Texture_Yellow_Dark);
					break;
				default:
					break;

			}
		}

		// Gate Textures and movement
		renderState.setIsGateLit(pipe.gate != null ? pipe.gate.isGateActive() : false);
		renderState.setIsGatePulsing(pipe.gate != null ? pipe.gate.isGatePulsing(): false);

		// Facades
		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
			int blockId = this.facadeBlocks[direction.ordinal()];
			renderState.facadeMatrix.setFacade(direction, blockId, this.facadeMeta[direction.ordinal()]);
		}

		//Plugs
		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
			renderState.plugMatrix.setConnected(direction, plugs[direction.ordinal()]);
		}

		if (renderState.isDirty()) {
			renderState.clean();
			sendUpdateToClient();
		}
	}

	public void initialize(Pipe pipe) {

		this.blockType = getBlockType();

		if (pipe == null) {
			BCLog.logger.log(Level.WARNING, "Pipe failed to initialize at {0},{1},{2}, deleting", new Object[]{xCoord, yCoord, zCoord});
			worldObj.setBlockToAir(xCoord, yCoord, zCoord);
			return;
		}

		this.pipe = pipe;

		for (ForgeDirection o : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tile = getTile(o);

			if (tile instanceof ITileBufferHolder bufferHolder)
                bufferHolder.blockCreated(o, BuildCraftTransport.genericPipeBlock.blockID, this);
			if (tile instanceof TileGenericPipe genericPipe)
                genericPipe.scheduleNeighborChange();
		}

		bindPipe();

		computeConnections();
		scheduleRenderUpdate();

		if (pipe.needsInit())
			pipe.initialize();

		initialized = true;
	}

	private void bindPipe() {

		if (!pipeBound && pipe != null) {

			pipe.setTile(this);

			coreState.pipeId = pipe.itemID;
			pipeBound = true;
		}
	}

	public boolean isInitialized() {
		return initialized;
	}

	@Override
	public PowerReceiver getPowerReceiver(ForgeDirection side) {
		if (BlockGenericPipe.isValid(pipe) && pipe instanceof IPowerReceptor)
			return ((IPowerReceptor) pipe).getPowerReceiver(null);
		else
			return null;
	}

	@Override
	public void doWork(PowerHandler workProvider) {
		if (BlockGenericPipe.isValid(pipe) && pipe instanceof IPowerReceptor)
			((IPowerReceptor) pipe).doWork(workProvider);
	}

	public void scheduleNeighborChange() {
		blockNeighborChange = true;
	}

	/* IPIPEENTRY */
	@Override
	public int injectItem(ItemStack payload, boolean doAdd, ForgeDirection from) {
		if (BlockGenericPipe.isValid(pipe) && pipe.transport instanceof PipeTransportItems && isPipeConnected(from)) {
			if (doAdd) {
				Position itemPos = new Position(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, from.getOpposite());
				itemPos.moveBackwards(0.4);

				TravelingItem pipedItem = TravelingItem.make(itemPos.x, itemPos.y, itemPos.z, payload);
				((PipeTransportItems) pipe.transport).injectItem(pipedItem, itemPos.orientation);
			}
			return payload.stackSize;
		}
		return 0;
	}

	@Override
	public PipeType getPipeType() {
		if (BlockGenericPipe.isValid(pipe))
			return pipe.transport.getPipeType();
		return null;
	}

	/* SMP */
	@Override
	public Packet getDescriptionPacket() {
		bindPipe();

		PacketTileState packet = new PacketTileState(this.xCoord, this.yCoord, this.zCoord);
		coreState.expansions.clear();
		if (pipe != null && pipe.gate != null) {
			coreState.gateMaterial = pipe.gate.material.ordinal();
			coreState.gateLogic = pipe.gate.logic.ordinal();
			for (IGateExpansion ex : pipe.gate.expansions.keySet()) {
				coreState.expansions.add(GateExpansions.getServerExpansionID(ex.getUniqueIdentifier()));
			}
		} else {
			coreState.gateMaterial = -1;
			coreState.gateLogic = -1;
		}

		if (pipe != null && pipe.transport != null)
			pipe.transport.sendDescriptionPacket();

		packet.addStateForSerialization((byte) 0, coreState);
		packet.addStateForSerialization((byte) 1, renderState);
		if (pipe instanceof IClientState)
			packet.addStateForSerialization((byte) 2, (IClientState) pipe);
		return packet.getPacket();
	}

	public void sendUpdateToClient() {
		sendClientUpdate = true;
	}

	@Override
	public LinkedList<ITrigger> getTriggers() {
		LinkedList<ITrigger> result = new LinkedList<ITrigger>();

		if (BlockGenericPipe.isFullyDefined(pipe) && pipe.hasGate()) {
			result.add(BuildCraftCore.triggerRedstoneActive);
			result.add(BuildCraftCore.triggerRedstoneInactive);
		}

		return result;
	}

	@Override
	public void blockRemoved(ForgeDirection from) {
	}

	public TileBuffer[] getTileCache() {
		if (tileBuffer == null && pipe != null)
			tileBuffer = TileBuffer.makeBuffer(worldObj, xCoord, yCoord, zCoord, pipe.transport.delveIntoUnloadedChunks());
		return tileBuffer;
	}

	@Override
	public void blockCreated(ForgeDirection from, int blockID, TileEntity tile) {
		TileBuffer[] cache = getTileCache();
		if (cache != null)
			cache[from.getOpposite().ordinal()].set(blockID, tile);
	}

	@Override
	public int getBlockId(ForgeDirection to) {
		TileBuffer[] cache = getTileCache();
		if (cache != null)
			return cache[to.ordinal()].getBlockID();
		else
			return 0;
	}

	@Override
	public TileEntity getTile(ForgeDirection to) {
		TileBuffer[] cache = getTileCache();
		if (cache != null)
			return cache[to.ordinal()].getTile();
		else
			return null;
	}

	/**
	 * Checks if this tile can connect to another tile
	 *
	 * @param with - The other Tile
	 * @param side - The orientation to get to the other tile ('with')
	 * @return true if pipes are considered connected
	 */
	protected boolean canPipeConnect(TileEntity with, ForgeDirection side) {
		if (with == null)
			return false;

		if (hasPlug(side))
			return false;

		if (!BlockGenericPipe.isValid(pipe))
			return false;

		if (!(pipe instanceof IPipeConnectionForced) || !((IPipeConnectionForced) pipe).ignoreConnectionOverrides(side))
			if (with instanceof IPipeConnection) {
				IPipeConnection.ConnectOverride override = ((IPipeConnection) with).overridePipeConnection(pipe.transport.getPipeType(), side.getOpposite());
				if (override != IPipeConnection.ConnectOverride.DEFAULT)
					return override == IPipeConnection.ConnectOverride.CONNECT ? true : false;
			}

		if (with instanceof TileGenericPipe) {
			if (((TileGenericPipe) with).hasPlug(side.getOpposite()))
				return false;
			Pipe otherPipe = ((TileGenericPipe) with).pipe;

			if (!BlockGenericPipe.isValid(otherPipe))
				return false;

			if (!otherPipe.canPipeConnect(this, side.getOpposite()))
				return false;
		}

		return pipe.canPipeConnect(with, side);
	}

	private void computeConnections() {
		TileBuffer[] cache = getTileCache();
		if (cache == null)
			return;

		for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
			TileBuffer t = cache[side.ordinal()];
			t.refresh();

			pipeConnectionsBuffer[side.ordinal()] = canPipeConnect(t.getTile(), side);
		}
	}

	@Override
	public boolean isPipeConnected(ForgeDirection with) {
		if (worldObj.isRemote)
			return renderState.pipeConnectionMatrix.isConnected(with);
		return pipeConnectionsBuffer[with.ordinal()];
	}

	@Override
	public boolean isWireActive(PipeWire wire) {
		if (pipe == null)
			return false;
		return pipe.signalStrength[wire.ordinal()] > 0;
	}

	@Override
	public boolean doDrop() {
		if (BlockGenericPipe.isValid(pipe))
			return pipe.doDrop();
		else
			return false;
	}

	@Override
	public void onChunkUnload() {
		if (pipe != null)
			pipe.onChunkUnload();
	}

	/**
	 * ITankContainer implementation *
	 */
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if (BlockGenericPipe.isValid(pipe) && pipe.transport instanceof IFluidHandler && !hasPlug(from))
			return ((IFluidHandler) pipe.transport).fill(from, resource, doFill);
		else
			return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		if (BlockGenericPipe.isValid(pipe) && pipe.transport instanceof IFluidHandler && !hasPlug(from))
			return ((IFluidHandler) pipe.transport).drain(from, maxDrain, doDrain);
		else
			return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		if (BlockGenericPipe.isValid(pipe) && pipe.transport instanceof IFluidHandler && !hasPlug(from))
			return ((IFluidHandler) pipe.transport).drain(from, resource, doDrain);
		else
			return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		if (BlockGenericPipe.isValid(pipe) && pipe.transport instanceof IFluidHandler && !hasPlug(from))
			return ((IFluidHandler) pipe.transport).canFill(from, fluid);
		else
			return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		if (BlockGenericPipe.isValid(pipe) && pipe.transport instanceof IFluidHandler && !hasPlug(from))
			return ((IFluidHandler) pipe.transport).canDrain(from, fluid);
		else
			return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return null;
	}

	public void scheduleRenderUpdate() {
		refreshRenderState = true;
	}

	public boolean addFacade(ForgeDirection direction, int blockid, int meta) {
		if (this.worldObj.isRemote)
			return false;
		if (this.facadeBlocks[direction.ordinal()] == blockid)
			return false;

		if (hasFacade(direction))
			dropFacadeItem(direction);

		this.facadeBlocks[direction.ordinal()] = blockid;
		this.facadeMeta[direction.ordinal()] = meta;
		worldObj.notifyBlockChange(this.xCoord, this.yCoord, this.zCoord, getBlockId());
		scheduleRenderUpdate();
		return true;
	}

	public boolean hasFacade(ForgeDirection direction) {
		if (direction == null || direction == ForgeDirection.UNKNOWN)
			return false;
		if (this.worldObj.isRemote)
			return renderState.facadeMatrix.getFacadeBlockId(direction) != 0;
		return (this.facadeBlocks[direction.ordinal()] != 0);
	}

	private void dropFacadeItem(ForgeDirection direction) {
		InvUtils.dropItems(worldObj, ItemFacade.getStack(this.facadeBlocks[direction.ordinal()], this.facadeMeta[direction.ordinal()]), this.xCoord, this.yCoord, this.zCoord);
	}

	public boolean dropFacade(ForgeDirection direction) {
		if (!hasFacade(direction))
			return false;
		if (!worldObj.isRemote) {
			dropFacadeItem(direction);
			this.facadeBlocks[direction.ordinal()] = 0;
			this.facadeMeta[direction.ordinal()] = 0;
			worldObj.notifyBlockChange(this.xCoord, this.yCoord, this.zCoord, getBlockId());
			scheduleRenderUpdate();
		}
		return true;
	}

	@Environment(EnvType.CLIENT)
	public IIconProvider getPipeIcons() {
		if (pipe == null)
			return null;
		return pipe.getIconProvider();
	}

	@Override
	public IClientState getStateInstance(byte stateId) {
		switch (stateId) {
			case 0:
				return coreState;
			case 1:
				return renderState;
			case 2:
				return (IClientState) pipe;
		}
		throw new RuntimeException("Unknown state requested: " + stateId + " this is a bug!");
	}

	@Override
	public void afterStateUpdated(byte stateId) {
		if (!worldObj.isRemote)
			return;

		switch (stateId) {
			case 0:
				if (pipe == null && coreState.pipeId != 0)
					initialize(BlockGenericPipe.createPipe(coreState.pipeId));

				if (pipe == null)
					break;

				if (coreState.gateMaterial == -1)
					pipe.gate = null;
				else if (pipe.gate == null)
					pipe.gate = GateFactory.makeGate(pipe, GateDefinition.GateMaterial.fromOrdinal(coreState.gateMaterial), GateDefinition.GateLogic.fromOrdinal(coreState.gateLogic));

				syncGateExpansions();

				worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
				break;
			case 1: {
				if (renderState.needsRenderUpdate()) {
					worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
					renderState.clean();
				}
				break;
			}
		}
	}

	private void syncGateExpansions() {
		resyncGateExpansions = false;
		if (pipe.gate != null && !coreState.expansions.isEmpty()) {
			for (byte id : coreState.expansions) {
				IGateExpansion ex = GateExpansions.getExpansionClient(id);
				if (ex != null) {
					if (!pipe.gate.expansions.containsKey(ex))
						pipe.gate.addGateExpansion(ex);
				} else {
					resyncGateExpansions = true;
				}
			}
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public double getMaxRenderDistanceSquared() {
		return DefaultProps.PIPE_CONTENTS_RENDER_DIST * DefaultProps.PIPE_CONTENTS_RENDER_DIST;
	}

/*	@Override
	public boolean shouldDeleteTileEntityOnBlockChange(int newID) {
		return oldID != newID;
	}*/

	@Override
	public boolean isSolidOnSide(ForgeDirection side) {
		if (hasFacade(side))
			return true;

		if (BlockGenericPipe.isValid(pipe) && pipe instanceof ISolidSideTile solid)
			if (solid.isSolidOnSide(side))
				return true;
		return false;
	}

	public boolean hasPlug(ForgeDirection side) {
		if (side == null || side == ForgeDirection.UNKNOWN)
			return false;
		if (this.worldObj.isRemote)
			return renderState.plugMatrix.isConnected(side);
		return plugs[side.ordinal()];
	}

	public boolean removeAndDropPlug(ForgeDirection side) {
		if (!hasPlug(side))
			return false;
		if (!worldObj.isRemote) {
			plugs[side.ordinal()] = false;
			InvUtils.dropItems(worldObj, new ItemStack(BuildCraftTransport.plugItem), this.xCoord, this.yCoord, this.zCoord);
			worldObj.notifyBlockChange(this.xCoord, this.yCoord, this.zCoord, getBlockId());
			scheduleNeighborChange(); //To force recalculation of connections
			scheduleRenderUpdate();
		}
		return true;
	}

	public boolean addPlug(ForgeDirection forgeDirection) {
		if (hasPlug(forgeDirection))
			return false;

		plugs[forgeDirection.ordinal()] = true;
		worldObj.notifyBlockChange(this.xCoord, this.yCoord, this.zCoord, getBlockId());
		scheduleNeighborChange(); //To force recalculation of connections
		scheduleRenderUpdate();
		return true;
	}

	public int getBlockId() {
		Block block = getBlockType();
		if (block != null)
			return block.blockID;
		return 0;
	}

	@Override
	public World getWorld() {
		return worldObj;
	}

	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this;
	}

	@Override
	public void writeGuiData(DataOutputStream data) throws IOException {
		if (BlockGenericPipe.isValid(pipe) && pipe instanceof IGuiReturnHandler)
			((IGuiReturnHandler) pipe).writeGuiData(data);
	}

	@Override
	public void readGuiData(DataInputStream data, EntityPlayer sender) throws IOException {
		if (BlockGenericPipe.isValid(pipe) && pipe instanceof IGuiReturnHandler)
			((IGuiReturnHandler) pipe).readGuiData(data, sender);
	}
}
