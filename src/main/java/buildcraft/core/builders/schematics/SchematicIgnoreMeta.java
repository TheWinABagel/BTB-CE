/**
 * Copyright (c) 2011-2017, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 * <p/>
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL. Please check the contents
 * of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.builders.schematics;

import buildcraft.api.blueprints.IBuilderContext;
import buildcraft.api.blueprints.SchematicBlock;
import net.minecraft.src.ItemStack;

import java.util.LinkedList;

public class SchematicIgnoreMeta extends SchematicBlock {

    @Override
    public void getRequirementsForPlacement(IBuilderContext context, LinkedList<ItemStack> requirements) {
        requirements.add(new ItemStack(block, 1, 0));
    }

    @Override
    public void storeRequirements(IBuilderContext context, int x, int y, int z) {}

    @Override
    public boolean isAlreadyBuilt(IBuilderContext context, int x, int y, int z) {
        return block == context.world().getBlock(x, y, z);
    }
}
