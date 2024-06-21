/**
 * BuildCraft is open-source. It is distributed under the terms of the
 * BuildCraft Open Source License. It grants rights to read, modify, compile or
 * run the code. It does *NOT* grant the right to redistribute this software or
 * its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */
package buildcraft;

import btw.BTWAddon;
import btw.crafting.recipe.RecipeManager;
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
import buildcraft.transport.network.PacketGateExpansionMap;
import buildcraft.transport.pipes.*;
import buildcraft.transport.pipes.PipePowerIron.PowerMode;
import buildcraft.transport.triggers.*;
import buildcraft.transport.triggers.TriggerClockTimer.Time;
import buildcraft.transport.triggers.TriggerPipeContents.PipeContents;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.src.*;
import net.minecraftforge.PacketDispatcher;
import net.minecraftforge.common.ForgeDirection;

import java.util.LinkedList;

//@Mod(version = Version.VERSION, modid = "BuildCraft|Transport", name = "Buildcraft Transport", dependencies = DefaultProps.DEPENDENCY_CORE)
//@NetworkMod(channels = {DefaultProps.NET_CHANNEL_NAME}, packetHandler = PacketHandlerTransport.class, connectionHandler = TransportConnectionHandler.class)
public class BuildCraftTransport extends BuildcraftAddon {

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

	public static BuildCraftTransport instance = new BuildCraftTransport();

	public BuildCraftTransport() {
		super("tsp");
	}


	private static class PipeRecipe {

		boolean isShapeless = false; // pipe recipes come shaped and unshaped.
		ItemStack result;
		Object[] input;
	}

	private static class ExtractionHandler implements IExtractionHandler {

		private final String[] items;
		private final String[] liquids;

		public ExtractionHandler(String[] items, String[] liquids) {
			this.items = items;
			this.liquids = liquids;
		}

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
	private static LinkedList<PipeRecipe> pipeRecipes = new LinkedList<PipeRecipe>();

	@Override
	public void preInitialize() {
		//todotransport Config
		try {
		/*	Property durability = BuildCraftCore.mainConfiguration.get(Configuration.CATEGORY_GENERAL, "pipes.durability", DefaultProps.PIPES_DURABILITY);
			durability.comment = "How long a pipe will take to break";
			pipeDurability = (float) durability.getDouble(DefaultProps.PIPES_DURABILITY);*/
			pipeDurability = (float) DefaultProps.PIPES_DURABILITY;

			/*Property exclusionItemList = BuildCraftCore.mainConfiguration.get(Configuration.CATEGORY_BLOCK, "woodenPipe.item.exclusion", new String[0]);

			String[] excludedItemBlocks = exclusionItemList.getStringList();
			if (excludedItemBlocks != null) {
				for (int j = 0; j < excludedItemBlocks.length; ++j) {
					excludedItemBlocks[j] = excludedItemBlocks[j].trim();
				}
			} else*/
			String[] excludedItemBlocks = new String[0];

			/*Property exclusionFluidList = BuildCraftCore.mainConfiguration.get(Configuration.CATEGORY_BLOCK, "woodenPipe.liquid.exclusion", new String[0]);

			String[] excludedFluidBlocks = exclusionFluidList.getStringList();
			if (excludedFluidBlocks != null) {
				for (int j = 0; j < excludedFluidBlocks.length; ++j) {
					excludedFluidBlocks[j] = excludedFluidBlocks[j].trim();
				}
			} else*/
				String[] excludedFluidBlocks = new String[0];

			PipeManager.registerExtractionHandler(new ExtractionHandler(excludedItemBlocks, excludedFluidBlocks));

			GateExpansions.registerExpansion(GateExpansionPulsar.INSTANCE);
			GateExpansions.registerExpansion(GateExpansionTimer.INSTANCE);
			GateExpansions.registerExpansion(GateExpansionRedstoneFader.INSTANCE);

			/*Property groupItemsTriggerProp = BuildCraftCore.mainConfiguration.get(Configuration.CATEGORY_GENERAL, "pipes.groupItemsTrigger", 32);
			groupItemsTriggerProp.comment = "when reaching this amount of objects in a pipes, items will be automatically grouped";
			groupItemsTrigger = groupItemsTriggerProp.getInt();*/
			groupItemsTrigger = 32;




//			Property pipeWaterproofId = BuildCraftCore.mainConfiguration.getItem("pipeWaterproof.id", DefaultProps.PIPE_WATERPROOF_ID);

			pipeWaterproof = new ItemBuildCraft(DefaultProps.PIPE_WATERPROOF_ID);
			pipeWaterproof.setUnlocalizedName("pipeWaterproof");
			CoreProxy.proxy.registerItem(pipeWaterproof);

//			Property genericPipeId = BuildCraftCore.mainConfiguration.getBlock("pipe.id", DefaultProps.GENERIC_PIPE_ID);

			genericPipeBlock = new BlockGenericPipe(DefaultProps.GENERIC_PIPE_ID);
			CoreProxy.proxy.registerBlock(genericPipeBlock.setUnlocalizedName("pipeBlock"), ItemBlock.class);

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
			pipeItemsVoid = buildPipe(DefaultProps.PIPE_ITEMS_VOID_ID, PipeItemsVoid.class, "Void Transport Pipe", new ItemStack(Item.dyePowder, 1, 0), Block.glass, Item.redstone);
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

			// Fix the recipe
			// pipeItemsStipes = createPipe(DefaultProps.PIPE_ITEMS_STRIPES_ID, PipeItemsStripes.class, "Stripes Transport Pipe", new ItemStack(Item.dyePowder,
			// 1, 0), Block.glass, new ItemStack(Item.dyePowder, 1, 11));

//			int pipeWireId = BuildCraftCore.mainConfiguration.get(Configuration.CATEGORY_ITEM, "pipeWire.id", DefaultProps.PIPE_WIRE).getInt(DefaultProps.PIPE_WIRE);

			pipeWire = new ItemPipeWire(DefaultProps.PIPE_WIRE);
			CoreProxy.proxy.registerItem(pipeWire);
			PipeWire.item = pipeWire;

//			Property pipeGateId = BuildCraftCore.mainConfiguration.get(Configuration.CATEGORY_ITEM, "pipeGate.id", DefaultProps.GATE_ID);
			pipeGate = new ItemGate(DefaultProps.GATE_ID);
			pipeGate.setUnlocalizedName("pipeGate");
			CoreProxy.proxy.registerItem(pipeGate);

//			Property pipeFacadeId = BuildCraftCore.mainConfiguration.get(Configuration.CATEGORY_ITEM, "pipeFacade.id", DefaultProps.PIPE_FACADE_ID);
			facadeItem = new ItemFacade(DefaultProps.PIPE_FACADE_ID);
			facadeItem.setUnlocalizedName("pipeFacade");
			CoreProxy.proxy.registerItem(facadeItem);

//			Property pipePlugId = BuildCraftCore.mainConfiguration.get(Configuration.CATEGORY_ITEM, "pipePlug.id", DefaultProps.PIPE_PLUG_ID);
			plugItem = new ItemPlug(DefaultProps.PIPE_PLUG_ID);
			plugItem.setUnlocalizedName("pipePlug");
			CoreProxy.proxy.registerItem(plugItem);

//			Property filteredBufferId = BuildCraftCore.mainConfiguration.getBlock("filteredBuffer.id", DefaultProps.FILTERED_BUFFER_ID);
			filteredBufferBlock = new BlockFilteredBuffer(DefaultProps.FILTERED_BUFFER_ID);
			CoreProxy.proxy.registerBlock(filteredBufferBlock.setUnlocalizedName("filteredBufferBlock"));
			CoreProxy.proxy.addName(filteredBufferBlock, "Filtered Buffer");
		} finally {
//			BuildCraftCore.mainConfiguration.save();
		}
	}

