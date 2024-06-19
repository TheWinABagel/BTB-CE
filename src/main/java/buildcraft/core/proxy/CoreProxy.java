/**
 * Copyright (c) 2011-2017, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 * <p/>
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL. Please check the contents
 * of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.proxy;

import buildcraft.BuildCraftCore;
import buildcraft.api.core.ICoreProxy;
import buildcraft.core.LaserKind;
import buildcraft.core.lib.EntityBlock;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.SidedProxy;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NetHandler;
import net.minecraft.src.NetHandlerPlayServer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;

import java.lang.ref.WeakReference;

public class CoreProxy implements ICoreProxy {

    @SidedProxy(clientSide = "buildcraft.core.proxy.CoreProxyClient", serverSide = "buildcraft.core.proxy.CoreProxy")
    public static CoreProxy proxy;

    /* BUILDCRAFT PLAYER */
    protected static WeakReference<EntityPlayer> buildCraftPlayer = new WeakReference<EntityPlayer>(null);

    public String getMinecraftVersion() {
        return Loader.instance().getMinecraftModContainer().getVersion();
    }

    /* INSTANCES */
    public Object getClient() {
        return null;
    }

    public World getClientWorld() {
        return null;
    }

    /* ENTITY HANDLING */
    public void removeEntity(Entity entity) {
        entity.worldObj.removeEntity(entity);
    }

    public String getItemDisplayName(ItemStack newStack) {
        return "";
    }

    /* GFX */
    public void initializeRendering() {}

    public void initializeEntityRendering() {}

    public void onCraftingPickup(World world, EntityPlayer player, ItemStack stack) {
        stack.onCrafting(world, player, stack.stackSize);
    }

    public String playerName() {
        return "";
    }

    private WeakReference<EntityPlayer> createNewPlayer(WorldServer world) {
        EntityPlayer player = FakePlayerFactory.get(world, BuildCraftCore.gameProfile);

        return new WeakReference<EntityPlayer>(player);
    }

    private WeakReference<EntityPlayer> createNewPlayer(WorldServer world, int x, int y, int z) {
        EntityPlayer player = FakePlayerFactory.get(world, BuildCraftCore.gameProfile);
        player.posX = x;
        player.posY = y;
        player.posZ = z;
        return new WeakReference<EntityPlayer>(player);
    }

    @Override
    public final WeakReference<EntityPlayer> getBuildCraftPlayer(WorldServer world) {
        if (CoreProxy.buildCraftPlayer.get() == null) {
            CoreProxy.buildCraftPlayer = createNewPlayer(world);
        } else {
            CoreProxy.buildCraftPlayer.get().worldObj = world;
        }

        return CoreProxy.buildCraftPlayer;
    }

    public final WeakReference<EntityPlayer> getBuildCraftPlayer(WorldServer world, int x, int y, int z) {
        if (CoreProxy.buildCraftPlayer.get() == null) {
            CoreProxy.buildCraftPlayer = createNewPlayer(world, x, y, z);
        } else {
            CoreProxy.buildCraftPlayer.get().worldObj = world;
            CoreProxy.buildCraftPlayer.get().posX = x;
            CoreProxy.buildCraftPlayer.get().posY = y;
            CoreProxy.buildCraftPlayer.get().posZ = z;
        }

        return CoreProxy.buildCraftPlayer;
    }

    public EntityBlock newEntityBlock(World world, double i, double j, double k, double iSize, double jSize,
            double kSize, LaserKind laserKind) {
        return new EntityBlock(world, i, j, k, iSize, jSize, kSize);
    }

    /**
     * This function returns either the player from the handler if it's on the server, or directly from the minecraft
     * instance if it's the client.
     */
    public EntityPlayer getPlayerFromNetHandler(NetHandler handler) {
        if (handler instanceof NetHandlerPlayServer) {
            return ((NetHandlerPlayServer) handler).playerEntity;
        } else {
            return null;
        }
    }

    public TileEntity getServerTile(TileEntity source) {
        return source;
    }

    public EntityPlayer getClientPlayer() {
        return null;
    }
}
