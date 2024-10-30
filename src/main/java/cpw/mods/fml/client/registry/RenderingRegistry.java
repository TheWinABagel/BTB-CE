package cpw.mods.fml.client.registry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import net.minecraft.src.Block;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Render;
import net.minecraft.src.RenderManager;
import net.minecraft.src.Entity;
import net.minecraft.src.IBlockAccess;

public class RenderingRegistry {
    private static final RenderingRegistry INSTANCE = new RenderingRegistry();
    private int nextRenderId = 40;
    public Map<Integer, ISimpleBlockRenderingHandler> blockRenderers = Maps.newHashMap();
    private final List<RenderingRegistry.EntityRendererInfo> entityRenderers = Lists.newArrayList();

    public static void registerEntityRenderingHandler(Class<? extends Entity> entityClass, Render renderer) {
        instance().entityRenderers.add(new RenderingRegistry.EntityRendererInfo(entityClass, renderer));
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

    public void loadEntityRenderers(Map<Class<? extends Entity>, Render> rendererMap) {
        for (EntityRendererInfo info : this.entityRenderers) {
            rendererMap.put(info.target, info.renderer);
            info.renderer.setRenderManager(RenderManager.instance);
        }

    }

    private static class EntityRendererInfo {
        private Class<? extends Entity> target;
        private Render renderer;

        public EntityRendererInfo(Class<? extends Entity> target, Render renderer) {
            this.target = target;
            this.renderer = renderer;
        }
    }
}
