package buildcraft.silicon;


import buildcraft.BuildCraftCore;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.fabricmc.api.EnvType;

public class SiliconProxy {

	public static SiliconProxy proxy = new SiliconProxy();
	public static int laserBlockModel = -1;

	public void registerRenderers() {
		if (BuildCraftCore.INSTANCE.getEffectiveSide().equals(EnvType.CLIENT)) {
			SiliconProxy.laserBlockModel = RenderingRegistry.getNextAvailableRenderId();
			RenderingRegistry.registerBlockHandler(new SiliconRenderBlock());
		}
	}

}
