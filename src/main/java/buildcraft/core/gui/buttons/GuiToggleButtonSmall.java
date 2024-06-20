package buildcraft.core.gui.buttons;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 *
 * @author CovertJaguar <railcraft.wikispaces.com>
 */
@Environment(EnvType.CLIENT)
public class GuiToggleButtonSmall extends GuiToggleButton {

	public GuiToggleButtonSmall(int i, int j, int k, String s, boolean active) {
		this(i, j, k, 200, s, active);
	}

	public GuiToggleButtonSmall(int i, int x, int y, int w, String s, boolean active) {
		super(i, x, y, w, StandardButtonTextureSets.SMALL_BUTTON, s, active);
		this.active = active;
	}
}
