/**
 * BuildCraft is open-source. It is distributed under the terms of the
 * BuildCraft Open Source License. It grants rights to read, modify, compile or
 * run the code. It does *NOT* grant the right to redistribute this software or
 * its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */
package buildcraft;

import btw.client.network.packet.handler.CustomEntityPacketHandler;
import dev.bagel.btb.mixin.accessors.EntityListAccessor;
import buildcraft.api.core.BuildCraftAPI;
import buildcraft.api.core.IIconProvider;
import buildcraft.api.gates.ActionManager;
import buildcraft.api.recipes.BuildcraftRecipes;
import buildcraft.core.*;
import buildcraft.core.blueprints.BptItem;
import buildcraft.core.network.EntityIds;
import buildcraft.core.network.PacketHandlerCore;
import buildcraft.core.network.PacketUpdate;
import buildcraft.core.proxy.CoreProxy;
import buildcraft.core.recipes.AssemblyRecipeManager;
import buildcraft.core.recipes.IntegrationRecipeManager;
import buildcraft.core.recipes.RefineryRecipeManager;
import buildcraft.core.triggers.*;
import buildcraft.core.triggers.ActionMachineControl.Mode;
import buildcraft.core.utils.BCLog;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.RenderBlockFluid;

import java.util.TreeMap;

public class BuildCraftCore implements IBuildCraftModule {

	public enum RenderMode {
		Full, NoDynamic
	};
	public static RenderMode render = RenderMode.Full;
	public static boolean debugMode = false;
	public static boolean modifyWorld = false;
	public static boolean trackNetworkUsage = false;
	public static boolean colorBlindMode = false;
	public static boolean dropBrokenBlocks = true; // Set to false to prevent the filler from dropping broken blocks.
	public static int itemLifespan = 1200;
	public static int updateFactor = 10;
	public static long longUpdateFactor = 40;
	public static TreeMap<BlockIndex, PacketUpdate> bufferedDescriptions = new TreeMap<>();
	public static Block springBlock;
	public static Item woodenGearItem;
	public static Item stoneGearItem;
	public static Item ironGearItem;
	public static Item goldGearItem;
	public static Item diamondGearItem;
	public static Item wrenchItem;
	@Environment(EnvType.CLIENT)
	public static Icon redLaserTexture;
	@Environment(EnvType.CLIENT)
	public static Icon blueLaserTexture;
	@Environment(EnvType.CLIENT)
	public static Icon stripesLaserTexture;
	@Environment(EnvType.CLIENT)
	public static Icon transparentTexture;
	@Environment(EnvType.CLIENT)
	public static IIconProvider iconProvider;
	public static int blockByEntityModel;
	public static int legacyPipeModel;
	public static int markerModel;
	public static int oilModel;
	public static BCTrigger triggerMachineActive = new TriggerMachine(true);
	public static BCTrigger triggerMachineInactive = new TriggerMachine(false);
	public static BCTrigger triggerEmptyInventory = new TriggerInventory(TriggerInventory.State.Empty);
	public static BCTrigger triggerContainsInventory = new TriggerInventory(TriggerInventory.State.Contains);
	public static BCTrigger triggerSpaceInventory = new TriggerInventory(TriggerInventory.State.Space);
	public static BCTrigger triggerFullInventory = new TriggerInventory(TriggerInventory.State.Full);
	public static BCTrigger triggerEmptyFluid = new TriggerFluidContainer(TriggerFluidContainer.State.Empty);
	public static BCTrigger triggerContainsFluid = new TriggerFluidContainer(TriggerFluidContainer.State.Contains);
	public static BCTrigger triggerSpaceFluid = new TriggerFluidContainer(TriggerFluidContainer.State.Space);
	public static BCTrigger triggerFullFluid = new TriggerFluidContainer(TriggerFluidContainer.State.Full);
	public static BCTrigger triggerRedstoneActive = new TriggerRedstoneInput(true);
	public static BCTrigger triggerRedstoneInactive = new TriggerRedstoneInput(false);
	public static BCTrigger triggerInventoryBelow25 = new TriggerInventoryLevel(TriggerInventoryLevel.TriggerType.BELOW_25);
	public static BCTrigger triggerInventoryBelow50 = new TriggerInventoryLevel(TriggerInventoryLevel.TriggerType.BELOW_50);
	public static BCTrigger triggerInventoryBelow75 = new TriggerInventoryLevel(TriggerInventoryLevel.TriggerType.BELOW_75);
	public static BCAction actionRedstone = new ActionRedstoneOutput();
	public static BCAction actionOn = new ActionMachineControl(Mode.On);
	public static BCAction actionOff = new ActionMachineControl(Mode.Off);
	public static BCAction actionLoop = new ActionMachineControl(Mode.Loop);
	public static boolean loadDefaultRecipes = true;
	public static boolean forcePneumaticPower = true;
	public static boolean consumeWaterSources = false;
	public static BptItem[] itemBptProps = new BptItem[Item.itemsList.length];

	public static BuildCraftCore INSTANCE = new BuildCraftCore();

