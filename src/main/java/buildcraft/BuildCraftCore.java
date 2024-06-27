/**
 * BuildCraft is open-source. It is distributed under the terms of the
 * BuildCraft Open Source License. It grants rights to read, modify, compile or
 * run the code. It does *NOT* grant the right to redistribute this software or
 * its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */
package buildcraft;

import btw.AddonHandler;
import btw.community.example.mixin.accessors.EntityListAccessor;
import buildcraft.api.core.BuildCraftAPI;
import buildcraft.api.core.IIconProvider;
import buildcraft.api.gates.ActionManager;
import buildcraft.api.recipes.BuildcraftRecipes;
import buildcraft.core.*;
import buildcraft.core.blueprints.BptItem;
import buildcraft.core.network.EntityIds;
import buildcraft.core.network.PacketHandler;
import buildcraft.core.network.PacketUpdate;
import buildcraft.core.proxy.CoreProxy;
import buildcraft.core.recipes.AssemblyRecipeManager;
import buildcraft.core.recipes.IntegrationRecipeManager;
import buildcraft.core.recipes.RefineryRecipeManager;
import buildcraft.core.triggers.*;
import buildcraft.core.triggers.ActionMachineControl.Mode;
import buildcraft.core.utils.BCLog;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;
import net.minecraftforge.fluids.IFluidBlock;

import java.util.TreeMap;

public class BuildCraftCore implements IBuildcraftModule {

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
	public static BuildCraftConfiguration mainConfiguration;
	public static TreeMap<BlockIndex, PacketUpdate> bufferedDescriptions = new TreeMap<BlockIndex, PacketUpdate>();
	public static final int trackedPassiveEntityId = 156;
	public static boolean continuousCurrentModel;
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
	public void registerConfigProps(BuildCraftAddon addon) {

	}

	@Override
	public void handleConfigProps() {

	}

	@Override
	public void preInit() {
		BCLog.initLog();

		//todocore config
/*
		mainConfiguration = new BuildCraftConfiguration(new File(evt.getModConfigurationDirectory(), "buildcraft/main.conf"));
		try {
			mainConfiguration.load();

			Property updateCheck = BuildCraftCore.mainConfiguration.get(Configuration.CATEGORY_GENERAL, "update.check", true);
			updateCheck.comment = "set to true for version check on startup";
			if (updateCheck.getBoolean(true)) {
				Version.check();
			}

			Property continuousCurrent = BuildCraftCore.mainConfiguration.get(Configuration.CATEGORY_GENERAL, "current.continuous",
					DefaultProps.CURRENT_CONTINUOUS);
			continuousCurrent.comment = "set to true for allowing machines to be driven by continuous current";
			continuousCurrentModel = continuousCurrent.getBoolean(DefaultProps.CURRENT_CONTINUOUS);

			Property trackNetwork = BuildCraftCore.mainConfiguration.get(Configuration.CATEGORY_GENERAL, "trackNetworkUsage", false);
			trackNetworkUsage = trackNetwork.getBoolean(false);

			Property dropBlock = BuildCraftCore.mainConfiguration.get(Configuration.CATEGORY_GENERAL, "dropBrokenBlocks", true);
			dropBlock.comment = "set to false to prevent fillers from dropping blocks.";
			dropBrokenBlocks = dropBlock.getBoolean(true);

			Property lifespan = BuildCraftCore.mainConfiguration.get(Configuration.CATEGORY_GENERAL, "itemLifespan", itemLifespan);
			lifespan.comment = "the lifespan in ticks of items dropped on the ground by pipes and machines, vanilla = 6000, default = 1200";
			itemLifespan = lifespan.getInt(itemLifespan);
			if (itemLifespan < 100) {
				itemLifespan = 100;
			}

			Property factor = BuildCraftCore.mainConfiguration.get(Configuration.CATEGORY_GENERAL, "network.updateFactor", 10);
			factor.comment = "increasing this number will decrease network update frequency, useful for overloaded servers";
			updateFactor = factor.getInt(10);

			Property longFactor = BuildCraftCore.mainConfiguration.get(Configuration.CATEGORY_GENERAL, "network.stateRefreshPeriod", 40);
			longFactor.comment = "delay between full client sync packets, increasing it saves bandwidth, decreasing makes for better client syncronization.";
			longUpdateFactor = longFactor.getInt(40);

			Property wrenchId = BuildCraftCore.mainConfiguration.getItem("wrench.id", DefaultProps.WRENCH_ID);

			wrenchItem = (new ItemWrench(wrenchId.getInt(DefaultProps.WRENCH_ID))).setUnlocalizedName("wrenchItem");
			LanguageRegistry.addName(wrenchItem, "Wrench");
			CoreProxy.getProxy().registerItem(wrenchItem);

			int springId = BuildCraftCore.mainConfiguration.getBlock("springBlock.id", DefaultProps.SPRING_ID).getInt(DefaultProps.SPRING_ID);

			Property woodenGearId = BuildCraftCore.mainConfiguration.getItem("woodenGearItem.id", DefaultProps.WOODEN_GEAR_ID);
			Property stoneGearId = BuildCraftCore.mainConfiguration.getItem("stoneGearItem.id", DefaultProps.STONE_GEAR_ID);
			Property ironGearId = BuildCraftCore.mainConfiguration.getItem("ironGearItem.id", DefaultProps.IRON_GEAR_ID);
			Property goldenGearId = BuildCraftCore.mainConfiguration.getItem("goldenGearItem.id", DefaultProps.GOLDEN_GEAR_ID);
			Property diamondGearId = BuildCraftCore.mainConfiguration.getItem("diamondGearItem.id", DefaultProps.DIAMOND_GEAR_ID);
			Property modifyWorldProp = BuildCraftCore.mainConfiguration.get(Configuration.CATEGORY_GENERAL, "modifyWorld", true);
			modifyWorldProp.comment = "set to false if BuildCraft should not generate custom blocks (e.g. oil)";
			modifyWorld = modifyWorldProp.getBoolean(true);

			if (BuildCraftCore.modifyWorld && springId > 0) {
				BlockSpring.EnumSpring.WATER.canGen = BuildCraftCore.mainConfiguration.get("worldgen", "waterSpring", true).getBoolean(true);
				springBlock = new BlockSpring(springId).setUnlocalizedName("eternalSpring");
				CoreProxy.getProxy().registerBlock(springBlock, ItemSpring.class);
			}

			Property consumeWater = BuildCraftCore.mainConfiguration.get(Configuration.CATEGORY_GENERAL, "consumeWater", consumeWaterSources);
			consumeWaterSources = consumeWater.getBoolean(consumeWaterSources);
			consumeWater.comment = "set to true if the Pump should consume water";

			woodenGearItem = (new ItemBuildCraft(woodenGearId.getInt())).setUnlocalizedName("woodenGearItem");
			LanguageRegistry.addName(woodenGearItem, "Wooden Gear");
			CoreProxy.getProxy().registerItem(woodenGearItem);
			OreDictionary.registerOre("gearWood", new ItemStack(woodenGearItem));

			stoneGearItem = (new ItemBuildCraft(stoneGearId.getInt())).setUnlocalizedName("stoneGearItem");
			LanguageRegistry.addName(stoneGearItem, "Stone Gear");
			CoreProxy.getProxy().registerItem(stoneGearItem);
			OreDictionary.registerOre("gearStone", new ItemStack(stoneGearItem));

			ironGearItem = (new ItemBuildCraft(ironGearId.getInt())).setUnlocalizedName("ironGearItem");
			LanguageRegistry.addName(ironGearItem, "Iron Gear");
			CoreProxy.getProxy().registerItem(ironGearItem);
			OreDictionary.registerOre("gearIron", new ItemStack(ironGearItem));

			goldGearItem = (new ItemBuildCraft(goldenGearId.getInt())).setUnlocalizedName("goldGearItem");
			LanguageRegistry.addName(goldGearItem, "Gold Gear");
			CoreProxy.getProxy().registerItem(goldGearItem);
			OreDictionary.registerOre("gearGold", new ItemStack(goldGearItem));

			diamondGearItem = (new ItemBuildCraft(diamondGearId.getInt())).setUnlocalizedName("diamondGearItem");
			LanguageRegistry.addName(diamondGearItem, "Diamond Gear");
			CoreProxy.getProxy().registerItem(diamondGearItem);
			OreDictionary.registerOre("gearDiamond", new ItemStack(diamondGearItem));

			Property colorBlindProp = BuildCraftCore.mainConfiguration.get(Configuration.CATEGORY_GENERAL, "client.colorblindmode", false);
			colorBlindProp.comment = "Set to true to enable alternate textures";
			colorBlindMode = colorBlindProp.getBoolean(false);

		} finally {
			if (mainConfiguration.hasChanged()) {
				mainConfiguration.save();
			}
		}*/
	}

