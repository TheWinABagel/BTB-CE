package buildcraft.transport.network;

import dev.bagel.btb.extensions.BuildcraftCustomPacketHandler;
import buildcraft.core.network.PacketCoordinates;
import buildcraft.core.network.PacketIds;
import buildcraft.core.network.PacketSlotChange;
import buildcraft.core.network.PacketUpdate;
import buildcraft.transport.PipeTransportItems;
import buildcraft.transport.PipeTransportPower;
import buildcraft.transport.TileGenericPipe;
import buildcraft.transport.gui.ContainerGateInterface;
import buildcraft.transport.pipes.PipeItemsDiamond;
import buildcraft.transport.pipes.PipeItemsEmerald;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Arrays;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.Container;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import org.jetbrains.annotations.Nullable;

public class PacketHandlerTransport implements BuildcraftCustomPacketHandler {
	public static final PacketHandlerTransport INSTANCE = new PacketHandlerTransport();
	@Override
	public void onPacketData(EntityPlayer player, Packet250CustomPayload packet, DataInputStream data, int packetID) {
		try {
			System.out.println("on packet data transport " + packet.channel);
			PacketUpdate packetUpdate = new PacketUpdate();
			switch (packetID) {
				case PacketIds.PIPE_POWER:
					PacketPowerUpdate packetPower = new PacketPowerUpdate();
					packetPower.readData(data);
					onPacketPower(player, packetPower);
					break;
				case PacketIds.PIPE_LIQUID:
					PacketFluidUpdate packetFluid = new PacketFluidUpdate();
					packetFluid.readData(data);
					break;
				case PacketIds.PIPE_TRAVELER: {
					PacketPipeTransportTraveler pkt = new PacketPipeTransportTraveler();
					pkt.readData(data);
					onPipeTravelerUpdate(player, pkt);
					break;
				}
				case PacketIds.GATE_ACTIONS:
					packetUpdate.readData(data);
					onGateActions(player, packetUpdate);
					break;
				case PacketIds.GATE_TRIGGERS:
					packetUpdate.readData(data);
					onGateTriggers(player, packetUpdate);
					break;
				case PacketIds.GATE_SELECTION:
					packetUpdate.readData(data);
					onGateSelection(player, packetUpdate);
					break;
				case PacketIds.PIPE_ITEMSTACK: {
					PacketPipeTransportItemStack pkt = new PacketPipeTransportItemStack();
					pkt.readData(data);
					break;
				}
				case PacketIds.PIPE_GATE_EXPANSION_MAP: {
					PacketGateExpansionMap pkt = new PacketGateExpansionMap();
					pkt.readData(data);
					break;
				}

				/**
				 * SERVER SIDE *
				 */
				case PacketIds.DIAMOND_PIPE_SELECT: {
					PacketSlotChange packet1 = new PacketSlotChange();
					packet1.readData(data);
					onDiamondPipeSelect(player, packet1);
					break;
				}

				case PacketIds.EMERALD_PIPE_SELECT: {
					PacketSlotChange packet1 = new PacketSlotChange();
					packet1.readData(data);
					onEmeraldPipeSelect(player, packet1);
					break;
				}

				case PacketIds.GATE_REQUEST_INIT:
					PacketCoordinates packetU = new PacketCoordinates();
					packetU.readData(data);
					onGateInitRequest(player, packetU);
					break;

				case PacketIds.GATE_REQUEST_SELECTION:
					PacketCoordinates packetS = new PacketCoordinates();
					packetS.readData(data);
					onGateSelectionRequest(player, packetS);
					break;

				case PacketIds.GATE_SELECTION_CHANGE:
					PacketUpdate packet3 = new PacketUpdate();
					packet3.readData(data);
					onGateSelectionChange(player, packet3);
					break;

				case PacketIds.PIPE_ITEMSTACK_REQUEST: {
					PacketPipeTransportItemStackRequest pkt = new PacketPipeTransportItemStackRequest(player);
					pkt.readData(data);
					break;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Handles received list of potential actions on a gate
	 *
	 * @param packet update packet received
	 */
	private void onGateActions(EntityPlayer player, PacketUpdate packet) {
		Container container = player.openContainer;

		if (!(container instanceof ContainerGateInterface))
			return;

		((ContainerGateInterface) container).updateActions(packet);
	}

	/**
	 * Handles received list of potential triggers on a gate.
	 *
	 * @param packet
	 */
	private void onGateTriggers(EntityPlayer player, PacketUpdate packet) {
		Container container = player.openContainer;

		if (!(container instanceof ContainerGateInterface))
			return;

		((ContainerGateInterface) container).updateTriggers(packet);
	}

	/**
	 * Handles received current gate selection on a gate
	 *
	 * @param packet
	 */
	private void onGateSelection(EntityPlayer player, PacketUpdate packet) {
		Container container = player.openContainer;

		if (!(container instanceof ContainerGateInterface))
			return;

		((ContainerGateInterface) container).setSelection(packet, false);
	}

	/**
	 * Updates items in a pipe.
	 *
	 * @param packet
	 */
	public static void onPipeTravelerUpdate(EntityPlayer player, PacketPipeTransportTraveler packet) {
		World world = player.worldObj;

		if (!world.blockExists(packet.posX, packet.posY, packet.posZ)) {
			System.out.println("block does not exist at " + packet.posX + ", " + packet.posY + ", " + packet.posZ);
			return;
		}

		TileEntity entity = world.getBlockTileEntity(packet.posX, packet.posY, packet.posZ);
		if (!(entity instanceof TileGenericPipe pipe)) {
			System.out.println("tile entity not instance of pipe " + entity);
			return;
		}

        if (pipe.pipe == null) {
			System.out.println("pipe.pipe is null");
			return;
		}

		if (!(pipe.pipe.transport instanceof PipeTransportItems transportItems)) {
			return;
		}

		transportItems.handleTravelerPacket(packet);
	}

	/**
	 * Updates the display power on a power pipe
	 *
	 * @param packetPower
	 */
	private void onPacketPower(EntityPlayer player, PacketPowerUpdate packetPower) {
		World world = player.worldObj;
		if (!world.blockExists(packetPower.posX, packetPower.posY, packetPower.posZ))
			return;

		TileEntity entity = world.getBlockTileEntity(packetPower.posX, packetPower.posY, packetPower.posZ);
		if (!(entity instanceof TileGenericPipe pipe))
			return;

        if (pipe.pipe == null)
			return;

		if (!(pipe.pipe.transport instanceof PipeTransportPower))
			return;

		((PipeTransportPower) pipe.pipe.transport).handlePowerPacket(packetPower);

	}

	/**
	 * ****************** SERVER ******************** *
	 */
	/**
	 * Handles selection changes on a gate.
	 *
	 * @param playerEntity
	 * @param packet
	 */
	private void onGateSelectionChange(EntityPlayer playerEntity, PacketUpdate packet) {
		if (!(playerEntity.openContainer instanceof ContainerGateInterface))
			return;

		((ContainerGateInterface) playerEntity.openContainer).setSelection(packet, true);
	}

	/**
	 * Handles gate gui (current) selection requests.
	 *
	 * @param playerEntity
	 * @param packet
	 */
	private void onGateSelectionRequest(EntityPlayer playerEntity, PacketCoordinates packet) {
		if (!(playerEntity.openContainer instanceof ContainerGateInterface))
			return;

		((ContainerGateInterface) playerEntity.openContainer).sendSelection(playerEntity);
	}

	/**
	 * Handles received gate gui initialization requests.
	 *
	 * @param playerEntity
	 * @param packet
	 */
	private void onGateInitRequest(EntityPlayer playerEntity, PacketCoordinates packet) {
		if (!(playerEntity.openContainer instanceof ContainerGateInterface))
			return;

		((ContainerGateInterface) playerEntity.openContainer).handleInitRequest(playerEntity);
	}

	/**
	 * Retrieves pipe at specified coordinates if any.
	 *
	 * @param world The world
	 * @param x x pos
	 * @param y y pos
	 * @param z z pos
	 * @return Pipe at given coordinates
	 */
	@Nullable
	private TileGenericPipe getPipe(World world, int x, int y, int z) {
		if (!world.blockExists(x, y, z))
			return null;

		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if (!(tile instanceof TileGenericPipe pipe)) {
			return null;
		}

		return pipe;
	}

	/**
	 * Handles selection changes on diamond pipe guis.
	 *
	 * @param player
	 * @param packet
	 */
	private void onDiamondPipeSelect(EntityPlayer player, PacketSlotChange packet) {
		TileGenericPipe pipe = getPipe(player.worldObj, packet.posX, packet.posY, packet.posZ);
		if (pipe == null) {
			return;
		}

		if (!(pipe.pipe instanceof PipeItemsDiamond diamondPipe)) {
			return;
		}
		diamondPipe.getFilters().setInventorySlotContents(packet.slot, packet.stack);
	}

	/**
	 * Handles selection changes on emerald pipe guis.
	 *
	 * @param player
	 * @param packet
	 */
	private void onEmeraldPipeSelect(EntityPlayer player, PacketSlotChange packet) {
		TileGenericPipe pipe = getPipe(player.worldObj, packet.posX, packet.posY, packet.posZ);
		if (pipe == null) {
			return;
		}

		if (!(pipe.pipe instanceof PipeItemsEmerald emeraldPipe)) {
			return;
		}

		emeraldPipe.getFilters().setInventorySlotContents(packet.slot, packet.stack);
	}


}
