package btw.community.example.injected.client;

public interface ItemRenderExtension {

    public int getRenderPasses(int metadata);

    public boolean requiresMultipleRenderPasses();
}
