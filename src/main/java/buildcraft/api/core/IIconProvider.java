package buildcraft.api.core;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Icon;

public interface IIconProvider {
	
	/**
	 * @param iconIndex
	 * @return
	 */
	@Environment(EnvType.CLIENT)
	public Icon getIcon(int iconIndex);

	/**
	 * A call for the provider to register its Icons. This may be called multiple times but should only be executed once per provider
	 * @param iconRegister
	 */
	@Environment(EnvType.CLIENT)
	public void registerIcons(IconRegister iconRegister);

}
