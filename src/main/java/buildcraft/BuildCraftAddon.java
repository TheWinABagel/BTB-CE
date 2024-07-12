package buildcraft;

import btw.BTWAddon;
import buildcraft.core.ItemBlockBuildCraft;
import buildcraft.transport.network.PacketGateExpansionMap;
import net.minecraft.src.*;
import net.minecraftforge.PacketDispatcher;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BuildCraftAddon extends BTWAddon {

    public static final BuildCraftAddon INSTANCE = new BuildCraftAddon();
    public static final List<IBuildCraftModule> MODULES = new LinkedList<>();

    static {
        MODULES.add(BuildCraftCore.INSTANCE);
        MODULES.add(BuildCraftTransport.INSTANCE);
        MODULES.add(BuildCraftEnergy.INSTANCE);
        MODULES.add(BuildCraftFactory.INSTANCE);
        MODULES.add(BuildCraftSilicon.INSTANCE);
        MODULES.add(BuildCraftBuilders.INSTANCE);
    }

    public BuildCraftAddon() {}

    @Override
    public void postSetup() {
        this.modID = "buildcraft";
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

    private static void createAssociatedItemsForModBlocks() {
        for (int iTempBlockID = 0; iTempBlockID < 4096; ++iTempBlockID) {
            if (Block.blocksList[iTempBlockID] == null || Item.itemsList[iTempBlockID] != null) continue;
            Item.itemsList[iTempBlockID] = new ItemBlockBuildCraft(iTempBlockID - 256);
        }
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
        System.out.println("sending packet gate expansion map to players");
        PacketDispatcher.sendPacketToPlayer(pkt.getPacket(), playerMP);
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
