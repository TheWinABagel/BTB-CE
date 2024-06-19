package buildcraft.api.items;

import buildcraft.api.core.BlockIndex;
import buildcraft.api.core.IBox;
import buildcraft.api.core.IZone;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Created by asie on 2/28/15.
 */
public interface IMapLocation extends INamedItem {

    public enum MapLocationType {
        CLEAN,
        SPOT,
        AREA,
        PATH,
        ZONE
    }

    MapLocationType getType(ItemStack stack);

    /**
     * This function can be used for SPOT types.
     * 
     * @param stack
     * @return The point representing the map location.
     */
    BlockIndex getPoint(ItemStack stack);

    /**
     * This function can be used for SPOT and AREA types.
     * 
     * @param stack
     * @return The box representing the map location.
     */
    IBox getBox(ItemStack stack);

    /**
     * This function can be used for SPOT, AREA and ZONE types. The PATH type needs to be handled separately.
     * 
     * @param stack
     * @return An IZone representing the map location - also an instance of IBox for SPOT and AREA types.
     */
    IZone getZone(ItemStack stack);

    /**
     * This function can be used for SPOT and PATH types.
     * 
     * @param stack
     * @return A list of BlockIndexes representing the path the Map Location stores.
     */
    List<BlockIndex> getPath(ItemStack stack);

    /**
     * This function can be used for SPOT types only.
     * 
     * @param stack
     * @return The side of the spot.
     */
    ForgeDirection getPointSide(ItemStack stack);
}
