/**
 * BuildCraft is open-source. It is distributed under the terms of the
 * BuildCraft Open Source License. It grants rights to read, modify, compile or
 * run the code. It does *NOT* grant the right to redistribute this software or
 * its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */
package buildcraft;

import btw.crafting.recipe.RecipeManager;
import btw.util.color.Color;
import buildcraft.api.core.IIconProvider;
import buildcraft.api.gates.ActionManager;
import buildcraft.api.gates.GateExpansions;
import buildcraft.api.recipes.BuildcraftRecipes;
import buildcraft.api.transport.IExtractionHandler;
import buildcraft.api.transport.PipeManager;
import buildcraft.api.transport.PipeWire;
import buildcraft.core.DefaultProps;
import buildcraft.core.ItemBuildCraft;
import buildcraft.core.proxy.CoreProxy;
import buildcraft.core.triggers.BCAction;
import buildcraft.core.triggers.BCTrigger;
import buildcraft.core.utils.EnumColor;
import buildcraft.transport.*;
import buildcraft.transport.blueprints.*;
import buildcraft.transport.gates.GateExpansionPulsar;
import buildcraft.transport.gates.GateExpansionRedstoneFader;
import buildcraft.transport.gates.GateExpansionTimer;
import buildcraft.transport.gates.ItemGate;
import buildcraft.transport.pipes.*;
import buildcraft.transport.pipes.PipePowerIron.PowerMode;
import buildcraft.transport.triggers.*;
import buildcraft.transport.triggers.TriggerClockTimer.Time;
import buildcraft.transport.triggers.TriggerPipeContents.PipeContents;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.src.*;
import net.minecraftforge.common.ForgeDirection;

import java.util.LinkedList;

public class BuildCraftTransport implements IBuildcraftModule {

	public static BlockGenericPipe genericPipeBlock;
	public static float pipeDurability;
	public static Item pipeWaterproof;
	public static Item pipeGate;
	public static Item pipeWire;
	public static Item pipeItemsWood;
	public static Item pipeItemsEmerald;
	public static Item pipeItemsStone;
	public static Item pipeItemsCobblestone;
	public static Item pipeItemsIron;
	public static Item pipeItemsQuartz;
	public static Item pipeItemsGold;
	public static Item pipeItemsDiamond;
	public static Item pipeItemsObsidian;
	public static Item pipeItemsLapis;
	public static Item pipeItemsDaizuli;
	public static Item pipeItemsVoid;
	public static Item pipeItemsSandstone;
	public static Item pipeItemsEmzuli;
	public static Item pipeFluidsWood;
	public static Item pipeFluidsCobblestone;
	public static Item pipeFluidsStone;
	public static Item pipeFluidsIron;
	public static Item pipeFluidsGold;
	public static Item pipeFluidsVoid;
	public static Item pipeFluidsSandstone;
	public static Item pipeFluidsEmerald;
	public static Item pipePowerWood;
	public static Item pipePowerCobblestone;
	public static Item pipePowerStone;
	public static Item pipePowerQuartz;
	public static Item pipePowerIron;
	public static Item pipePowerGold;
	public static Item pipePowerDiamond;
	public static ItemFacade facadeItem;
	public static Item plugItem;
	public static BlockFilteredBuffer filteredBufferBlock;
	// public static Item pipeItemsStipes;
	public static Item pipeStructureCobblestone;
	public static int groupItemsTrigger;
	public static BCTrigger[] triggerPipe = new BCTrigger[PipeContents.values().length];
	public static BCTrigger[] triggerPipeWireActive = new BCTrigger[PipeWire.values().length];
	public static BCTrigger[] triggerPipeWireInactive = new BCTrigger[PipeWire.values().length];
	public static BCTrigger[] triggerTimer = new BCTrigger[Time.VALUES.length];
	public static BCTrigger[] triggerRedstoneLevel = new BCTrigger[15];
	public static BCAction[] actionPipeWire = new ActionSignalOutput[PipeWire.values().length];
	public static BCAction actionEnergyPulser = new ActionEnergyPulsar();
	public static BCAction actionSingleEnergyPulse = new ActionSingleEnergyPulse();
	public static BCAction[] actionPipeColor = new BCAction[16];
	public static BCAction[] actionPipeDirection = new BCAction[16];
	public static BCAction[] actionPowerLimiter = new BCAction[7];
	public static BCAction[] actionRedstoneLevel = new BCAction[15];
	public static BCAction actionExtractionPresetRed = new ActionExtractionPreset(EnumColor.RED);
	public static BCAction actionExtractionPresetBlue = new ActionExtractionPreset(EnumColor.BLUE);
	public static BCAction actionExtractionPresetGreen = new ActionExtractionPreset(EnumColor.GREEN);
	public static BCAction actionExtractionPresetYellow = new ActionExtractionPreset(EnumColor.YELLOW);
	public IIconProvider pipeIconProvider = new PipeIconProvider();
	public IIconProvider wireIconProvider = new WireIconProvider();

