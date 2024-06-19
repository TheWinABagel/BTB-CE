package buildcraft.core.lib.utils;

import net.minecraft.src.Block;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraftforge.fluids.*;

public final class FluidUtils {

    private FluidUtils() {}

    public static FluidStack getFluidStackFromBlock(Block b) {
        if (b != null) {
            if (b instanceof IFluidBlock && ((IFluidBlock) b).getFluid() != null) {
                return new FluidStack(((IFluidBlock) b).getFluid(), 1000);
            } else {
                Fluid f = FluidRegistry.lookupFluidForBlock(b);
                if (f != null && FluidRegistry.isFluidRegistered(f)) {
                    return new FluidStack(f, 1000);
                }
            }
        }

        return null;
    }

    public static FluidStack getFluidStackFromItemStack(ItemStack stack) {
        if (stack != null) {
            if (stack.getItem() instanceof IFluidContainerItem) {
                IFluidContainerItem ctr = (IFluidContainerItem) stack.getItem();
                return ctr.getFluid(stack);
            } else if (FluidContainerRegistry.isFilledContainer(stack)) {
                return FluidContainerRegistry.getFluidForFilledItem(stack);
            } else if (stack.getItem() instanceof ItemBlock) {
                Block b = Block.getBlockFromItem(stack.getItem());
                if (b != null) {
                    return getFluidStackFromBlock(b);
                }
            }
        }
        return null;
    }

    public static Fluid getFluidFromItemStack(ItemStack stack) {
        FluidStack fluidStack = getFluidStackFromItemStack(stack);
        return fluidStack != null ? fluidStack.getFluid() : null;
    }

    public static boolean isFluidContainer(ItemStack stack) {
        return stack != null && stack.getItem() != null
                && (stack.getItem() instanceof IFluidContainerItem || FluidContainerRegistry.isFilledContainer(stack));
    }
}
