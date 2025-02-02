package buildcraft.transport;

import buildcraft.api.core.IIconProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Icon;

public class WireIconProvider implements IIconProvider {
	
	public static final int Texture_Red_Dark				=  0;
	public static final int Texture_Red_Lit					=  1;
	public static final int Texture_Blue_Dark				=  2;
	public static final int Texture_Blue_Lit				=  3;
	public static final int Texture_Green_Dark				=  4;
	public static final int Texture_Green_Lit				=  5;
	public static final int Texture_Yellow_Dark				=  6;
	public static final int Texture_Yellow_Lit				=  7;
	
	public static final int MAX								=  8;

	@Environment(EnvType.CLIENT)
	private Icon[] icons;
	
	@Override
	@Environment(EnvType.CLIENT)
	public Icon getIcon(int pipeIconIndex) {
		return icons[pipeIconIndex];
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		icons = new Icon[MAX];

		icons[WireIconProvider.Texture_Red_Dark] = iconRegister.registerIcon("buildcraft:texture_red_dark");
		icons[WireIconProvider.Texture_Red_Lit] = iconRegister.registerIcon("buildcraft:texture_red_lit");
		icons[WireIconProvider.Texture_Blue_Dark] = iconRegister.registerIcon("buildcraft:texture_blue_dark");
		icons[WireIconProvider.Texture_Blue_Lit] = iconRegister.registerIcon("buildcraft:texture_blue_lit");
		icons[WireIconProvider.Texture_Green_Dark] = iconRegister.registerIcon("buildcraft:texture_green_dark");
		icons[WireIconProvider.Texture_Green_Lit] = iconRegister.registerIcon("buildcraft:texture_green_lit");
		icons[WireIconProvider.Texture_Yellow_Dark] = iconRegister.registerIcon("buildcraft:texture_yellow_dark");
		icons[WireIconProvider.Texture_Yellow_Lit] = iconRegister.registerIcon("buildcraft:texture_yellow_lit");		

	}

}
