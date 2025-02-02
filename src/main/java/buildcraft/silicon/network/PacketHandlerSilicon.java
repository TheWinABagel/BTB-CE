package buildcraft.silicon.network;

import dev.bagel.btb.extensions.BuildcraftCustomPacketHandler;
import buildcraft.core.network.PacketCoordinates;
import buildcraft.core.network.PacketIds;
import buildcraft.core.network.PacketNBT;
import buildcraft.core.network.PacketSlotChange;
import buildcraft.silicon.TileAdvancedCraftingTable;
import buildcraft.silicon.TileAssemblyTable;
import buildcraft.silicon.TileAssemblyTable.SelectionMessage;
import buildcraft.silicon.gui.ContainerAssemblyTable;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Container;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class PacketHandlerSilicon implements BuildcraftCustomPacketHandler {
	public static final PacketHandlerSilicon INSTANCE = new PacketHandlerSilicon();
	@Override
	public void onPacketData(EntityPlayer player, Packet250CustomPayload packet, DataInputStream data, int packetID) {
		try {
			switch (packetID) {
			case PacketIds.SELECTION_ASSEMBLY_SEND:
				PacketNBT packetT = new PacketNBT();
				packetT.readData(data);
				onSelectionUpdate(player, packetT);
				break;

			case PacketIds.SELECTION_ASSEMBLY:
				PacketNBT packetA = new PacketNBT();
				packetA.readData(data);
				onAssemblySelect(player, packetA);
				break;
			case PacketIds.SELECTION_ASSEMBLY_GET:
				PacketCoordinates packetC = new PacketCoordinates();
				packetC.readData(data);
				onAssemblyGetSelection(player, packetC);
				break;
			case PacketIds.ADVANCED_WORKBENCH_SETSLOT:
				PacketSlotChange packet1 = new PacketSlotChange();
				packet1.readData(data);
				onAdvancedWorkbenchSet(player, packet1);
				break;

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

/*	public void onPacketDataExtra(EntityPlayer player, Packet250CustomPayload packet, int packetID, DataInputStream data) {
		try {
			switch (packetID) {
				case PacketIds.SELECTION_ASSEMBLY_SEND:
					PacketNBT packetT = new PacketNBT();
					packetT.readData(data);
					onSelectionUpdate(player, packetT);
					break;

				case PacketIds.SELECTION_ASSEMBLY:
					PacketNBT packetA = new PacketNBT();
					packetA.readData(data);
					onAssemblySelect(player, packetA);
					break;
				case PacketIds.SELECTION_ASSEMBLY_GET:
					PacketCoordinates packetC = new PacketCoordinates();
					packetC.readData(data);
					onAssemblyGetSelection(player, packetC);
					break;
				case PacketIds.ADVANCED_WORKBENCH_SETSLOT:
					PacketSlotChange packet1 = new PacketSlotChange();
					packet1.readData(data);
					onAdvancedWorkbenchSet(player, packet1);
					break;

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}*/

	private void onSelectionUpdate(EntityPlayer player, PacketNBT packet) {

		Container container = player.openContainer;

		if (container instanceof ContainerAssemblyTable) {
			SelectionMessage message = new SelectionMessage();
			message.fromNBT(packet.getTagCompound());
			((ContainerAssemblyTable) container).handleSelectionMessage(message);
		}
	}

	private TileAssemblyTable getAssemblyTable(World world, int x, int y, int z) {

		if (!world.blockExists(x, y, z))
			return null;

		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if (!(tile instanceof TileAssemblyTable))
			return null;

		return (TileAssemblyTable) tile;
	}

	private TileAdvancedCraftingTable getAdvancedWorkbench(World world, int x, int y, int z) {

		if (!world.blockExists(x, y, z))
			return null;

		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if (!(tile instanceof TileAdvancedCraftingTable))
			return null;

		return (TileAdvancedCraftingTable) tile;
	}

	/**
	 * Sends the current selection on the assembly table to a player.
	 * 
	 * @param player
	 * @param packet
	 */
	private void onAssemblyGetSelection(EntityPlayer player, PacketCoordinates packet) {

		TileAssemblyTable tile = getAssemblyTable(player.worldObj, packet.posX, packet.posY, packet.posZ);
		if (tile == null)
			return;

		tile.sendSelectionTo(player);
	}

	/**
	 * Sets the selection on an assembly table according to player request.
	 * 
	 * @param player
	 * @param packetA
	 */
	private void onAssemblySelect(EntityPlayer player, PacketNBT packetA) {

		TileAssemblyTable tile = getAssemblyTable(player.worldObj, packetA.posX, packetA.posY, packetA.posZ);
		if (tile == null)
			return;

		SelectionMessage message = new SelectionMessage();
		message.fromNBT(packetA.getTagCompound());
		tile.handleSelectionMessage(message);
	}

	/**
	 * Sets the packet into the advanced workbench
	 * 
	 * @param player
	 * @param packet1
	 */
	private void onAdvancedWorkbenchSet(EntityPlayer player, PacketSlotChange packet1) {

		TileAdvancedCraftingTable tile = getAdvancedWorkbench(player.worldObj, packet1.posX, packet1.posY, packet1.posZ);
		if (tile == null)
			return;

		tile.updateCraftingMatrix(packet1.slot, packet1.stack);
	}
}
