package buildcraft.api.core;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Icon;

public interface IIconProvider {
	
	/**
	 * @param iconIndex
	 * @return
	 */
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int iconIndex);

	/**
	 * A call for the provider to register its Icons. This may be called multiple times but should only be executed once per provider
	 * @param iconRegister
	 */
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister);

}