	public static BuildCraftTransport INSTANCE = new BuildCraftTransport();

	private static class PipeRecipe {

		boolean isShapeless = false; // pipe recipes come shaped and unshaped.
		ItemStack result;
		Object[] input;
	}
	private static final LinkedList<PipeRecipe> pipeRecipes = new LinkedList<>();

	@Override
	public void preInit() {

	}

	@Override
	public void registerConfigForSettings(BuildCraftAddon addon) {
		addon.registerProp("PipeDurability", String.valueOf(DefaultProps.PIPES_DURABILITY), "Transport\n\n# How long a pipe will take to break. Default: 0.25 (Float)");
		addon.registerProp("GroupItemsTriggerAmount", String.valueOf(32), "Amount required before pipes automatically group items. Default: 32 (Integer)");
		addon.registerProp("PipeItemExclusionList", "itemName1, itemName2", "List of unlocalized names of items excluded from being pumped with a pipe. Default: none (Comma and space delimited list)");
		addon.registerProp("PipeFluidExclusionList", "fluidName1, fluidName2", "List of unlocalized names of fluids excluded from being pumped with a pipe. Default: none (Comma and space delimited list)");
	}

	@Override
	public void registerConfigForIds(BuildCraftAddon addon) {
		addon.registerProp("FilteredBufferBlockId", String.valueOf(DefaultProps.FILTERED_BUFFER_ID), "Transport\n");
		addon.registerProp("GenericPipeBlockId", String.valueOf(DefaultProps.GENERIC_PIPE_ID));
		addon.registerProp("PipeWaterproofItemId", String.valueOf(DefaultProps.PIPE_WATERPROOF_ID));
		addon.registerProp("PipeWireItemId", String.valueOf(DefaultProps.PIPE_WIRE));
		addon.registerProp("PipeGateItemId", String.valueOf(DefaultProps.GATE_ID));
		addon.registerProp("PipeFacadeItemId", String.valueOf(DefaultProps.PIPE_FACADE_ID));
		addon.registerProp("PipePlugItemId", String.valueOf(DefaultProps.PIPE_PLUG_ID));

	}

	@Override
	public void handleConfigProps() {
		pipeDurability = BuildcraftConfig.getFloat("PipeDurability");

		BuildcraftConfig.filteredBufferBlockId = BuildcraftConfig.getInt("FilteredBufferBlockId");
		BuildcraftConfig.genericPipeBlockId = BuildcraftConfig.getInt("GenericPipeBlockId");
		BuildcraftConfig.pipeWaterproofItemId = BuildcraftConfig.getInt("PipeWaterproofItemId");
		BuildcraftConfig.pipeWireItemId = BuildcraftConfig.getInt("PipeWireItemId");
		BuildcraftConfig.pipeGateItemId = BuildcraftConfig.getInt("PipeGateItemId");
		BuildcraftConfig.pipeFacadeItemId = BuildcraftConfig.getInt("PipeFacadeItemId");
		BuildcraftConfig.pipePlugItemId = BuildcraftConfig.getInt("PipePlugItemId");

		groupItemsTrigger = BuildcraftConfig.getInt("GroupItemsTriggerAmount");

		String[] excludedItemBlocks = BuildcraftConfig.getStringList("PipeItemExclusionList");
		if (excludedItemBlocks != null) {
			for (int j = 0; j < excludedItemBlocks.length; ++j) {
				excludedItemBlocks[j] = excludedItemBlocks[j].trim();
			}
		} else
			excludedItemBlocks = new String[0];

		String[] excludedFluidBlocks = BuildcraftConfig.getStringList("PipeFluidExclusionList");
		if (excludedFluidBlocks != null) {
			for (int j = 0; j < excludedFluidBlocks.length; ++j) {
				excludedFluidBlocks[j] = excludedFluidBlocks[j].trim();
			}
		} else
			excludedFluidBlocks = new String[0];


		PipeManager.registerExtractionHandler(new ExtractionHandler(excludedItemBlocks, excludedFluidBlocks));
	}

