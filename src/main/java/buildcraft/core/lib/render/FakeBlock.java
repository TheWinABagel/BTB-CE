/**
 * Copyright (c) 2011-2017, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 * <p/>
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL. Please check the contents
 * of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.lib.render;

import buildcraft.api.core.render.ITextureStates;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraft.src.Icon;
import net.minecraft.src.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

/*
 * This is fake block to render pluggables and pipes without altering host state May prove useful if we will make API
 * for roboports, pipes, plugs and facades
 */
@Environment(EnvType.CLIENT)
public final class FakeBlock extends Block implements ITextureStates {

    public static final FakeBlock INSTANCE = new FakeBlock(254); //todocore fake block id

    private int renderMask = 0;
    private int colorMultiplier = 0xFFFFFF;

    private TextureStateManager textureState;

    private FakeBlock(int id) {
        super(id, Material.glass);
        textureState = new TextureStateManager(null); // Always Clientside
    }

    @Override
    public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
        return colorMultiplier;
    }

    @Deprecated
    public int getColor() {
        return colorMultiplier;
    }

    @Override
    public int getBlockColor() {
        return colorMultiplier;
    }

    public void setColor(int color) {
        this.colorMultiplier = color;
    }

    @Override
    public TextureStateManager getTextureState() {
        return textureState;
    }

    @Override
    public Icon getIcon(int side, int meta) {
        return textureState.isSided() ? textureState.getTextureArray()[side] : textureState.getTexture();
    }

    @Override
    public void setRenderSide(ForgeDirection side, boolean render) {
        if (render) {
            renderMask |= 1 << side.ordinal();
        } else {
            renderMask &= ~(1 << side.ordinal());
        }
    }

    @Override
    public void setRenderAllSides() {
        renderMask = 0x3f;
    }

    @Override
    public void setRenderMask(int mask) {
        renderMask = mask;
    }

    @Override
    public Block getBlock() {
        return this;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
        return (renderMask & (1 << side)) != 0;
    }
}
