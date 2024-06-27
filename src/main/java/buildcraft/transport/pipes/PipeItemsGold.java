/**
 * BuildCraft is open-source. It is distributed under the terms of the
 * BuildCraft Open Source License. It grants rights to read, modify, compile or
 * run the code. It does *NOT* grant the right to redistribute this software or
 * its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */
package buildcraft.transport.pipes;

import buildcraft.BuildCraftTransport;
import buildcraft.api.core.IIconProvider;
import buildcraft.core.utils.MathUtils;
import buildcraft.transport.*;
import buildcraft.transport.pipes.events.PipeEventItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraftforge.common.ForgeDirection;

public class PipeItemsGold extends Pipe {

	public PipeItemsGold(int itemID) {
		super(new PipeTransportItems(), itemID);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public IIconProvider getIconProvider() {
		return BuildCraftTransport.INSTANCE.pipeIconProvider;
	}

	@Override
	public int getIconIndex(ForgeDirection direction) {
		return PipeIconProvider.TYPE.PipeItemsGold.ordinal();
	}

	public void eventHandler(PipeEventItem.AdjustSpeed event) {
		event.handled = true;
		TravelingItem item = event.item;
		item.setSpeed(MathUtils.clamp(item.getSpeed() * 4F, TransportConstants.PIPE_NORMAL_SPEED * 4F, TransportConstants.PIPE_NORMAL_SPEED * 15F));
	}
}
