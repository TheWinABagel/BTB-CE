package dev.bagel.btb.mixin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cpw.mods.fml.common.network.IGuiHandler;
import dev.bagel.btb.injected.EntityPlayerExtension;
import buildcraft.core.utils.BCLog;
import cpw.mods.fml.common.network.NetworkRegistry;
import dev.bagel.btb.mixin.accessors.EntityPlayerMPAccessor;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityPlayer.class)
public class EntityPlayerMixin implements EntityPlayerExtension {

    @Override
    public void btb$openGui(String mod, int modGuiId, World world, int x, int y, int z) {
        EntityPlayer player = ((EntityPlayer) (Object) this);
        if (!player.worldObj.isRemote) {
            btb$openRemoteGui(mod, ((EntityPlayerMP) player), modGuiId, world, x, y, z);
        } else {
            btb$openLocalGui(mod, player, modGuiId, world, x, y, z);
        }
    }

    private void btb$openLocalGui(String modId, EntityPlayer player, int modGuiId, World world, int x, int y, int z) {
        IGuiHandler handler = NetworkRegistry.instance().clientGuiHandlers.get(modId);

        if (handler == null) {
            try {
                int handlerId = Integer.parseInt(modId);
                for (IGuiHandler handler1 : NetworkRegistry.instance().clientGuiHandlers.values()) {
                    if (handler1.getId() == handlerId) {
                        handler = handler1;
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

        }
        if (handler != null) {
            Minecraft.getMinecraft().displayGuiScreen((GuiContainer) handler.getClientGuiElement(modGuiId, player, world, x, y, z));
        } else {
            BCLog.logger.warning("Packet failed to send, mod id is " + modId);
        }
    }

    private void btb$openRemoteGui(String modId, EntityPlayerMP player, int modGuiId, World world, int x, int y, int z) {
        IGuiHandler handler = NetworkRegistry.instance().serverGuiHandlers.get(modId);
        if (handler != null) {
            Container container = (Container) handler.getServerGuiElement(modGuiId, player, world, x, y, z);
            if (container != null) {
                ((EntityPlayerMPAccessor) player).callIncrementWindowID();
                player.closeContainer();
                int windowId = ((EntityPlayerMPAccessor) player).getCurrentWindowId();
                Packet250CustomPayload pkt = new Packet250CustomPayload("fml|NW", btb$generateGuiPacket(windowId, handler.getId(), modGuiId, x, y, z));
                player.playerNetServerHandler.sendPacketToPlayer(pkt);
                player.openContainer = container;
                player.openContainer.windowId = windowId;
                player.openContainer.onCraftGuiOpened(player);
            }
        }
    }

    private byte[] btb$generateGuiPacket(int windowId, int handlerId, int modGuiId, int x, int y, int z) {
        ByteArrayDataOutput dat = ByteStreams.newDataOutput();
        dat.writeInt(windowId);
        dat.writeInt(handlerId);
        dat.writeInt(modGuiId);
        dat.writeInt(x);
        dat.writeInt(y);
        dat.writeInt(z);
        return dat.toByteArray();
    }
}
