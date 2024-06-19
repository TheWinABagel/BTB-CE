/**
 * Copyright (c) 2011-2017, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 * <p/>
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL. Please check the contents
 * of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core;

import buildcraft.api.core.IIconProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.src.Icon;

public class CoreIconProvider implements IIconProvider {

    public static int ENERGY = 0;

    public static int MAX = 1;

    private Icon[] icons;

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int iconIndex) {
        return icons[iconIndex];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        icons = new Icon[MAX];

        icons[ENERGY] = iconRegister.registerIcon("buildcraftcore:icons/energy");
    }
}
