package buildcraft.factory;

import buildcraft.core.GuiIds;
import buildcraft.factory.gui.*;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class GuiHandlerFactory implements IGuiHandler {
    public static final int FACTORY_GUI_ID = 3;

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        if (!world.blockExists(x, y, z)) {
            return null;
        }
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        return switch (ID) {
            case GuiIds.AUTO_CRAFTING_TABLE -> {
                if (!(tile instanceof TileAutoWorkbench autoWorkbench)) {
                    yield null;
                }
                yield new GuiAutoCrafting(player.inventory, world, autoWorkbench);
            }
            case GuiIds.REFINERY -> {
                if (!(tile instanceof TileRefinery refinery)) {
                    yield null;
                }
                yield new GuiRefinery(player.inventory, refinery);
            }
            case GuiIds.HOPPER -> {
                if (!(tile instanceof TileHopper hopper)) {
                    yield null;
                }
                yield new GuiHopper(player.inventory, hopper);
            }
            default -> null;
        };
    }

    @Override
    public int getId() {
        return FACTORY_GUI_ID;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        if (!world.blockExists(x, y, z)) {
            return null;
        }
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        return switch (ID) {
            case GuiIds.AUTO_CRAFTING_TABLE -> {
                if (!(tile instanceof TileAutoWorkbench)) {
                    yield null;
                }
                yield new ContainerAutoWorkbench(player.inventory, (TileAutoWorkbench) tile);
            }
            case GuiIds.REFINERY -> {
                if (!(tile instanceof TileRefinery)) {
                    yield null;
                }
                yield new ContainerRefinery(player.inventory, (TileRefinery) tile);
            }
            case GuiIds.HOPPER -> {
                if (!(tile instanceof TileHopper)) {
                    yield null;
                }
                yield new ContainerHopper(player.inventory, (TileHopper) tile);
            }
            default -> null;
        };
    }

}
