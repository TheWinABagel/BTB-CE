package buildcraft.transport.triggers;

import buildcraft.core.triggers.BCAction;
import buildcraft.core.utils.StringUtils;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Icon;

public class ActionEnergyPulsar extends BCAction {

	private Icon icon;
	
	public ActionEnergyPulsar() {
		super("buildcraft:pulsar.constant", "buildcraft.pulser.constant");
	}

	@Override
	public Icon getIcon() {
		return icon;
	}
	
	@Override
	public String getDescription() {
		return StringUtils.localize("gate.action.pulsar.constant");
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister) {
		icon = iconRegister.registerIcon("buildcraft:triggers/action_pulsar");
	}

}
