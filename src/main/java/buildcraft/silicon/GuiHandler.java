package buildcraft.silicon;

import buildcraft.silicon.gui.*;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		if (!world.blockExists(x, y, z))
			return null;

		TileEntity tile = world.getBlockTileEntity(x, y, z);

		switch (ID) {

			case 0:
				if (!(tile instanceof TileAssemblyTable))
					return null;
				return new GuiAssemblyTable(player.inventory, (TileAssemblyTable) tile);

			case 1:
				if (!(tile instanceof TileAdvancedCraftingTable))
					return null;
				return new GuiAdvancedCraftingTable(player.inventory, (TileAdvancedCraftingTable) tile);

			case 2:
				if (!(tile instanceof TileIntegrationTable))
					return null;
				return new GuiIntegrationTable(player.inventory, (TileIntegrationTable) tile);
			default:
				return null;
		}
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		if (!world.blockExists(x, y, z))
			return null;

		TileEntity tile = world.getBlockTileEntity(x, y, z);

		switch (ID) {

			case 0:
				if (!(tile instanceof TileAssemblyTable))
					return null;
				return new ContainerAssemblyTable(player.inventory, (TileAssemblyTable) tile);

			case 1:
				if (!(tile instanceof TileAdvancedCraftingTable))
					return null;
				return new ContainerAdvancedCraftingTable(player.inventory, (TileAdvancedCraftingTable) tile);

			case 2:
				if (!(tile instanceof TileIntegrationTable))
					return null;
				return new ContainerIntegrationTable(player.inventory, (TileIntegrationTable) tile);
			default:
				return null;
		}
	}
}
