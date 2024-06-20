/**
 * Copyright (c) SpaceToad, 2011 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.transport.triggers;

import buildcraft.core.triggers.BCAction;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Icon;
import net.minecraftforge.common.ForgeDirection;

import java.util.Locale;

public class ActionPipeDirection extends BCAction {

	private Icon icon;
	public final ForgeDirection direction;

	public ActionPipeDirection(ForgeDirection direction) {
		super("buildcraft:pipe.dir." + direction.name().toLowerCase(Locale.ENGLISH), "buildcraft.pipe.dir." + direction.name().toLowerCase(Locale.ENGLISH));

		this.direction = direction;
	}

	@Override
	public String getDescription() {
		return direction.name().substring(0, 1) + direction.name().substring(1).toLowerCase(Locale.ENGLISH) + " Pipe Direction";
	}

	@Override
	public Icon getIcon() {
		return icon;
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		icon = iconRegister.registerIcon("buildcraft:triggers/trigger_dir_" + direction.name().toLowerCase(Locale.ENGLISH));
	}
}
