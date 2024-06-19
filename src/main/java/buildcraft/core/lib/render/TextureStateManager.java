/**
 * Copyright (c) 2011-2017, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 * <p/>
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL. Please check the contents
 * of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.lib.render;

import buildcraft.api.core.render.ITextureStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Icon;

/*
 * This class moves texture management from PipeRenderState to be filled while rendering as efficient as possible
 */

@Environment(EnvType.CLIENT)
public final class TextureStateManager implements ITextureStateManager {

    private Icon currentTexture;
    private Icon[] textureArray;
    private Icon[] textureArrayCache;

    public TextureStateManager(Icon placeholder) {
        currentTexture = placeholder;
        textureArrayCache = new Icon[6];
    }

    public Icon[] popArray() {
        textureArray = textureArrayCache;
        return textureArrayCache; // Thread safety. Seriously.
    }

    public void pushArray() {
        textureArray = null;
    }

    public Icon getTexture() {
        return currentTexture;
    }

    public Icon[] getTextureArray() {
        return textureArray;
    }

    public boolean isSided() {
        return textureArray != null;
    }

    public void set(Icon icon) {
        currentTexture = icon;
    }
}
