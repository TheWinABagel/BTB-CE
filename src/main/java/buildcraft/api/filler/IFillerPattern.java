package buildcraft.api.filler;

import buildcraft.api.core.IBox;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Icon;
import net.minecraftforge.common.ForgeDirection;

public interface IFillerPattern {

	public String getUniqueTag();

	/**
	 * Creates the object that does the pattern iteration. This object may be
	 * state-full and will be used until the pattern is done or changes.
	 *
	 * @param tile the Filler
	 * @param box the area to fill
	 * @param orientation not currently used, but may be in the future (the filler needs some orientation code)
	 * @return
	 */
	public IPatternIterator createPatternIterator(TileEntity tile, IBox box, ForgeDirection orientation);

	@Environment(EnvType.CLIENT)
	public Icon getIcon();

	public String getDisplayName();
}
