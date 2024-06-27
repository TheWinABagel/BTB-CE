package buildcraft.silicon;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class SiliconProxyClient extends SiliconProxy {
	@Override
	@Environment(EnvType.CLIENT)
	public void registerRenderers() {

	}
}
