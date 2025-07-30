package dev.bagel.btb.mixin.emi_mod_name;

import buildcraft.core.utils.BCItem;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @ModifyArgs(method = "getTooltip", at = @At(value = "INVOKE", target = "Lemi/dev/emi/emi/EmiUtil;getModName(Ljava/lang/String;)Ljava/lang/String;", remap = false))
    private void btb$makeBcItemsHaveName(Args args) {
        if (((ItemStack)(Object) this).getItem() instanceof BCItem) {
            args.set(0, "buildcraft");
        }
    }

    private static String get(NBTBase nbt) {
        return switch (nbt.getId()) {
            case 0 -> nbt.getName();
            case 1 -> nbt.getName() + ": " + ((NBTTagByte) nbt).data;
            case 2 -> nbt.getName() + ": " + ((NBTTagShort) nbt).data;
            case 3 -> nbt.getName() + ": " + ((NBTTagInt) nbt).data;
            case 4 -> nbt.getName() + ": " + ((NBTTagLong) nbt).data;
            case 5 -> nbt.getName() + ": " + ((NBTTagFloat) nbt).data;
            case 6 -> nbt.getName() + ": " + ((NBTTagDouble) nbt).data;
            case 7 -> nbt.getName() + ": " + Arrays.toString(((NBTTagByteArray) nbt).byteArray);
            case 8 -> nbt.getName() + ": " + ((NBTTagString) nbt).data;
            case 9 -> {
                String str = "List " + nbt.getName() + ": { ";
                for (int i = 0; i < ((NBTTagList) nbt).tagCount(); i++) {
                    if(i != 0) str +=", ";
                    str += get(((NBTTagList) nbt).tagAt(i));
                }
                yield str + " }";
            }
            case 10 -> nbt.getName() + ": " + ((NBTTagCompound) nbt).getTags().stream().map(o -> get((NBTBase) o));
            case 11 -> nbt.getName() + ": " + Arrays.toString(((NBTTagIntArray) nbt).intArray);
            default -> throw new IllegalStateException("Unexpected value: " + nbt.getId());
        };
    }

    @Inject(method = "getTooltip", at = @At("RETURN"), cancellable = true)
    private void btb$testGetNbt(EntityPlayer par1EntityPlayer, boolean par2, CallbackInfoReturnable<List> cir){
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.hasTagCompound()) {
            var ret = cir.getReturnValue();
            var tag = stack.getTagCompound();
            ret.add("");
            ret.add("Tags: ");
            ((Collection<NBTBase>) tag.getTags()).stream().map(nbt -> get(nbt)).toList().forEach(str ->{
                ret.add(str);
            });
//            ret.add(((Collection<NBTBase>) tag.getTags()).stream().map(nbt -> get(nbt)).toList().toString());
//            ret.add(stack.getTagCompound().toString());
            cir.setReturnValue(ret);
        }
    }
}
