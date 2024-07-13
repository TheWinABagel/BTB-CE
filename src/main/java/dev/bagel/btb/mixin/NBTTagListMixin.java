package dev.bagel.btb.mixin;

import dev.bagel.btb.injected.INBTTagListExtension;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(NBTTagList.class)
public abstract class NBTTagListMixin extends NBTBase implements INBTTagListExtension {

    @Shadow
    private List tagList;

    protected NBTTagListMixin(String string) {
        super(string);
    }

    @Override
    public NBTTagCompound getCompoundTagAt(int i) {
        if (i >= 0 && i < this.tagList.size()) {
            NBTBase nbtbase = (NBTBase) this.tagList.get(i);
            return nbtbase.getId() == 10 ? (NBTTagCompound) nbtbase : new NBTTagCompound();
        } else {
            return new NBTTagCompound();
        }
    }

    @Override
    public String getStringTagAt(int i) {
        if (i >= 0 && i < this.tagList.size()) {
            NBTBase nbtbase = (NBTBase) this.tagList.get(i);
            return nbtbase.toString();
        } else {
            return "";
        }
    }

    @Override //func_150309_d
    public double getDoubleTagAt(int i)
    {
        if (i >= 0 && i < this.tagList.size())
        {
            NBTBase nbtbase = (NBTBase)this.tagList.get(i);
            return nbtbase.getId() == 6 ? ((NBTTagDouble)nbtbase).data : 0.0D;
        }
        else
        {
            return 0.0D;
        }
    }
    @Override //func_150308_e
    public float getFloatTagAt(int i)
    {
        if (i >= 0 && i < this.tagList.size())
        {
            NBTBase nbtbase = (NBTBase)this.tagList.get(i);
            return nbtbase.getId() == 5 ? ((NBTTagFloat)nbtbase).data : 0.0F;
        }
        else
        {
            return 0.0F;
        }
    }
}
