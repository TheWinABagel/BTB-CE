package dev.bagel.btb.emi.fluid;

import emi.dev.emi.emi.EmiPort;
import emi.dev.emi.emi.api.render.EmiTooltipComponents;
import emi.dev.emi.emi.config.EmiConfig;
import emi.shims.java.net.minecraft.client.gui.tooltip.TooltipComponent;
import emi.shims.java.net.minecraft.client.util.math.MatrixStack;
import emi.shims.java.net.minecraft.text.Text;
import emi.shims.java.net.minecraft.util.Formatting;
import net.minecraft.src.*;
import net.minecraftforge.fluids.Fluid;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class EmiFluidHelper {

    public static TooltipComponent getAmount(FluidEmiStack ingredient) {
        return EmiTooltipComponents.of(EmiConfig.fluidUnit.translate(ingredient.getAmount()).copy().formatted(Formatting.GRAY));
    }

    public static Text getFluidName(Fluid fluid, NBTTagCompound nbt) {
        String name = fluid.getLocalizedName();
        return Text.literal(name).append(Text.literal(Minecraft.getMinecraft().gameSettings.advancedItemTooltips ? String.format(" (#%04d)", fluid.getID()) : ""));
    }

    public static List<Text> getFluidTooltip(Fluid fluid, NBTTagCompound nbt) {
        return List.of(getFluidName(fluid, nbt));
    }

    public static void renderFluid(FluidEmiStack stack, MatrixStack matrices, int x, int y, float delta) {
        Fluid fluid = ((Fluid) stack.getKey());
        Icon icon = fluid.getIcon();
        drawTintedSprite(matrices, fluid.getIcon(),  -1, x, y,  icon.getIconWidth(), icon.getIconHeight());
    }

    private static void drawTintedSprite(MatrixStack matrices, Icon icn, int color, int x, int y, int width, int height) {
        EmiPort.setPositionColorTexShader();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        GL11.glEnable(3042);
        float r = (float)(color >> 16 & 0xFF) / 256.0f;
        float g = (float)(color >> 8 & 0xFF) / 256.0f;
        float b = (float)(color & 0xFF) / 256.0f;
        float minU = icn.getMinU();
        float maxU = icn.getMaxU();
        float minV = icn.getMinV();
        float maxV = icn.getMaxV();
        GL11.glColor4f(r, g, b, 1.0f);
        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        float xMin = x;
        float yMin = y;
        float xMax = xMin + (float)width;
        float yMax = yMin + (float)height;
        float uSpan = maxU - minU;
        float vSpan = maxV - minV;
        float uMin = minU + uSpan / 16.0f * (float)0;
        float vMin = minV + vSpan / 16.0f * (float)0;
        float uMax = maxU - uSpan / 16.0f * (float)(16 - width);
        float vMax = maxV - vSpan / 16.0f * (float)(16 - height);
        tess.addVertexWithUV(xMin, yMax, 1.0, uMin, vMax);
        tess.addVertexWithUV(xMax, yMax, 1.0, uMax, vMax);
        tess.addVertexWithUV(xMax, yMin, 1.0, uMax, vMin);
        tess.addVertexWithUV(xMin, yMin, 1.0, uMin, vMin);
        tess.draw();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
