/**
 * Copyright (c) 2011-2017, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 * <p/>
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL. Please check the contents
 * of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.lib.items;

import buildcraft.core.BCCreativeTab;
import buildcraft.core.lib.utils.ResourceUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.Icon;
import net.minecraft.src.World;

public class ItemBuildCraft extends Item {

    public Icon[] icons;
    private String iconName;
    private boolean passSneakClick = false;

    public ItemBuildCraft() {
        this(BCCreativeTab.get("main"));
    }

    public ItemBuildCraft(CreativeTabs creativeTab) {
        super();

        setCreativeTab(creativeTab);
    }

    @Override
    public Item setUnlocalizedName(String par1Str) {
        iconName = par1Str;
        return super.setUnlocalizedName(par1Str);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int meta) {
        if (itemIcon != null) { // NBT lookup workaround?
            return itemIcon;
        }
        if (icons != null && icons.length > 0) {
            return icons[meta % icons.length];
        } else {
            return null;
        }
    }

    public String[] getIconNames() {
        return new String[] { iconName };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister) {
        String[] names = getIconNames();
        String prefix = ResourceUtils.getObjectPrefix(Item.itemRegistry.getNameForObject(this));
        prefix = prefix.substring(0, prefix.indexOf(":") + 1);
        icons = new Icon[names.length];

        for (int i = 0; i < names.length; i++) {
            icons[i] = par1IconRegister.registerIcon(prefix + names[i]);
        }
    }

    public Item setPassSneakClick(boolean passClick) {
        this.passSneakClick = passClick;
        return this;
    }

    @Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
        return passSneakClick;
    }
}
