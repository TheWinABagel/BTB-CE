package buildcraft.api.library;

import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

public abstract class LibraryTypeHandlerNBT extends LibraryTypeHandler {

    public LibraryTypeHandlerNBT(String extension) {
        super(extension);
    }

    public abstract ItemStack load(ItemStack stack, NBTTagCompound nbt);

    public abstract boolean store(ItemStack stack, NBTTagCompound nbt);
}
