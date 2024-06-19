package buildcraft.api.transport.pluggable;

import buildcraft.api.transport.IPipe;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public interface IPipePluggableItem {

    PipePluggable createPipePluggable(IPipe pipe, ForgeDirection side, ItemStack stack);
}
