package buildcraft.api.gates;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Icon;

public interface ITrigger {

	/**
	 * Every trigger needs a unique tag, it should be in the format of
	 * "<modid>:<name>".
	 *
	 * @return the unique id
	 */
	String getUniqueTag();

	@Environment(EnvType.CLIENT)
	Icon getIcon();

	@Environment(EnvType.CLIENT)
	void registerIcons(IconRegister iconRegister);

	/**
	 * Return true if this trigger can accept parameters
	 */
	boolean hasParameter();

	/**
	 * Return true if this trigger requires a parameter
	 */
	boolean requiresParameter();

	/**
	 * Return the trigger description in the UI
	 */
	String getDescription();

	/**
	 * Create parameters for the trigger. As for now, there is only one kind of
	 * trigger parameter available so this subprogram is final.
	 */
	ITriggerParameter createParameter();
}
