/**
 * Copyright (c) 2011-2017, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 *
 * The BuildCraft API is distributed under the terms of the MIT License. Please check the contents of the license, which
 * should be located as "LICENSE.API" in the BuildCraft source code distribution.
 */
package buildcraft.api.gates;

import net.minecraft.src.IconRegister;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Icon;

public interface IGateExpansion {

    String getUniqueIdentifier();

    String getDisplayName();

    GateExpansionController makeController(TileEntity pipeTile);

    void registerBlockOverlay(IconRegister iconRegister);

    void registerItemOverlay(IconRegister iconRegister);

    Icon getOverlayBlock();

    Icon getOverlayItem();
}
