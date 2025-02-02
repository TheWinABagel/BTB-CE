/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.transport.pipes;

import buildcraft.BuildCraftTransport;
import buildcraft.api.core.IIconProvider;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeIconProvider;
import buildcraft.transport.PipeTransportItems;
import buildcraft.transport.pipes.events.PipeEventItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraftforge.common.ForgeDirection;

public class PipeItemsVoid extends Pipe<PipeTransportItems> {

	public PipeItemsVoid(int itemID) {
		super(new PipeTransportItems(), itemID);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public IIconProvider getIconProvider() {
		return BuildCraftTransport.INSTANCE.pipeIconProvider;
	}

	@Override
	public int getIconIndex(ForgeDirection direction) {
		return PipeIconProvider.TYPE.PipeItemsVoid.ordinal();
	}

	public void eventHandler(PipeEventItem.DropItem event) {
		event.entity = null;
	}

	public void eventHandler(PipeEventItem.ReachedCenter event) {
		transport.items.scheduleRemoval(event.item);
	}
}
