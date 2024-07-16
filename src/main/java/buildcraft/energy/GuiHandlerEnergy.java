package buildcraft.energy;

import buildcraft.core.GuiIds;
import buildcraft.energy.gui.ContainerEngine;
import buildcraft.energy.gui.GuiCombustionEngine;
import buildcraft.energy.gui.GuiStoneEngine;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class GuiHandlerEnergy implements IGuiHandler {

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        if (!world.blockExists(x, y, z)) {
            return null;
        }

        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (!(tile instanceof TileEngineWithInventory engine)) {
            return null;
        }

        return switch (ID) {
            case GuiIds.ENGINE_IRON -> new GuiCombustionEngine(player.inventory, engine);
            case GuiIds.ENGINE_STONE -> new GuiStoneEngine(player.inventory, engine);
            default -> null;
        };
    }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        if (!world.blockExists(x, y, z)) {
            return null;
        }

        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (!(tile instanceof TileEngineWithInventory engine)) {
            return null;
        }

        return switch (ID) {
            case GuiIds.ENGINE_IRON, GuiIds.ENGINE_STONE -> new ContainerEngine(player.inventory, engine);
            default -> null;
        };
    }

}
