package buildcraft.silicon;

import buildcraft.silicon.gui.*;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class GuiHandlerSilicon implements IGuiHandler {

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        if (!world.blockExists(x, y, z)) {
            return null;
        }
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        return switch (ID) {
            case 0 -> {
                if (!(tile instanceof TileAssemblyTable assemblyTable)) {
                    yield null;
                }
                yield new GuiAssemblyTable(player.inventory, assemblyTable);
            }
            case 1 -> {
                if (!(tile instanceof TileAdvancedCraftingTable advancedCraftingTable)) {
                    yield null;
                }
                yield new GuiAdvancedCraftingTable(player.inventory, advancedCraftingTable);
            }
            case 2 -> {
                if (!(tile instanceof TileIntegrationTable integrationTable)) {
                    yield null;
                }
                yield new GuiIntegrationTable(player.inventory, integrationTable);
            }
            default -> null;
        };
    }

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        if (!world.blockExists(x, y, z)) {
            return null;
        }

        TileEntity tile = world.getBlockTileEntity(x, y, z);

        return switch (ID) {
            case 0 -> {
                if (!(tile instanceof TileAssemblyTable assemblyTable)) {
                    yield null;
                }
                yield new ContainerAssemblyTable(player.inventory, assemblyTable);
            }
            case 1 -> {
                if (!(tile instanceof TileAdvancedCraftingTable advancedCraftingTable)) {
                    yield null;
                }
                yield new ContainerAdvancedCraftingTable(player.inventory, advancedCraftingTable);
            }
            case 2 -> {
                if (!(tile instanceof TileIntegrationTable integrationTable)) {
                    yield null;
                }
                yield new ContainerIntegrationTable(player.inventory, integrationTable);
            }
            default -> null;
        };
    }
}
