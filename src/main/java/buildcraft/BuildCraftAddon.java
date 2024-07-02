package buildcraft;

import btw.BTWAddon;
import buildcraft.core.DefaultProps;
import buildcraft.core.ItemBlockBuildCraft;
import buildcraft.core.network.PacketHandler;
import buildcraft.transport.network.PacketGateExpansionMap;
import net.minecraft.src.*;
import net.minecraftforge.PacketDispatcher;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BuildCraftAddon extends BTWAddon {

    public static final BuildCraftAddon INSTANCE = new BuildCraftAddon();
    public static final List<IBuildcraftModule> MODULES = new LinkedList<>();

    static {
        MODULES.add(BuildCraftCore.INSTANCE);
        MODULES.add(BuildCraftTransport.INSTANCE);
        MODULES.add(BuildCraftEnergy.INSTANCE);
        MODULES.add(BuildCraftFactory.INSTANCE);
        MODULES.add(BuildCraftSilicon.INSTANCE);
//        MODULES.add(BuildCraftBuilders.INSTANCE);
    }

    public BuildCraftAddon() {

    }



    @Override
    public void postSetup() {
        this.modID = "buildcraft";
        MODULES.forEach(IBuildcraftModule::postSetup);
    }

    @Override
    public void preInitialize() {
        MODULES.forEach(module -> module.registerConfigForSettings(this));
        MODULES.forEach(module -> module.registerConfigForIds(this));
        MODULES.forEach(IBuildcraftModule::preInit);
    }

    @Override
    public void initialize() {
        MODULES.forEach(IBuildcraftModule::init);
        createAssociatedItemsForModBlocks();
    }

    private static void createAssociatedItemsForModBlocks() {
        for (int iTempBlockID = 0; iTempBlockID < 4096; ++iTempBlockID) {
            if (Block.blocksList[iTempBlockID] == null || Item.itemsList[iTempBlockID] != null) continue;
            Item.itemsList[iTempBlockID] = new ItemBlockBuildCraft(iTempBlockID - 256);
        }
    }

    @Override
    public void handleConfigProperties(Map<String, String> propertyValues) {
        BuildcraftConfig.putAll(propertyValues);
        MODULES.forEach(IBuildcraftModule::handleConfigProps);
    }



    @Override
    public void postInitialize() {
        MODULES.forEach(IBuildcraftModule::postInit);
        if (BuildCraftCore.loadDefaultRecipes) {
            MODULES.forEach(IBuildcraftModule::initRecipes);
        }
    }

    //originally from transport
    @Override
    public void serverPlayerConnectionInitialized(NetServerHandler serverHandler, EntityPlayerMP playerMP) {
        PacketGateExpansionMap pkt = new PacketGateExpansionMap();
        PacketDispatcher.sendPacketToPlayer(pkt.getPacket(), playerMP);
    }

    @Override
    public boolean serverCustomPacketReceived(NetServerHandler handler, Packet250CustomPayload packet) {
        PacketHandler.INSTANCE.onPacketData(handler.playerEntity, packet);
        return super.serverCustomPacketReceived(handler, packet);
    }

    @Override
    public boolean clientCustomPacketReceived(Minecraft mcInstance, Packet250CustomPayload packet) {
        PacketHandler.INSTANCE.onPacketData(mcInstance.thePlayer, packet);
        return super.clientCustomPacketReceived(mcInstance, packet);
    }

    public static void textureHook(TextureMap map) {
        MODULES.forEach(module -> module.textureHook(map));
    }

    public void registerProp(String propertyName, Object defaultValue, String comment) {
        this.registerProperty(propertyName, defaultValue.toString(), comment);
    }


    public void registerProp(String propertyName, Object defaultValue) {
        this.registerProperty(propertyName, defaultValue.toString(), "");
    }
}