	@Override
	public void registerConfigForIds(BuildCraftAddon addon) {
		addon.registerProp("WrenchItemId", DefaultProps.WRENCH_ID, "***IDs***\n\n# Core\n");

		addon.registerProp("WoodenGearItemId", DefaultProps.WOODEN_GEAR_ID);
		addon.registerProp("StoneGearItemId", DefaultProps.STONE_GEAR_ID);
		addon.registerProp("IronGearItemId", DefaultProps.IRON_GEAR_ID);
		addon.registerProp("GoldenGearItemId", DefaultProps.GOLDEN_GEAR_ID);
		addon.registerProp("DiamondGearItemId", DefaultProps.DIAMOND_GEAR_ID);

		addon.registerProp("SpringBlockId", DefaultProps.SPRING_ID);
	}

	@Override
	public void registerConfigForSettings(BuildCraftAddon addon) {
		addon.registerProp("TrackNetworkUsage", false, "Main Buildcraft Configuration\n\n# Core\n\n# Tracks network usage. Used for debugging, not useful in normal play. Default: false (Boolean)");
		addon.registerProp("FillersDropBrokenBlocks", true, "Set to false to prevent fillers from dropping blocks. Default: true (Boolean)");
		addon.registerProp("ItemLifespan", 1200, "The lifespan in ticks of items dropped on the ground by pipes and machines. Vanilla: 6000, Default: 1200 (Integer)");
		addon.registerProp("NetworkUpdateFactor", 10, "Network update frequency, higher numbers are less frequent. Default: 10 (Integer)");
		addon.registerProp("NetworkStateRefreshPeriod", 40, "Delay between full client sync packets, higher numbers save bandwidth, lower numbers make for better client synchronization. Default: 40 (Integer)");
		addon.registerProp("ModifyWorld", true, "Set to false to disable worldgen (ie oil). Default: true (Boolean)");
		addon.registerProp("GenerateWaterSprings", true, "Set to false to disable water spring generation. Default: true (Boolean)");
		addon.registerProp("ConsumeWater", true, "Set to false to disable the pump consuming water sources. Default: true (Boolean)");
		addon.registerProp("ColorBlindMode", false, "Set to true to enable alternate, colorblind friendly textures. Default: false (Boolean)");
	}

	@Override
	public void handleConfigProps() {
		trackNetworkUsage = BuildcraftConfig.getBoolean("TrackNetworkUsage");
		dropBrokenBlocks = BuildcraftConfig.getBoolean("FillersDropBrokenBlocks");
		itemLifespan = BuildcraftConfig.getInt("ItemLifespan");
		if (itemLifespan < 100) itemLifespan = 100;
		updateFactor = BuildcraftConfig.getInt("NetworkUpdateFactor");
		longUpdateFactor = BuildcraftConfig.getInt("NetworkStateRefreshPeriod");
		modifyWorld = BuildcraftConfig.getBoolean("ModifyWorld");
		BlockSpring.EnumSpring.WATER.canGen = BuildcraftConfig.getBoolean("GenerateWaterSprings");
		consumeWaterSources = BuildcraftConfig.getBoolean("ConsumeWater");
		colorBlindMode = BuildcraftConfig.getBoolean("ColorBlindMode");

		BuildcraftConfig.gearWoodItemId = BuildcraftConfig.getInt("WoodenGearItemId");
		BuildcraftConfig.gearStoneItemId = BuildcraftConfig.getInt("StoneGearItemId");
		BuildcraftConfig.gearIronItemId = BuildcraftConfig.getInt("IronGearItemId");
		BuildcraftConfig.gearGoldItemId = BuildcraftConfig.getInt("GoldenGearItemId");
		BuildcraftConfig.gearDiamondItemId = BuildcraftConfig.getInt("DiamondGearItemId");

		BuildcraftConfig.wrenchItemId = BuildcraftConfig.getInt("WrenchItemId");
		BuildcraftConfig.springBlockId = BuildcraftConfig.getInt("SpringBlockId");
	}

	@Override
	public void preInit() {
		BCLog.initLog();
	}

