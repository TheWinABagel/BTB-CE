package buildcraft.api.gates;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Icon;

public interface IAction {

	String getUniqueTag();

	@Environment(EnvType.CLIENT)
	Icon getIcon();

	@Environment(EnvType.CLIENT)
	void registerIcons(IconRegister iconRegister);

	boolean hasParameter();

	String getDescription();
}
