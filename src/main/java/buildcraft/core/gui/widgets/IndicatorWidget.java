package buildcraft.core.gui.widgets;

import buildcraft.core.gui.GuiBuildCraft;
import buildcraft.core.gui.tooltips.ToolTip;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class IndicatorWidget extends Widget {

    public final IIndicatorController controller;

    public IndicatorWidget(IIndicatorController controller, int x, int y, int u, int v, int w, int h) {
        super(x, y, u, v, w, h);
        this.controller = controller;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void draw(GuiBuildCraft gui, int guiX, int guiY, int mouseX, int mouseY) {
        int scale = controller.getScaledLevel(h);
        gui.drawTexturedModalRect(guiX + x, guiY + y + h - scale, u, v + h - scale, w, scale);
    }

    @Override
    public ToolTip getToolTip() {
        return controller.getToolTip();
    }

}
