/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.energy.triggers;

import buildcraft.api.gates.ITileTrigger;
import buildcraft.api.gates.ITriggerParameter;
import buildcraft.core.triggers.BCTrigger;
import buildcraft.core.utils.StringUtils;
import buildcraft.energy.TileEngine;
import buildcraft.energy.TileEngine.EnergyStage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import java.util.Locale;
import net.minecraft.src.IconRegister;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Icon;
import net.minecraftforge.common.ForgeDirection;

public class TriggerEngineHeat extends BCTrigger implements ITileTrigger {

	public EnergyStage stage;
	@Environment(EnvType.CLIENT)
	private Icon icon;

	public TriggerEngineHeat(EnergyStage stage) {
		super("buildcraft:engine.stage." + stage.name().toLowerCase(Locale.ENGLISH), "buildcraft.engine.stage." + stage.name().toLowerCase(Locale.ENGLISH));

		this.stage = stage;
	}

	@Override
	public String getDescription() {
		return StringUtils.localize("gate.trigger.engine." + stage.name().toLowerCase(Locale.ENGLISH));
	}

	@Override
	public boolean isTriggerActive(ForgeDirection side, TileEntity tile, ITriggerParameter parameter) {
		if (tile instanceof TileEngine) {
			TileEngine engine = ((TileEngine) tile);

			return engine.getEnergyStage() == stage;
		}

		return false;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Icon getIcon() {
		return icon;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		icon = iconRegister.registerIcon("buildcraft:triggers/trigger_engineheat_" + stage.name().toLowerCase(Locale.ENGLISH));
	}
}
