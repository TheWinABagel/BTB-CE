package buildcraft.builders;

import buildcraft.builders.gui.*;
import buildcraft.core.GuiIds;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class GuiHandlerBuilders implements IGuiHandler {

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        if (!world.blockExists(x, y, z)) {
            return null;
        }
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        return switch (ID) {
            case GuiIds.ARCHITECT_TABLE -> {
                if (!(tile instanceof TileArchitect architect)) {
                    yield null;
                }
                yield new GuiTemplate(player.inventory, architect);
            }
            case GuiIds.BLUEPRINT_LIBRARY -> {
                if (!(tile instanceof TileBlueprintLibrary blueprintLibrary)) {
                    yield null;
                }
                yield new GuiBlueprintLibrary(player, blueprintLibrary);
            }
            case GuiIds.BUILDER -> {
                if (!(tile instanceof TileBuilder builder)) {
                    yield null;
                }
                yield new GuiBuilder(player.inventory, builder);
            }
            case GuiIds.FILLER -> {
                if (!(tile instanceof TileFiller filler)) {
                    yield null;
                }
                yield new GuiFiller(player.inventory, filler);
            }
            default -> null;
        };

    }

    @Override
    public int getId() {
        return 4;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        if (!world.blockExists(x, y, z)) {
            return null;
        }

        TileEntity tile = world.getBlockTileEntity(x, y, z);

        return switch (ID) {
            case GuiIds.ARCHITECT_TABLE -> {
                if (!(tile instanceof TileArchitect architect)) {
                    yield null;
                }
                yield new ContainerTemplate(player.inventory, architect);
            }
            case GuiIds.BLUEPRINT_LIBRARY -> {
                if (!(tile instanceof TileBlueprintLibrary blueprintLibrary)) {
                    yield null;
                }
                yield new ContainerBlueprintLibrary(player, blueprintLibrary);
            }
            case GuiIds.BUILDER -> {
                if (!(tile instanceof TileBuilder builder)) {
                    yield null;
                }
                yield new ContainerBuilder(player.inventory, builder);
            }
            case GuiIds.FILLER -> {
                if (!(tile instanceof TileFiller filler)) {
                    yield null;
                }
                yield new ContainerFiller(player.inventory, filler);
            }
            default -> null;
        };
    }

}
