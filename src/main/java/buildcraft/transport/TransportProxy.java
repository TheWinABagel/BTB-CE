package buildcraft.transport;

import buildcraft.BuildCraftTransport;
import net.minecraft.src.TileEntity;

public class TransportProxy {

	public static TransportProxy proxy = new TransportProxy();
	public static int pipeModel = -1;

	public void registerTileEntities() {
		TileEntity.addMapping(TileDummyGenericPipe.class, "net.minecraft.src.buildcraft.GenericPipe");
		TileEntity.addMapping(TileDummyGenericPipe2.class, "net.minecraft.src.buildcraft.transport.TileGenericPipe");
		TileEntity.addMapping(TileGenericPipe.class, "net.minecraft.src.buildcraft.transport.GenericPipe");
		TileEntity.addMapping(TileFilteredBuffer.class, "net.minecraft.src.buildcraft.transport.TileFilteredBuffer");
	}

	public void registerRenderers() {
	}

	public void initIconProviders(BuildCraftTransport instance){
		
	}

	public void setIconProviderFromPipe(ItemPipe item, Pipe dummyPipe) {
		
	}

}
