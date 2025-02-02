/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.transport.pipes;

import net.minecraft.src.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.BuildCraftTransport;
import buildcraft.api.core.IIconProvider;
import buildcraft.transport.IPipeConnectionForced;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeIconProvider;
import buildcraft.transport.PipeTransportItems;
import buildcraft.transport.TileGenericPipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class PipeItemsSandstone extends Pipe implements IPipeConnectionForced {

	public PipeItemsSandstone(int itemID) {
		super(new PipeTransportItems(), itemID);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public IIconProvider getIconProvider() {
		return BuildCraftTransport.INSTANCE.pipeIconProvider;
	}

	@Override
	public int getIconIndex(ForgeDirection direction) {
		return PipeIconProvider.TYPE.PipeItemsSandstone.ordinal();
	}

	@Override
	public boolean canPipeConnect(TileEntity tile, ForgeDirection side) {
		return (tile instanceof TileGenericPipe) && super.canPipeConnect(tile, side);
	}

	@Override
	public boolean ignoreConnectionOverrides(ForgeDirection with) {
		return true;
	}
}