	@Override
	public void init() {
		filteredBufferBlock = new BlockFilteredBuffer(BuildcraftConfig.filteredBufferBlockId);
		filteredBufferBlock.setUnlocalizedName("filteredBufferBlock");
		try {
			GateExpansions.registerExpansion(GateExpansionPulsar.INSTANCE);
			GateExpansions.registerExpansion(GateExpansionTimer.INSTANCE);
			GateExpansions.registerExpansion(GateExpansionRedstoneFader.INSTANCE);

			pipeWaterproof = new ItemBuildCraft(BuildcraftConfig.pipeWaterproofItemId);
			pipeWaterproof.setUnlocalizedName("pipeWaterproof");
			CoreProxy.getProxy().registerItem(pipeWaterproof);

			genericPipeBlock = new BlockGenericPipe(BuildcraftConfig.genericPipeBlockId);
			CoreProxy.getProxy().registerBlock(genericPipeBlock.setUnlocalizedName("pipeBlock"), ItemBlock.class);

			pipeItemsWood = buildPipe(DefaultProps.PIPE_ITEMS_WOOD_ID, PipeItemsWood.class, "Wooden Transport Pipe", Block.planks, Block.glass, Block.planks);
			pipeItemsEmerald = buildPipe(DefaultProps.PIPE_ITEMS_EMERALD_ID, PipeItemsEmerald.class, "Emerald Transport Pipe", Item.emerald, Block.glass, Item.emerald);
			pipeItemsCobblestone = buildPipe(DefaultProps.PIPE_ITEMS_COBBLESTONE_ID, PipeItemsCobblestone.class, "Cobblestone Transport Pipe", Block.cobblestone, Block.glass, Block.cobblestone);
			pipeItemsStone = buildPipe(DefaultProps.PIPE_ITEMS_STONE_ID, PipeItemsStone.class, "Stone Transport Pipe", Block.stone, Block.glass, Block.stone);
			pipeItemsQuartz = buildPipe(DefaultProps.PIPE_ITEMS_QUARTZ_ID, PipeItemsQuartz.class, "Quartz Transport Pipe", Block.blockNetherQuartz, Block.glass, Block.blockNetherQuartz);
			pipeItemsIron = buildPipe(DefaultProps.PIPE_ITEMS_IRON_ID, PipeItemsIron.class, "Iron Transport Pipe", Item.ingotIron, Block.glass, Item.ingotIron);
			pipeItemsGold = buildPipe(DefaultProps.PIPE_ITEMS_GOLD_ID, PipeItemsGold.class, "Golden Transport Pipe", Item.ingotGold, Block.glass, Item.ingotGold);
			pipeItemsDiamond = buildPipe(DefaultProps.PIPE_ITEMS_DIAMOND_ID, PipeItemsDiamond.class, "Diamond Transport Pipe", Item.diamond, Block.glass, Item.diamond);
			pipeItemsObsidian = buildPipe(DefaultProps.PIPE_ITEMS_OBSIDIAN_ID, PipeItemsObsidian.class, "Obsidian Transport Pipe", Block.obsidian, Block.glass, Block.obsidian);
			pipeItemsLapis = buildPipe(DefaultProps.PIPE_ITEMS_LAPIS_ID, PipeItemsLapis.class, "Lapis Transport Pipe", Block.blockLapis, Block.glass, Block.blockLapis);
			pipeItemsDaizuli = buildPipe(DefaultProps.PIPE_ITEMS_DAIZULI_ID, PipeItemsDaizuli.class, "Daizuli Transport Pipe", Block.blockLapis, Block.glass, Item.diamond);
			pipeItemsSandstone = buildPipe(DefaultProps.PIPE_ITEMS_SANDSTONE_ID, PipeItemsSandstone.class, "Sandstone Transport Pipe", Block.sandStone, Block.glass, Block.sandStone);
			pipeItemsVoid = buildPipe(DefaultProps.PIPE_ITEMS_VOID_ID, PipeItemsVoid.class, "Void Transport Pipe", new ItemStack(Item.dyePowder, 1, Color.BLACK.colorID), Block.glass, Item.redstone);
			pipeItemsEmzuli = buildPipe(DefaultProps.PIPE_ITEMS_EMZULI_ID, PipeItemsEmzuli.class, "Emzuli Transport Pipe", Block.blockLapis, Block.glass, Item.emerald);

			pipeFluidsWood = buildPipe(DefaultProps.PIPE_LIQUIDS_WOOD_ID, PipeFluidsWood.class, "Wooden Waterproof Pipe", pipeWaterproof, pipeItemsWood);
			pipeFluidsCobblestone = buildPipe(DefaultProps.PIPE_LIQUIDS_COBBLESTONE_ID, PipeFluidsCobblestone.class, "Cobblestone Waterproof Pipe", pipeWaterproof, pipeItemsCobblestone);
			pipeFluidsStone = buildPipe(DefaultProps.PIPE_LIQUIDS_STONE_ID, PipeFluidsStone.class, "Stone Waterproof Pipe", pipeWaterproof, pipeItemsStone);
			pipeFluidsIron = buildPipe(DefaultProps.PIPE_LIQUIDS_IRON_ID, PipeFluidsIron.class, "Iron Waterproof Pipe", pipeWaterproof, pipeItemsIron);
			pipeFluidsGold = buildPipe(DefaultProps.PIPE_LIQUIDS_GOLD_ID, PipeFluidsGold.class, "Golden Waterproof Pipe", pipeWaterproof, pipeItemsGold);
			pipeFluidsEmerald = buildPipe(DefaultProps.PIPE_LIQUIDS_EMERALD_ID, PipeFluidsEmerald.class, "Emerald Waterproof Pipe", pipeWaterproof, pipeItemsEmerald);
			pipeFluidsSandstone = buildPipe(DefaultProps.PIPE_LIQUIDS_SANDSTONE_ID, PipeFluidsSandstone.class, "Sandstone Waterproof Pipe", pipeWaterproof, pipeItemsSandstone);
			pipeFluidsVoid = buildPipe(DefaultProps.PIPE_LIQUIDS_VOID_ID, PipeFluidsVoid.class, "Void Waterproof Pipe", pipeWaterproof, pipeItemsVoid);

			pipePowerWood = buildPipe(DefaultProps.PIPE_POWER_WOOD_ID, PipePowerWood.class, "Wooden Kinesis Pipe", Item.redstone, pipeItemsWood);
			pipePowerCobblestone = buildPipe(DefaultProps.PIPE_POWER_COBBLESTONE_ID, PipePowerCobblestone.class, "Cobblestone Kinesis Pipe", Item.redstone, pipeItemsCobblestone);
			pipePowerStone = buildPipe(DefaultProps.PIPE_POWER_STONE_ID, PipePowerStone.class, "Stone Kinesis Pipe", Item.redstone, pipeItemsStone);
			pipePowerQuartz = buildPipe(DefaultProps.PIPE_POWER_QUARTZ_ID, PipePowerQuartz.class, "Quartz Kinesis Pipe", Item.redstone, pipeItemsQuartz);
			pipePowerIron = buildPipe(DefaultProps.PIPE_POWER_IRON_ID, PipePowerIron.class, "Iron Kinesis Pipe", Item.redstone, pipeItemsIron);
			pipePowerGold = buildPipe(DefaultProps.PIPE_POWER_GOLD_ID, PipePowerGold.class, "Golden Kinesis Pipe", Item.redstone, pipeItemsGold);
			pipePowerDiamond = buildPipe(DefaultProps.PIPE_POWER_DIAMOND_ID, PipePowerDiamond.class, "Diamond Kinesis Pipe", Item.redstone, pipeItemsDiamond);

			pipeStructureCobblestone = buildPipe(DefaultProps.PIPE_STRUCTURE_COBBLESTONE_ID, PipeStructureCobblestone.class, "Cobblestone Structure Pipe", Block.gravel, pipeItemsCobblestone);

			pipeWire = new ItemPipeWire(BuildcraftConfig.pipeWireItemId);
			CoreProxy.getProxy().registerItem(pipeWire);
			PipeWire.item = pipeWire;

			pipeGate = new ItemGate(BuildcraftConfig.pipeGateItemId);
			pipeGate.setUnlocalizedName("pipeGate");
			CoreProxy.getProxy().registerItem(pipeGate);

			facadeItem = new ItemFacade(BuildcraftConfig.pipeFacadeItemId);
			facadeItem.setUnlocalizedName("pipeFacade");
			CoreProxy.getProxy().registerItem(facadeItem);

			plugItem = new ItemPlug(BuildcraftConfig.pipePlugItemId);
			plugItem.setUnlocalizedName("pipePlug");
			CoreProxy.getProxy().registerItem(plugItem);

		}
		catch (Throwable e){
			e.printStackTrace();
			System.out.println("idk what happened but something broke");
		}

		TransportProxy.getProxy().registerTileEntities();

		new BptBlockPipe(genericPipeBlock.blockID);

		BuildCraftCore.itemBptProps[pipeItemsWood.itemID] = new BptItemPipeWooden();
		BuildCraftCore.itemBptProps[pipeFluidsWood.itemID] = new BptItemPipeWooden();
		BuildCraftCore.itemBptProps[pipeItemsIron.itemID] = new BptItemPipeIron();
		BuildCraftCore.itemBptProps[pipeFluidsIron.itemID] = new BptItemPipeIron();
		BuildCraftCore.itemBptProps[pipeItemsDiamond.itemID] = new BptItemPipeDiamond();
		BuildCraftCore.itemBptProps[pipeItemsEmerald.itemID] = new BptItemPipeEmerald();

		ActionManager.registerTriggerProvider(new PipeTriggerProvider());


		if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT)){
			TransportProxyClient.PROXY_CLIENT.registerRenderers();
		}
		else {
			TransportProxy.proxy.registerRenderers();
		}
		NetworkRegistry.instance().registerGuiHandler("bctransport", new GuiHandler());
	}

	@Override
	public void initRecipes() {
		loadRecipes();
	}

	@Override
	public void postInit() {
		ItemFacade.initialize();

		for (PipeContents kind : PipeContents.values()) {
			triggerPipe[kind.ordinal()] = new TriggerPipeContents(kind);
		}

		for (PipeWire wire : PipeWire.values()) {
			triggerPipeWireActive[wire.ordinal()] = new TriggerPipeSignal(true, wire);
			triggerPipeWireInactive[wire.ordinal()] = new TriggerPipeSignal(false, wire);
			actionPipeWire[wire.ordinal()] = new ActionSignalOutput(wire);
		}

		for (Time time : Time.VALUES) {
			triggerTimer[time.ordinal()] = new TriggerClockTimer(time);
		}

		for (int level = 0; level < triggerRedstoneLevel.length; level++) {
			triggerRedstoneLevel[level] = new TriggerRedstoneFaderInput(level + 1);
			actionRedstoneLevel[level] = new ActionRedstoneFaderOutput(level + 1);
		}

		for (EnumColor color : EnumColor.VALUES) {
			actionPipeColor[color.ordinal()] = new ActionPipeColor(color);
		}

		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
			actionPipeDirection[direction.ordinal()] = new ActionPipeDirection(direction);
		}

		for (PowerMode limit : PowerMode.VALUES) {
			actionPowerLimiter[limit.ordinal()] = new ActionPowerLimiter(limit);
		}
	}

	@Override
	public String getModId() {
		return "bctransport";
	}

	public void loadRecipes() {

		// Add base recipe for pipe waterproof.
		RecipeManager.addShapelessRecipe(new ItemStack(pipeWaterproof, 1), new ItemStack[] {
				new ItemStack(Item.dyePowder, 1, Color.GREEN.colorID)
		});

		// Add pipe recipes
		for (PipeRecipe pipe : pipeRecipes) {
			if (pipe.isShapeless) {
				CoreProxy.getProxy().addShapelessRecipe(pipe.result, pipe.input);
			} else {
				CoreProxy.getProxy().addCraftingRecipe(pipe.result, pipe.input);
			}
		}

		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(filteredBufferBlock),
				"wdw",
				"wcw",
				"wpw",
				'w', Block.planks,
				'd', BuildCraftTransport.pipeItemsDiamond,
				'c', Block.chest,
				'p', Block.pistonBase);

		//Facade turning helper
		CraftingManager.getInstance().getRecipes().add(facadeItem.new FacadeRecipe());

		BuildcraftRecipes.assemblyTable.addRecipe(1000, new ItemStack(plugItem, 8), new ItemStack(pipeStructureCobblestone));
	}

	public static Item buildPipe(int defaultID, Class<? extends Pipe> clas, String descr, Object... ingredients) {
		String name = Character.toLowerCase(clas.getSimpleName().charAt(0)) + clas.getSimpleName().substring(1);

/*		Property prop = BuildCraftCore.mainConfiguration.getItem(name + ".id", defaultID);

		int id = prop.getInt(defaultID);*/
		ItemPipe res = BlockGenericPipe.registerPipe(defaultID, clas);
		res.setUnlocalizedName(clas.getSimpleName());
		/*LanguageRegistry.addName(res, descr);*/

		// Add appropriate recipe to temporary list
		PipeRecipe recipe = new PipeRecipe();

		if (ingredients.length == 3) {
			recipe.result = new ItemStack(res, 8);
			recipe.input = new Object[]{"ABC", 'A', ingredients[0], 'B', ingredients[1], 'C', ingredients[2]};

			pipeRecipes.add(recipe);
		} else if (ingredients.length == 2) {
			recipe.isShapeless = true;
			recipe.result = new ItemStack(res, 1);
			recipe.input = new Object[]{ingredients[0], ingredients[1]};

			pipeRecipes.add(recipe);

			if (ingredients[1] instanceof ItemPipe) {
				PipeRecipe uncraft = new PipeRecipe();
				uncraft.isShapeless = true;
				uncraft.input = new Object[]{new ItemStack(res)};
				uncraft.result = new ItemStack((Item) ingredients[1]);
				pipeRecipes.add(uncraft);
			}
		}

		return res;
	}

	private record ExtractionHandler(String[] items, String[] liquids) implements IExtractionHandler {

		@Override
			public boolean canExtractItems(Object extractor, World world, int i, int j, int k) {
				return testStrings(items, world, i, j, k);
			}

			@Override
			public boolean canExtractFluids(Object extractor, World world, int i, int j, int k) {
				return testStrings(liquids, world, i, j, k);
			}

			private boolean testStrings(String[] excludedBlocks, World world, int i, int j, int k) {
				int id = world.getBlockId(i, j, k);
				Block block = Block.blocksList[id];
				if (block == null)
					return false;

				int meta = world.getBlockMetadata(i, j, k);

				for (String excluded : excludedBlocks) {
					if (excluded.equals(block.getUnlocalizedName()))
						return false;

					String[] tokens = excluded.split(":");
					if (tokens[0].equals(Integer.toString(id)) && (tokens.length == 1 || tokens[1].equals(Integer.toString(meta))))
						return false;
				}
				return true;
			}
		}
}
