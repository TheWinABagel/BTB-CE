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
    public final Map<String, IGuiHandler> serverGuiHandlers = new HashMap<>();
    public final Map<String, IGuiHandler> clientGuiHandlers = new HashMap<>();
    public void registerGuiHandler(String mod, IGuiHandler handler) {
        this.serverGuiHandlers.put(mod, handler);
        this.clientGuiHandlers.put(mod, handler);
    }

    public static NetworkRegistry instance() {
        return INSTANCE;
    }

    @Override
    public void initialize() {
        this.modID = "fml";
        registerPacketHandler("fml|NW", new ForgeCustomPacket());
    }

}
