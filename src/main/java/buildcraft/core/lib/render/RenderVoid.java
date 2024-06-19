/**
 * Copyright (c) 2011-2017, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 * <p/>
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL. Please check the contents
 * of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.lib.render;

import net.minecraft.src.Render;
import net.minecraft.src.Entity;
import net.minecraft.src.ResourceLocation;

public class RenderVoid extends Render {

    @Override
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {}

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
