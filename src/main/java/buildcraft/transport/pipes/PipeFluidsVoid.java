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
import buildcraft.transport.IPipeTransportFluidsHook;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeIconProvider;
import buildcraft.transport.PipeTransportFluids;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class PipeFluidsVoid extends Pipe implements IPipeTransportFluidsHook {

	public PipeFluidsVoid(int itemID) {
		super(new PipeTransportFluids(), itemID);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public IIconProvider getIconProvider() {
		return BuildCraftTransport.INSTANCE.pipeIconProvider;
	}

	@Override
	public int getIconIndex(ForgeDirection direction) {
		return PipeIconProvider.TYPE.PipeFluidsVoid.ordinal();
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return resource.amount;
	}
}
