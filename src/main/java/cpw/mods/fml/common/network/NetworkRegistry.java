package cpw.mods.fml.common.network;

import btw.BTWAddon;
import dev.bagel.btb.mixin.accessors.EntityPlayerMPAccessor;
import buildcraft.core.utils.BCLog;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.minecraft.src.*;

import java.util.HashMap;
import java.util.Map;

public class NetworkRegistry extends BTWAddon {
    private static final NetworkRegistry INSTANCE = new NetworkRegistry();
    private Map<String, IGuiHandler> serverGuiHandlers = new HashMap<>();
    private Map<String, IGuiHandler> clientGuiHandlers = new HashMap<>();
    public void registerGuiHandler(String mod, IGuiHandler handler) {
        this.serverGuiHandlers.put(mod, handler);

        this.clientGuiHandlers.put(mod, handler);

    }

    public static NetworkRegistry instance() {
        return INSTANCE;
    }

    public void openRemoteGui(String modId, EntityPlayerMP player, int modGuiId, World world, int x, int y, int z) {
        IGuiHandler handler = this.serverGuiHandlers.get(modId);
        if (handler != null) {
            Container container = (Container)handler.getServerGuiElement(modGuiId, player, world, x, y, z);
            if (container != null) {
                ((EntityPlayerMPAccessor) player).callIncrementWindowID();
                player.closeContainer();
                int windowId = ((EntityPlayerMPAccessor) player).getCurrentWindowId();
                Packet250CustomPayload pkt = new Packet250CustomPayload("fml|NW", generateGuiPacket(windowId, handler.getId(), modGuiId, x, y, z));
                player.playerNetServerHandler.sendPacketToPlayer(pkt);
                player.openContainer = container;
                player.openContainer.windowId = windowId;
                player.openContainer.onCraftGuiOpened(player);
            }
        }

    }

    public void openLocalGui(String modId, EntityPlayer player, int modGuiId, World world, int x, int y, int z) {
        IGuiHandler handler = this.clientGuiHandlers.get(modId);

        if (handler == null) {
            try {
                int handlerId = Integer.parseInt(modId);
                for (IGuiHandler handler1 : this.clientGuiHandlers.values()) {
                    if (handler1.getId() == handlerId){
                        handler = handler1;
                    }
                }
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
            }

        }
        if (handler != null) {
            Minecraft.getMinecraft().displayGuiScreen((GuiContainer) handler.getClientGuiElement(modGuiId, player, world, x, y, z));
        }
        else {
            BCLog.logger.warning("Packet failed to send, mod id is " + modId);
        }
    }

    public byte[] generateGuiPacket(Object... data) {
        ByteArrayDataOutput dat = ByteStreams.newDataOutput();
        dat.writeInt((Integer)data[0]);
        dat.writeInt((Integer)data[1]);
        dat.writeInt((Integer)data[2]);
        dat.writeInt((Integer)data[3]);
        dat.writeInt((Integer)data[4]);
        dat.writeInt((Integer)data[5]);
        return dat.toByteArray();
    }

    @Override
    public void postSetup() {
        this.modID = "fml";
    }

    @Override
    public void initialize() {
        registerPacketHandler("fml|NW", new ForgeCustomPacket());
    }

}
