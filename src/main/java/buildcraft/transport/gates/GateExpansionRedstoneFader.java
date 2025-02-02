/*
 * Copyright (c) SpaceToad, 2011-2012
 * http://www.mod-buildcraft.com
 * 
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.transport.gates;

import buildcraft.BuildCraftTransport;
import buildcraft.api.gates.GateExpansionController;
import buildcraft.api.gates.IAction;
import buildcraft.api.gates.IGateExpansion;
import buildcraft.api.gates.ITrigger;
import net.minecraft.src.TileEntity;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public class GateExpansionRedstoneFader extends GateExpansionBuildcraft implements IGateExpansion {

	public static GateExpansionRedstoneFader INSTANCE = new GateExpansionRedstoneFader();

	private GateExpansionRedstoneFader() {
		super("fader");
	}

	@Override
	public GateExpansionController makeController(TileEntity pipeTile) {
		return new GateExpansionControllerRedstoneFader(pipeTile);
	}

	private class GateExpansionControllerRedstoneFader extends GateExpansionController {

		public GateExpansionControllerRedstoneFader(TileEntity pipeTile) {
			super(GateExpansionRedstoneFader.this, pipeTile);
		}

		@Override
		public void addTriggers(List<ITrigger> list) {
			super.addTriggers(list);
			list.addAll(Arrays.asList(BuildCraftTransport.triggerRedstoneLevel));
		}

		@Override
		public void addActions(List<IAction> list) {
			super.addActions(list);
			list.addAll(Arrays.asList(BuildCraftTransport.actionRedstoneLevel));
		}
	}
}