	@Override
	public void initialize() {
		// Register connection handler
		// MinecraftForge.registerConnectionHandler(new ConnectionHandler());

		// Register GUI handler
		// MinecraftForge.setGuiHandler(mod_BuildCraftTransport.instance, new GuiHandler());

		TransportProxy.proxy.registerTileEntities();

		// dockingStationBlock = new
		// BlockDockingStation(Integer.parseInt(dockingStationId.value));
		// ModLoader.registerBlock(dockingStationBlock);
		// CoreProxy.addName(dockingStationBlock.setBlockName("dockingStation"),
		// "Docking Station");

		// ModLoader.RegisterTileEntity(TileDockingStation.class,
		// "net.minecraft.src.buildcraft.TileDockingStation");

		new BptBlockPipe(genericPipeBlock.blockID);

		BuildCraftCore.itemBptProps[pipeItemsWood.itemID] = new BptItemPipeWooden();
		BuildCraftCore.itemBptProps[pipeFluidsWood.itemID] = new BptItemPipeWooden();
		BuildCraftCore.itemBptProps[pipeItemsIron.itemID] = new BptItemPipeIron();
		BuildCraftCore.itemBptProps[pipeFluidsIron.itemID] = new BptItemPipeIron();
		BuildCraftCore.itemBptProps[pipeItemsDiamond.itemID] = new BptItemPipeDiamond();
		BuildCraftCore.itemBptProps[pipeItemsEmerald.itemID] = new BptItemPipeEmerald();

		ActionManager.registerTriggerProvider(new PipeTriggerProvider());

		if (BuildCraftCore.loadDefaultRecipes) {
			loadRecipes();
		}

		TransportProxy.proxy.registerRenderers();
		NetworkRegistry.instance().registerGuiHandler("tsp", new GuiHandler());
	}


	@Override
	public void postInitialize() {
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

	public void loadRecipes() {

		// Add base recipe for pipe waterproof.
		RecipeManager.addShapelessRecipe(new ItemStack(pipeWaterproof, 1), new ItemStack[]{new ItemStack(Item.dyePowder, 1, 2)});

		// Add pipe recipes
		for (PipeRecipe pipe : pipeRecipes) {
			if (pipe.isShapeless) {
				CoreProxy.proxy.addShapelessRecipe(pipe.result, pipe.input);
			} else {
				CoreProxy.proxy.addCraftingRecipe(pipe.result, pipe.input);
			}
		}

		CoreProxy.proxy.addCraftingRecipe(new ItemStack(filteredBufferBlock, 1),
				new Object[]{"wdw", "wcw", "wpw", 'w', Block.planks, 'd',
			BuildCraftTransport.pipeItemsDiamond, 'c', Block.chest, 'p',
			Block.pistonBase});

		//Facade turning helper
		CraftingManager.getInstance().getRecipes().add(facadeItem.new FacadeRecipe());

		BuildcraftRecipes.assemblyTable.addRecipe(1000, new ItemStack(plugItem, 8), new ItemStack(pipeStructureCobblestone));
	}

/*	@EventHandler
	public void processIMCRequests(IMCEvent event) {
		InterModComms.processIMC(event);
	}*/

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

	@Override
	public void serverPlayerConnectionInitialized(NetServerHandler serverHandler, EntityPlayerMP playerMP) {
		PacketGateExpansionMap pkt = new PacketGateExpansionMap();
		PacketDispatcher.sendPacketToPlayer(pkt.getPacket(), playerMP);
	}
}
