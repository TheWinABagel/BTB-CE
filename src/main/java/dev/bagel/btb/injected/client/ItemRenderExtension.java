package dev.bagel.btb.injected.client;

public interface ItemRenderExtension {

    public int getRenderPasses(int metadata);

    public boolean requiresMultipleRenderPasses();
}
