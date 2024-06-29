package buildcraft.energy;

import buildcraft.BuildCraftCore;
import net.fabricmc.api.EnvType;
import net.minecraft.src.TileEntity;

public class EnergyProxy {

	public static EnergyProxy proxy = new EnergyProxy();

	public static EnergyProxy getProxy() {
		if (BuildCraftCore.INSTANCE.getEffectiveSide().equals(EnvType.SERVER)) {
			return proxy;
		}
		else {
			return EnergyProxyClient.INSTANCE;
		}
	}

	public void registerTileEntities() {
		TileEntity.addMapping(TileEngineLegacy.class, "net.minecraft.src.buildcraft.energy.Engine");
		TileEntity.addMapping(TileEngineWood.class, "net.minecraft.src.buildcraft.energy.TileEngineWood");
		TileEntity.addMapping(TileEngineStone.class, "net.minecraft.src.buildcraft.energy.TileEngineStone");
		TileEntity.addMapping(TileEngineIron.class, "net.minecraft.src.buildcraft.energy.TileEngineIron");
		TileEntity.addMapping(TileEngineCreative.class, "net.minecraft.src.buildcraft.energy.TileEngineCreative");
	}

	public void registerBlockRenderers() {
	}
}
