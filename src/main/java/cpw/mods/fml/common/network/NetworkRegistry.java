package cpw.mods.fml.common.network;

import btw.community.example.mixin.EntityPlayerMPAccessor;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.minecraft.src.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class NetworkRegistry {
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
                Packet250CustomPayload pkt = new Packet250CustomPayload();
                pkt.channel = "FML";
                pkt.data = generateGuiPacket(/*FMLPacket.Type.GUIOPEN, */windowId, modId, modGuiId, x, y, z);
                pkt.length = pkt.data.length;
                player.playerNetServerHandler.sendPacketToPlayer(pkt);
                player.openContainer = container;
                player.openContainer.windowId = windowId;
                player.openContainer.onCraftGuiOpened(player);
            }
        }

    }

    public void openLocalGui(String modId, EntityPlayer player, int modGuiId, World world, int x, int y, int z) {
        IGuiHandler handler = this.clientGuiHandlers.get(modId);
        Minecraft.getMinecraft().displayGuiScreen((GuiContainer) handler.getClientGuiElement(modGuiId, player, world, x, y, z));
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
}
