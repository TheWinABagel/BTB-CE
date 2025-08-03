package buildcraft.transport.pipes;

import buildcraft.BuildCraftTransport;
import buildcraft.api.core.IIconProvider;
import buildcraft.api.transport.IPipeTile;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeIconProvider;
import buildcraft.transport.PipeTransportItems;
import buildcraft.transport.pipes.events.PipeEventItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import java.util.LinkedList;

public class PipeItemsClay extends Pipe<PipeTransportItems> {

    public PipeItemsClay(int itemID) {
        super(new PipeTransportItems(), itemID);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public IIconProvider getIconProvider() {
        return BuildCraftTransport.INSTANCE.pipeIconProvider;
    }

    @Override
    public int getIconIndex(ForgeDirection direction) {
        return PipeIconProvider.TYPE.PipeItemsClay.ordinal();
    }

    public void eventHandler(PipeEventItem.FindDest event) {
        LinkedList<ForgeDirection> nonPipesList = new LinkedList<ForgeDirection>();
        LinkedList<ForgeDirection> pipesList = new LinkedList<ForgeDirection>();

        for (ForgeDirection o : event.destinations) {
            if (!event.item.blacklist.contains(o) && container.pipe.outputOpen(o)) {
                if (container.isPipeConnected(o)) {
                    TileEntity entity = container.getTile(o);
                    if (entity instanceof IPipeTile) {
                        pipesList.add(o);
                    } else {
                        nonPipesList.add(o);
                    }
                }
            }
        }

        event.destinations.clear();
        if (nonPipesList.isEmpty()) {
            event.destinations.addAll(pipesList);
        } else {
            event.destinations.addAll(nonPipesList);
        }
    }
}
