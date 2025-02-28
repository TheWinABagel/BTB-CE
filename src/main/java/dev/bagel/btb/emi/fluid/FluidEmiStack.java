package dev.bagel.btb.emi.fluid;

import emi.dev.emi.emi.EmiPort;
import emi.dev.emi.emi.api.render.EmiRender;
import emi.dev.emi.emi.api.stack.EmiStack;
import emi.dev.emi.emi.config.EmiConfig;
import emi.shims.java.com.unascribed.retroemi.RetroEMI;
import emi.shims.java.net.minecraft.client.gui.DrawContext;
import emi.shims.java.net.minecraft.client.gui.tooltip.TooltipComponent;
import emi.shims.java.net.minecraft.text.Text;
import emi.shims.java.net.minecraft.util.Formatting;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

@ApiStatus.Internal
public class FluidEmiStack extends EmiStack {
    private final Fluid fluid;
    private final NBTTagCompound nbt;

    public FluidEmiStack(Fluid fluid) {
        this(fluid, null);
    }

    public FluidEmiStack(Fluid fluid, @Nullable NBTTagCompound nbt) {
        this(fluid, nbt, 0);
    }

    public FluidEmiStack(Fluid fluid, @Nullable NBTTagCompound nbt, long amount) {
        this.fluid = fluid;
        this.nbt = nbt;
        this.amount = amount;
    }

    @Override
    public EmiStack copy() {
        FluidEmiStack e = new FluidEmiStack(fluid, nbt, amount);
        e.setChance(chance);
        e.setRemainder(getRemainder().copy());
        e.comparison = comparison;
        return e;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public NBTTagCompound getNbt() {
        return nbt;
    }

    @Override
    public Object getKey() {
        return fluid;
    }

    @Override
    public ItemStack getItemStack() {
        int id = fluid.getBlockID() == -1 ? 0 : fluid.getBlockID();
        return new ItemStack(id, 0, 32767);
    }

    @Override
    public ResourceLocation getId() {
        return new ResourceLocation("fluid", fluid.getName());
    }

    @Override
    public void render(DrawContext draw, int x, int y, float delta, int flags) {
        if ((flags & RENDER_ICON) != 0) {
            EmiFluidHelper.renderFluid(this, draw.getMatrices(), x, y, delta);
        }
        if ((flags & RENDER_REMAINDER) != 0) {
            EmiRender.renderRemainderIcon(this, draw, x, y);
        }
    }

    @Override
    public List<Text> getTooltipText() {
        return EmiFluidHelper.getFluidTooltip(fluid, nbt);
    }

    @Override
    public List<TooltipComponent> getTooltip() {
        List<TooltipComponent> list = getTooltipText().stream().map(EmiPort::ordered).map(TooltipComponent::of)
                .collect(Collectors.toList());
        if (amount > 1) {
            list.add(EmiFluidHelper.getAmount(this));
        }
        if (EmiConfig.appendModId) {
            String mod = "error";
            try {
                mod = RetroEMI.getMod(fluid);
            } catch (NullPointerException ignored) {}
            list.add(TooltipComponent.of(EmiPort.ordered(EmiPort.literal(mod, Formatting.BLUE, Formatting.ITALIC))));
        }
        list.addAll(super.getTooltip());
        return list;
    }

    @Override
    public Text getName() {
        return EmiFluidHelper.getFluidName(fluid, nbt);
    }

    public static EmiStack of(FluidStack fs) {
        if (fs == null) {
            return EmiStack.EMPTY;
        }
        return of(fs.getFluid(), fs.tag, fs.amount);
    }

    public static EmiStack of(Fluid fluid) {
        return of(fluid, 0);
    }

    public static EmiStack of(Fluid fluid, long amount) {
        return of(fluid, null, amount);
    }

    public static EmiStack of(Fluid fluid, @Nullable NBTTagCompound nbt, long amount) {
        if (fluid == null) {
            return EmiStack.EMPTY;
        }
        return new FluidEmiStack(fluid, nbt, amount);
    }
}