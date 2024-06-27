package buildcraft.transport.pipes;

import buildcraft.BuildCraftTransport;
import buildcraft.api.core.IIconProvider;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeIconProvider;
import buildcraft.transport.PipeTransportStructure;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraftforge.common.ForgeDirection;

public class PipeStructureCobblestone extends Pipe {

	public PipeStructureCobblestone(int itemID) {
		super(new PipeTransportStructure(), itemID);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public IIconProvider getIconProvider() {
		return BuildCraftTransport.INSTANCE.pipeIconProvider;
	}

	@Override
	public int getIconIndex(ForgeDirection direction) {
		return PipeIconProvider.TYPE.PipeStructureCobblestone.ordinal();
	}
}
