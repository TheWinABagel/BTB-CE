package buildcraft;

import btw.BTWAddon;
import btw.network.packet.handler.CustomPacketHandler;
import buildcraft.core.ItemBlockBuildCraft;
import buildcraft.core.utils.BCLog;
import buildcraft.transport.network.PacketGateExpansionMap;
import dev.bagel.btb.extensions.BuildcraftCustomPacketHandler;
import net.minecraft.src.*;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

public class BuildCraftAddon extends BTWAddon {

    public static final BuildCraftAddon INSTANCE = new BuildCraftAddon();
    public static final List<IBuildCraftModule> MODULES = new LinkedList<>();

    public static final Map<String, BuildcraftCustomPacketHandler> BCPacketHandlers = new HashMap<>();

    static {
        MODULES.add(BuildCraftCore.INSTANCE);
        MODULES.add(BuildCraftTransport.INSTANCE);
        MODULES.add(BuildCraftEnergy.INSTANCE);
        MODULES.add(BuildCraftFactory.INSTANCE);
        MODULES.add(BuildCraftSilicon.INSTANCE);
        MODULES.add(BuildCraftBuilders.INSTANCE);
    }

    public BuildCraftAddon() {
        this.modID = "buildcraft";
    }

    private static void createAssociatedItemsForModBlocks() {
        for (int iTempBlockID = 0; iTempBlockID < 4096; ++iTempBlockID) {
            if (Block.blocksList[iTempBlockID] == null || Item.itemsList[iTempBlockID] != null) {
                continue;
            }
            Item.itemsList[iTempBlockID] = new ItemBlockBuildCraft(iTempBlockID - 256);
        }
    }

    public static void textureHook(TextureMap map) {
        MODULES.forEach(module -> module.textureHook(map));
    }

    @Override
    public void registerPacketHandler(String channel, CustomPacketHandler handler) {

        super.registerPacketHandler(channel, handler);
    }

    public static void registerBCPacketHandler(String channel, BuildcraftCustomPacketHandler handler) {
        System.out.println("CHANNEL " + channel);
        INSTANCE.registerPacketHandler(channel, handler);
    }

    @Override
    public void postSetup() {
        MODULES.forEach(IBuildCraftModule::postSetup);
    }

    @Override
    public void preInitialize() {
        MODULES.forEach(module -> module.registerConfigForSettings(this));
        MODULES.forEach(module -> module.registerConfigForIds(this));
        MODULES.forEach(IBuildCraftModule::preInit);
    }

    @Override
    public void initialize() {
        MODULES.forEach(IBuildCraftModule::init);
        createAssociatedItemsForModBlocks();
    }

    @Override
    public void handleConfigProperties(Map<String, String> propertyValues) {
        BuildcraftConfig.putAll(propertyValues);
        MODULES.forEach(IBuildCraftModule::handleConfigProps);
    }

    @Override
    public void postInitialize() {
        MODULES.forEach(IBuildCraftModule::postInit);
        if (BuildCraftCore.loadDefaultRecipes) {
            MODULES.forEach(IBuildCraftModule::initRecipes);
        }
    }

    //originally from transport
    @Override
    public void serverPlayerConnectionInitialized(NetServerHandler serverHandler, EntityPlayerMP playerMP) {
        PacketGateExpansionMap pkt = new PacketGateExpansionMap();
        System.out.println("Sending packet gate expansion map to " + playerMP.getEntityName());
        playerMP.playerNetServerHandler.sendPacketToPlayer(pkt.getPacket());
    }

    @Override
    public boolean serverCustomPacketReceived(NetServerHandler handler, Packet250CustomPayload packet) {
        try {
            if (BuildCraftAddon.BCPacketHandlers.get(packet.channel) != null) {
                DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
                int packetID = data.read();
                for (BuildcraftCustomPacketHandler packetHandler : BuildCraftAddon.BCPacketHandlers.values()) {
//                    packetHandler.onPacketData(handler.playerEntity, packet, data, packetID);
                }
                return true;
            }
        } catch (IOException e) {
            BCLog.logger.severe("Server failed to receive a custom packet!\n" + Arrays.toString(e.getStackTrace()));
            return false;
        }
        return false;
    }

    @Override
    public boolean clientCustomPacketReceived(Minecraft mcInstance, Packet250CustomPayload packet) {
        try {
            if (BuildCraftAddon.BCPacketHandlers.get(packet.channel) != null) {
                DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
                int packetID = data.read();
                for (BuildcraftCustomPacketHandler packetHandler : BuildCraftAddon.BCPacketHandlers.values()) {
//                    packetHandler.onPacketData(mcInstance.thePlayer, packet, data, packetID);
                }
                return true;
            }
        } catch (IOException e) {
            BCLog.logger.severe("Client failed to receive a custom packet!\n" + Arrays.toString(e.getStackTrace()));
            return false;
        }
        return false;
    }

    public void registerProp(String propertyName, Object defaultValue, String comment) {
        this.registerProperty(propertyName, defaultValue.toString(), comment);
    }

    public void registerProp(String propertyName, Object defaultValue) {
        this.registerProperty(propertyName, defaultValue.toString(), "");
    }
}
