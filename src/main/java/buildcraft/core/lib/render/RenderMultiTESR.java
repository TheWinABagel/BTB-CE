package buildcraft.core.lib.render;

import net.minecraft.src.TileEntityRenderer;
import net.minecraft.src.TileEntitySpecialRenderer;
import net.minecraft.src.TileEntity;

public class RenderMultiTESR extends TileEntitySpecialRenderer {

    private final TileEntitySpecialRenderer[] renderers;

    public RenderMultiTESR(TileEntitySpecialRenderer[] renderers) {
        this.renderers = renderers;
        for (TileEntitySpecialRenderer r : renderers) {
            r.setTileEntityRenderer(TileEntityRenderer.instance);
        }
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f) {
        for (TileEntitySpecialRenderer r : renderers) {
            r.renderTileEntityAt(tile, x, y, z, f);
        }
    }
}
