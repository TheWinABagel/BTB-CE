package dev.bagel.btb.mixin;

import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Unique
    private static String get(NBTBase nbt, int indentLevel) {
        var spaces = " ".repeat(indentLevel + 1);
        return "|" + spaces + switch (nbt.getId()) {
            case 0 -> nbt.getName();
            case 1 -> nbt.getName() + ":bt " + ((NBTTagByte) nbt).data;
            case 2 -> nbt.getName() + ":s " + ((NBTTagShort) nbt).data;
            case 3 -> nbt.getName() + ":i " + ((NBTTagInt) nbt).data;
            case 4 -> nbt.getName() + ":l " + ((NBTTagLong) nbt).data;
            case 5 -> nbt.getName() + ":f " + ((NBTTagFloat) nbt).data;
            case 6 -> nbt.getName() + ":d " + ((NBTTagDouble) nbt).data;
            case 7 -> nbt.getName() + ":ba " + Arrays.toString(((NBTTagByteArray) nbt).byteArray);
            case 8 -> nbt.getName() + ":s " + ((NBTTagString) nbt).data;
            case 9 -> {
                String str = "List " + nbt.getName() + ": { ";
                for (int i = 0; i < ((NBTTagList) nbt).tagCount(); i++) {
                    if(i != 0) str +=", ";
                    str += get(((NBTTagList) nbt).tagAt(i), indentLevel + 1);
                }
                yield str + " }";
            }
            case 10 -> nbt.getName() + ":c " + ((NBTTagCompound) nbt).getTags().stream().map(o -> get((NBTBase) o,indentLevel + 1)).toList();
            case 11 -> nbt.getName() + ":ia " + Arrays.toString(((NBTTagIntArray) nbt).intArray);
            default -> throw new IllegalStateException("Unexpected value: " + nbt.getId());
        };
    }

    @Inject(method = "getTooltip", at = @At("RETURN"), cancellable = true)
    private void btb$testGetNbt(EntityPlayer par1EntityPlayer, boolean par2, CallbackInfoReturnable<List> cir){
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.hasTagCompound()) {
            var tooltipList = cir.getReturnValue();
            var tag = stack.getTagCompound();
            tooltipList.add("");
            tooltipList.add("Tags: ");
            ((Collection<NBTBase>) tag.getTags()).stream().map(nbt -> get(nbt, 0)).toList().forEach(str -> {
                tooltipList.addAll(Arrays.stream(str.split("\\|")).filter(s -> !s.isBlank()).toList());
            });
            cir.setReturnValue(tooltipList);
        }
    }
}
