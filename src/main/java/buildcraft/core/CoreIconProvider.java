package buildcraft.core;

import buildcraft.api.core.IIconProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Icon;

public class CoreIconProvider implements IIconProvider {

	public static int ENERGY 	= 0;
	
	public static int MAX 		= 1;

	private Icon[] _icons;
	
	@Override
	@Environment(EnvType.CLIENT)
	public Icon getIcon(int iconIndex) {
		return _icons[iconIndex];
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		_icons = new Icon[MAX];
		
		_icons[ENERGY] = iconRegister.registerIcon("buildcraft:icons/energy");
		
	}

}
