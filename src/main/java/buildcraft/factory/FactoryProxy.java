package buildcraft.factory;

import buildcraft.BuildCraftCore;
import buildcraft.core.EntityBlock;
import buildcraft.core.proxy.CoreProxyClient;
import net.fabricmc.api.EnvType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.World;

public class FactoryProxy {
	public static FactoryProxy proxy = new FactoryProxy();

    public static FactoryProxy getProxy() {
        if (BuildCraftCore.instance.getEffectiveSide().equals(EnvType.SERVER)) {
            return proxy;
        }
        else {
            return FactoryProxyClient.INSTANCE;
        }
    }

	public void initializeTileEntities() {
	}

	public void initializeEntityRenders() {
	}

	public void initializeNEIIntegration() {
	}

    public EntityBlock newPumpTube(World w)
    {
        return new EntityBlock(w);
    }

    public EntityBlock newDrill(World w, double i, double j, double k, double l, double d, double e)
    {
        return new EntityBlock(w, i, j, k, l, d, e);
    }

    public EntityBlock newDrillHead(World w, double i, double j, double k, double l, double d, double e)
    {
        return new EntityBlock(w, i, j, k, l, d, e);
    }
}
