package buildcraft.api.transport.pluggable;

import net.minecraft.src.Block;

public interface IFacadePluggable {

    Block getCurrentBlock();

    int getCurrentMetadata();

    boolean isTransparent();

    boolean isHollow();
}
