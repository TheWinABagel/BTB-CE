package buildcraft.energy;

import buildcraft.BuildCraftEnergy;
import buildcraft.core.render.RenderingEntityBlocks;
import buildcraft.core.render.RenderingEntityBlocks.EntityRenderIndex;
import buildcraft.energy.render.RenderEngine;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityRenderer;
import net.minecraft.src.TileEntitySpecialRenderer;

public class EnergyProxyClient extends EnergyProxy {
	public static final EnergyProxyClient INSTANCE = new EnergyProxyClient();
	@Override
	public void registerTileEntities() {
		super.registerTileEntities();
		bindTileEntitySpecialRenderer(TileEngine.class, new RenderEngine());
	}
	private static void bindTileEntitySpecialRenderer(Class<? extends TileEntity> tileEntityClass, TileEntitySpecialRenderer specialRenderer) {
		TileEntityRenderer.instance.specialRendererMap.put(tileEntityClass, specialRenderer);
		specialRenderer.setTileEntityRenderer(TileEntityRenderer.instance);
	}
	@Override
	public void registerBlockRenderers() {
		RenderingEntityBlocks.blockByEntityRenders.put(new EntityRenderIndex(BuildCraftEnergy.woodEngineBlock, 0), new RenderEngine(TileEngine.WOOD_TEXTURE));
		RenderingEntityBlocks.blockByEntityRenders.put(new EntityRenderIndex(BuildCraftEnergy.stoneEngineBlock, 0), new RenderEngine(TileEngine.STONE_TEXTURE));
		RenderingEntityBlocks.blockByEntityRenders.put(new EntityRenderIndex(BuildCraftEnergy.ironEngineBlock, 0), new RenderEngine(TileEngine.IRON_TEXTURE));
	}
}