	@Override
	public void init() {
		BuildcraftRecipes.assemblyTable = AssemblyRecipeManager.INSTANCE;
		BuildcraftRecipes.integrationTable = IntegrationRecipeManager.INSTANCE;
		BuildcraftRecipes.refinery = RefineryRecipeManager.INSTANCE;

		woodenGearItem = (new ItemBuildCraft(DefaultProps.WOODEN_GEAR_ID)).setUnlocalizedName("woodenGearItem").setTextureName("woodenGearItem");
		stoneGearItem = (new ItemBuildCraft(DefaultProps.STONE_GEAR_ID)).setUnlocalizedName("stoneGearItem");
		ironGearItem = (new ItemBuildCraft(DefaultProps.IRON_GEAR_ID)).setUnlocalizedName("ironGearItem");
		goldGearItem = (new ItemBuildCraft(DefaultProps.GOLDEN_GEAR_ID)).setUnlocalizedName("goldGearItem");
		diamondGearItem = (new ItemBuildCraft(DefaultProps.DIAMOND_GEAR_ID)).setUnlocalizedName("diamondGearItem");

		wrenchItem = (new ItemWrench(DefaultProps.WRENCH_ID)).setUnlocalizedName("wrenchItem");

		// MinecraftForge.registerConnectionHandler(new ConnectionHandler());
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
		AddonHandler.registerPacketHandler("buildcraft|CR", new PacketHandler());
//		BuildCraftAddon.INSTANCE.registerPacketHandler("buildcraft|CR", new PacketHandler());
		//todocore packet handling is a total mess atm
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
		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(wrenchItem), "I I", " G ", " I ", 'I', Item.ingotIron, 'G', stoneGearItem);
		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(woodenGearItem), " S ", "S S", " S ", 'S', Item.stick);
		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(stoneGearItem), " I ", "IGI", " I ", 'I', Block.cobblestone, 'G',
				woodenGearItem);
		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(ironGearItem), " I ", "IGI", " I ", 'I', Item.ingotIron, 'G', stoneGearItem);
		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(goldGearItem), " I ", "IGI", " I ", 'I', Item.ingotGold, 'G', ironGearItem);
		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(diamondGearItem), " I ", "IGI", " I ", 'I', Item.diamond, 'G', goldGearItem);
	}


	public EnvType getEffectiveSide() {
		Thread thr = Thread.currentThread();
		return !(thr instanceof ThreadMinecraftServer) && !(thr instanceof ServerListenThread) ? EnvType.CLIENT : EnvType.SERVER;
	}
}