	@Override
	public void init() {
		BuildcraftRecipes.assemblyTable = AssemblyRecipeManager.INSTANCE;
		BuildcraftRecipes.integrationTable = IntegrationRecipeManager.INSTANCE;
		BuildcraftRecipes.refinery = RefineryRecipeManager.INSTANCE;

		woodenGearItem = new ItemBuildCraft(BuildcraftConfig.gearWoodItemId).setUnlocalizedName("woodenGearItem").setTextureName("woodenGearItem");
		stoneGearItem = new ItemBuildCraft(BuildcraftConfig.gearStoneItemId).setUnlocalizedName("stoneGearItem");
		ironGearItem = new ItemBuildCraft(BuildcraftConfig.gearIronItemId).setUnlocalizedName("ironGearItem");
		goldGearItem = new ItemBuildCraft(BuildcraftConfig.gearGoldItemId).setUnlocalizedName("goldGearItem");
		diamondGearItem = new ItemBuildCraft(BuildcraftConfig.gearDiamondItemId).setUnlocalizedName("diamondGearItem");

		wrenchItem = new ItemWrench(BuildcraftConfig.wrenchItemId);

		if (BuildcraftConfig.springBlockId > 0) {
			springBlock = new BlockSpring(BuildcraftConfig.springBlockId).setUnlocalizedName("eternalSpring");
		}

		ActionManager.registerTriggerProvider(new DefaultTriggerProvider());
		ActionManager.registerActionProvider(new DefaultActionProvider());

		initPackets();

		EntityList.addMapping(EntityRobot.class, "bcRobot", EntityIds.ROBOT);
		EntityList.addMapping(EntityPowerLaser.class, "bcLaser", EntityIds.LASER);
		EntityList.addMapping(EntityEnergyLaser.class, "bcEnergyLaser", EntityIds.ENERGY_LASER);
		EntityListAccessor.getClassToStringMapping().remove(EntityRobot.class);
		EntityListAccessor.getClassToStringMapping().remove(EntityPowerLaser.class);
		EntityListAccessor.getClassToStringMapping().remove(EntityEnergyLaser.class);
		EntityListAccessor.getClassToStringMapping().remove("BuildCraft|Core.bcRobot");
		EntityListAccessor.getClassToStringMapping().remove("BuildCraft|Core.bcLaser");
		EntityListAccessor.getClassToStringMapping().remove("BuildCraft|Core.bcEnergyLaser");

		CoreProxy.getProxy().initializeRendering();
		CoreProxy.getProxy().initializeEntityRendering();

		registerCommand();
		RenderingRegistry.registerBlockHandler(RenderBlockFluid.instance);

		if (!MinecraftServer.getIsServer()) {
			CustomEntityPacketHandler.entryMap.put(EntityIds.ROBOT, ((world, data, packet) -> {
				Box box = new Box();
				box.initialize(data);

                return new EntityRobot(world, box);
			}));
		}
	}

	@Override
	public void initRecipes() {
		loadRecipes();
	}

	@Override
	public void postInit() {
		for (Block block : Block.blocksList) {
			if (block instanceof BlockFluid || block instanceof IFluidBlock /*|| block instanceof IPlantable*/) {
				BuildCraftAPI.softBlocks[block.blockID] = true;
			}
		}

		BuildCraftAPI.softBlocks[Block.snow.blockID] = true;
		BuildCraftAPI.softBlocks[Block.vine.blockID] = true;
		BuildCraftAPI.softBlocks[Block.fire.blockID] = true;
	}

	@Override
	public String getModId() {
		return "bccore";
	}

	private static void initPackets() {
        BuildCraftAddon.registerBCPacketHandler(DefaultProps.CORE_CHANNEL_NAME, new PacketHandlerCore());
        //packet handling is a total mess atm...
//		BuildCraftCore.instance.registerPacketHandler("buildcraft|TP", new PacketHandlerTransport());
//		BuildCraftCore.instance.registerPacketHandler("buildcraft|SC", new PacketHandlerSilicon());
	}


	public void registerCommand() {
		BuildCraftAddon.INSTANCE.registerAddonCommand(new CommandBuildCraft());
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void textureHook(TextureMap map) {
		if (map.getTextureType() == 1) {
			iconProvider = new CoreIconProvider();
			iconProvider.registerIcons(map);
			ActionTriggerIconProvider.INSTANCE.registerIcons(map);
		} else if (map.getTextureType() == 0) {
			BuildCraftCore.redLaserTexture = map.registerIcon("buildcraft:blockRedLaser");
			BuildCraftCore.blueLaserTexture = map.registerIcon("buildcraft:blockBlueLaser");
			BuildCraftCore.stripesLaserTexture = map.registerIcon("buildcraft:blockStripesLaser");
			BuildCraftCore.transparentTexture = map.registerIcon("buildcraft:blockTransparentLaser");
		}

	}

	public void loadRecipes() {
		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(wrenchItem),
				"I I",
				" G ",
				" I ",
				'I', Item.ingotIron,
				'G', stoneGearItem);

		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(woodenGearItem),
				" S ",
				"S S",
				" S ",
				'S', Item.stick);
		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(stoneGearItem),
				" I ",
				"IGI",
				" I ",
				'I', Block.cobblestone,
				'G', woodenGearItem);
		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(ironGearItem),
				" I ",
				"IGI",
				" I ",
				'I', Item.ingotIron,
				'G', stoneGearItem);
		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(goldGearItem),
				" I ",
				"IGI",
				" I ",
				'I', Item.ingotGold,
				'G', ironGearItem);
		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(diamondGearItem),
				" I ",
				"IGI",
				" I ",
				'I', Item.diamond,
				'G', goldGearItem);
	}


	public EnvType getEffectiveSide() {
		Thread thr = Thread.currentThread();
		return !(thr instanceof ThreadMinecraftServer) && !(thr instanceof ServerListenThread) ? EnvType.CLIENT : EnvType.SERVER;
	}
}
