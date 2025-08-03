package cpw.mods.fml.client.registry;

import com.google.common.collect.Maps;
import net.minecraft.src.*;

import java.util.Map;

public class RenderingRegistry {
    private static final RenderingRegistry INSTANCE = new RenderingRegistry();
    private int nextRenderId = 40;
    public Map<Integer, ISimpleBlockRenderingHandler> blockRenderers = Maps.newHashMap();

    public static void registerEntityRenderingHandler(Class<? extends Entity> entityClass, Render renderer) {
        RenderManager.addEntityRenderer(entityClass, renderer);
//        instance().entityRenderers.add(new RenderingRegistry.EntityRendererInfo(entityClass, renderer));
    }

    public static void registerBlockHandler(ISimpleBlockRenderingHandler handler) {
        instance().blockRenderers.put(handler.getRenderId(), handler);
    }

    public static void registerBlockHandler(int renderId, ISimpleBlockRenderingHandler handler) {
        instance().blockRenderers.put(renderId, handler);
    }

    public static int getNextAvailableRenderId() {
        return instance().nextRenderId++;
    }

    public static RenderingRegistry instance() {
        return INSTANCE;
    }

    public boolean renderWorldBlock(RenderBlocks renderer, IBlockAccess world, int x, int y, int z, Block block, int modelId) {
        if (!this.blockRenderers.containsKey(modelId)) {
            return false;
        } else {
            ISimpleBlockRenderingHandler bri = this.blockRenderers.get(modelId);
            return bri.renderWorldBlock(world, x, y, z, block, modelId, renderer);
        }
    }

    public boolean renderInventoryBlock(RenderBlocks renderer, Block block, int metadata, int modelID) {
        if (this.blockRenderers.containsKey(modelID)) {
            ISimpleBlockRenderingHandler bri = this.blockRenderers.get(modelID);
            bri.renderInventoryBlock(block, metadata, modelID, renderer);
            return true;
        }
        return false;
    }

    public boolean renderItemAsFull3DBlock(int modelId) {
        ISimpleBlockRenderingHandler bri = blockRenderers.get(modelId);
        return bri != null && bri.shouldRender3DInInventory();
    }

    public void loadEntityRenderers() {
        //NOOP
    }

    public void loadEntityRenderers(Map<Class<? extends Entity>, Render> rendererMap) {
        //NOOP
    }
}
