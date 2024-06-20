package buildcraft.core.gui.buttons;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 *
 * @author CovertJaguar <railcraft.wikispaces.com>
 */
@Environment(EnvType.CLIENT)
public class GuiButtonSmall extends GuiBetterButton {

	public GuiButtonSmall(int i, int x, int y, String s) {
		this(i, x, y, 200, s);
	}

	public GuiButtonSmall(int i, int x, int y, int w, String s) {
		super(i, x, y, w, StandardButtonTextureSets.SMALL_BUTTON, s);
	}
}
