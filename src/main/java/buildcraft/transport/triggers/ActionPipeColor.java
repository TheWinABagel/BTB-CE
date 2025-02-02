/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.transport.triggers;

import buildcraft.core.triggers.BCAction;
import buildcraft.core.utils.EnumColor;
import buildcraft.core.utils.StringUtils;
import java.util.Locale;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Icon;

public class ActionPipeColor extends BCAction {

	private Icon icon;
	public final EnumColor color;

	public ActionPipeColor(EnumColor color) {
		super("buildcraft:pipe.color." + color.getTag(), "buildcraft.pipe." + color.getTag());

		this.color = color;
	}

	@Override
	public String getDescription() {
		return String.format(StringUtils.localize("gate.action.pipe.item.color"), color.getLocalizedName());
	}

	@Override
	public Icon getIcon() {
		return icon;
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		icon = iconRegister.registerIcon("buildcraft:triggers/color_" + color.name().toLowerCase(Locale.ENGLISH));
	}
}
