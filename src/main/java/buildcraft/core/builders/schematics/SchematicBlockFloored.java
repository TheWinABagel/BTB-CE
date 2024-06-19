package buildcraft.core.builders.schematics;

import buildcraft.api.blueprints.IBuilderContext;
import buildcraft.api.blueprints.SchematicBlock;
import buildcraft.api.core.BlockIndex;
import com.google.common.collect.Sets;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Set;

public class SchematicBlockFloored extends SchematicBlock {

    @Override
    public Set<BlockIndex> getPrerequisiteBlocks(IBuilderContext context) {
        return Sets.newHashSet(RELATIVE_INDEXES[ForgeDirection.DOWN.ordinal()]);
    }
}
