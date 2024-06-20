/**
 * Copyright (c) SpaceToad, 2011
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package buildcraft.core.render;

import net.minecraft.src.Render;
import net.minecraft.src.Entity;
import net.minecraft.src.ResourceLocation;

public class RenderVoid extends Render {

	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		// TODO Auto-generated method stub

	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
