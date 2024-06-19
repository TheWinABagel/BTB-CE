/**
 * Copyright (c) 2011-2017, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 *
 * The BuildCraft API is distributed under the terms of the MIT License. Please check the contents of the license, which
 * should be located as "LICENSE.API" in the BuildCraft source code distribution.
 */
package buildcraft.api.core;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Icon;

public interface IconProvider {

    /**
     * @param iconIndex
     */
    @Environment(EnvType.CLIENT)
    Icon getIcon(int iconIndex);

    /**
     * A call for the provider to register its Icons. This may be called multiple times but should only be executed once
     * per provider
     * 
     * @param iconRegister
     */
    @Environment(EnvType.CLIENT)
    void registerIcons(IconRegister iconRegister);
}
