package buildcraft.transport.triggers;

import buildcraft.core.triggers.BCAction;
import buildcraft.core.utils.StringUtils;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Icon;

public class ActionSingleEnergyPulse extends BCAction {
	
	private Icon icon;

	public ActionSingleEnergyPulse() {
		super("buildcraft:pulsar.single", "buildcraft.pulser.single");
	}

	@Override
	public Icon getIcon() {
		return icon;
	}

	@Override
	public String getDescription() {
		return StringUtils.localize("gate.action.pulsar.single");
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister) {
		icon = iconRegister.registerIcon("buildcraft:triggers/action_single_pulsar");
	}

}
