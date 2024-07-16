/**
 * Copyright (c) SpaceToad, 2011-2012 http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License
 * 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft;

import buildcraft.api.blueprints.BptBlock;
import buildcraft.api.bptblocks.*;
import buildcraft.api.filler.FillerManager;
import buildcraft.api.filler.IFillerPattern;
import buildcraft.api.gates.ActionManager;
import buildcraft.builders.*;
import buildcraft.builders.filler.FillerRegistry;
import buildcraft.builders.filler.pattern.*;
import buildcraft.builders.network.PacketHandlerBuilders;
import buildcraft.builders.triggers.ActionFiller;
import buildcraft.builders.triggers.BuildersActionProvider;
import buildcraft.core.DefaultProps;
import buildcraft.core.blueprints.BptPlayerIndex;
import buildcraft.core.blueprints.BptRootIndex;
import buildcraft.core.proxy.CoreProxy;
import buildcraft.core.utils.BCLog;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.src.*;


import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeMap;

public class BuildCraftBuilders implements IBuildCraftModule {

	public static final int LIBRARY_PAGE_SIZE = 12;
	public static final int MAX_BLUEPRINTS_NAME_SIZE = 14;
	public static BlockMarker markerBlock;
	public static BlockPathMarker pathMarkerBlock;
	public static BlockFiller fillerBlock;
	public static BlockBuilder builderBlock;
	public static BlockArchitect architectBlock;
	public static BlockBlueprintLibrary libraryBlock;
	public static ItemBptTemplate templateItem;
	public static ItemBptBluePrint blueprintItem;
	public static boolean fillerDestroy;
	public static int fillerLifespanTough;
	public static int fillerLifespanNormal;
	public static ActionFiller[] fillerActions;
	private static BptRootIndex rootBptIndex;
	public static TreeMap<String, BptPlayerIndex> playerLibrary = new TreeMap<String, BptPlayerIndex>();
	private static LinkedList<IBuilderHook> hooks = new LinkedList<>();

	public static BuildCraftBuilders INSTANCE = new BuildCraftBuilders();

    @Override
	public void init() {
		// Register gui handler
        NetworkRegistry.instance().registerGuiHandler(getModId(), new GuiHandlerBuilders());

		templateItem = new ItemBptTemplate(BuildcraftConfig.templateItemId);
		templateItem.setUnlocalizedName("templateItem");
		CoreProxy.getProxy().registerItem(templateItem);

		blueprintItem = new ItemBptBluePrint(BuildcraftConfig.blueprintItemId);
		blueprintItem.setUnlocalizedName("blueprintItem");
		CoreProxy.getProxy().registerItem(blueprintItem);

		markerBlock = new BlockMarker(BuildcraftConfig.markerBlockId);
		CoreProxy.getProxy().registerBlock(markerBlock.setUnlocalizedName("markerBlock"));
		CoreProxy.getProxy().addName(markerBlock, "Land Mark");

		pathMarkerBlock = new BlockPathMarker(BuildcraftConfig.pathMarkerBlockId);
		CoreProxy.getProxy().registerBlock(pathMarkerBlock.setUnlocalizedName("pathMarkerBlock"));
		CoreProxy.getProxy().addName(pathMarkerBlock, "Path Mark");

		fillerBlock = new BlockFiller(BuildcraftConfig.fillerBlockId);
		CoreProxy.getProxy().registerBlock(fillerBlock.setUnlocalizedName("fillerBlock"));
		CoreProxy.getProxy().addName(fillerBlock, "Filler");

		builderBlock = new BlockBuilder(BuildcraftConfig.builderBlockId);
		CoreProxy.getProxy().registerBlock(builderBlock.setUnlocalizedName("builderBlock"));
		CoreProxy.getProxy().addName(builderBlock, "Builder");

		architectBlock = new BlockArchitect(BuildcraftConfig.architectBlockId);
		CoreProxy.getProxy().registerBlock(architectBlock.setUnlocalizedName("architectBlock"));
		CoreProxy.getProxy().addName(architectBlock, "Architect Table");

		libraryBlock = new BlockBlueprintLibrary(BuildcraftConfig.blueprintLibraryBlockId);
		CoreProxy.getProxy().registerBlock(libraryBlock.setUnlocalizedName("libraryBlock"));
		CoreProxy.getProxy().addName(libraryBlock, "Blueprint Library");


		// Create filler registry
		try {
			FillerManager.registry = new FillerRegistry();

			// INIT FILLER PATTERNS
			FillerManager.registry.addPattern(PatternFill.INSTANCE);
			FillerManager.registry.addPattern(new PatternFlatten());
			FillerManager.registry.addPattern(new PatternHorizon());
			FillerManager.registry.addPattern(new PatternClear());
			FillerManager.registry.addPattern(new PatternBox());
			FillerManager.registry.addPattern(new PatternPyramid());
			FillerManager.registry.addPattern(new PatternStairs());
			FillerManager.registry.addPattern(new PatternCylinder());
		} catch (Error error) {
			BCLog.logErrorAPI("Buildcraft", error, IFillerPattern.class);
			throw error;
		}

		BuildCraftAddon.registerBCPacketHandler(DefaultProps.BUILDERS_CHANNEL_NAME, new PacketHandlerBuilders());

		ActionManager.registerActionProvider(new BuildersActionProvider());

		// Register save handler

		new BptBlock(0); // default bpt block

		new BptBlockIgnore(Block.snow.blockID);
		new BptBlockIgnore(Block.tallGrass.blockID);
		new BptBlockIgnore(Block.ice.blockID);
		new BptBlockIgnore(Block.pistonExtension.blockID);

		new BptBlockDirt(Block.dirt.blockID);
		new BptBlockDirt(Block.grass.blockID);
		new BptBlockDirt(Block.tilledField.blockID);

		new BptBlockDelegate(Block.torchRedstoneIdle.blockID, Block.torchRedstoneActive.blockID);
		new BptBlockDelegate(Block.furnaceBurning.blockID, Block.furnaceIdle.blockID);
		new BptBlockDelegate(Block.pistonMoving.blockID, Block.pistonBase.blockID);

		new BptBlockWallSide(Block.torchWood.blockID);
		new BptBlockWallSide(Block.torchRedstoneActive.blockID);

		new BptBlockRotateMeta(Block.ladder.blockID, new int[]{2, 5, 3, 4}, true);
		new BptBlockRotateMeta(Block.fenceGate.blockID, new int[]{0, 1, 2, 3}, true);

		new BptBlockRotateInventory(Block.furnaceIdle.blockID, new int[]{2, 5, 3, 4}, true);
		new BptBlockRotateInventory(Block.chest.blockID, new int[]{2, 5, 3, 4}, true);
		new BptBlockRotateInventory(Block.lockedChest.blockID, new int[]{2, 5, 3, 4}, true);
		new BptBlockRotateInventory(Block.dispenser.blockID, new int[]{2, 5, 3, 4}, true);

		new BptBlockInventory(Block.brewingStand.blockID);

		new BptBlockRotateMeta(Block.vine.blockID, new int[]{1, 4, 8, 2}, false);
		new BptBlockRotateMeta(Block.trapdoor.blockID, new int[]{0, 1, 2, 3}, false);

		new BptBlockLever(Block.woodenButton.blockID);
		new BptBlockLever(Block.stoneButton.blockID);
		new BptBlockLever(Block.lever.blockID);

		new BptBlockCustomStack(Block.stone.blockID, new ItemStack(Block.stone));
		new BptBlockCustomStack(Block.redstoneWire.blockID, new ItemStack(Item.redstone));
		// FIXME: Not sure what this has become
		// new BptBlockCustomStack(Block.stairDouble.blockID, new ItemStack(Block.stairSingle, 2));
		new BptBlockCustomStack(Block.cake.blockID, new ItemStack(Item.cake));
		new BptBlockCustomStack(Block.crops.blockID, new ItemStack(Item.seeds));
		new BptBlockCustomStack(Block.pumpkinStem.blockID, new ItemStack(Item.pumpkinSeeds));
		new BptBlockCustomStack(Block.melonStem.blockID, new ItemStack(Item.melonSeeds));
		new BptBlockCustomStack(Block.glowStone.blockID, new ItemStack(Block.glowStone));

		new BptBlockRedstoneRepeater(Block.redstoneRepeaterActive.blockID);
		new BptBlockRedstoneRepeater(Block.redstoneRepeaterIdle.blockID);

		new BptBlockFluid(Block.waterStill.blockID, new ItemStack(Item.bucketWater));
		new BptBlockFluid(Block.waterMoving.blockID, new ItemStack(Item.bucketWater));
		new BptBlockFluid(Block.lavaStill.blockID, new ItemStack(Item.bucketLava));
		new BptBlockFluid(Block.lavaMoving.blockID, new ItemStack(Item.bucketLava));

		new BptBlockIgnoreMeta(Block.rail.blockID);
		new BptBlockIgnoreMeta(Block.railPowered.blockID);
		new BptBlockIgnoreMeta(Block.railDetector.blockID);
		new BptBlockIgnoreMeta(Block.thinGlass.blockID);

		new BptBlockPiston(Block.pistonBase.blockID);
		new BptBlockPiston(Block.pistonStickyBase.blockID);

		new BptBlockPumpkin(Block.pumpkinLantern.blockID);

		new BptBlockStairs(Block.stairsCobblestone.blockID);
		new BptBlockStairs(Block.stairsWoodOak.blockID);
		new BptBlockStairs(Block.stairsNetherBrick.blockID);
		new BptBlockStairs(Block.stairsBrick.blockID);
		new BptBlockStairs(Block.stairsStoneBrick.blockID);

		new BptBlockDoor(Block.doorWood.blockID, new ItemStack(Item.doorWood));
		new BptBlockDoor(Block.doorIron.blockID, new ItemStack(Item.doorIron));

		new BptBlockBed(Block.bed.blockID);

		new BptBlockSign(Block.signWall.blockID, true);
		new BptBlockSign(Block.signPost.blockID, false);

		// BUILDCRAFT BLOCKS

		new BptBlockRotateInventory(architectBlock.blockID, new int[]{2, 5, 3, 4}, true);
		new BptBlockRotateInventory(builderBlock.blockID, new int[]{2, 5, 3, 4}, true);

		new BptBlockInventory(libraryBlock.blockID);

		new BptBlockWallSide(markerBlock.blockID);
		new BptBlockWallSide(pathMarkerBlock.blockID);


        TileEntity.addMapping(TileMarker.class, "Marker");
        TileEntity.addMapping(TileFiller.class, "Filler");
        TileEntity.addMapping(TileBuilder.class, "net.minecraft.src.builders.TileBuilder");
        TileEntity.addMapping(TileArchitect.class, "net.minecraft.src.builders.TileTemplate");
        TileEntity.addMapping(TilePathMarker.class, "net.minecraft.src.builders.TilePathMarker");
        TileEntity.addMapping(TileBlueprintLibrary.class, "net.minecraft.src.builders.TileBlueprintLibrary");
	}

    @Override
    public void handleConfigProps() {
		fillerDestroy = BuildcraftConfig.getBoolean("FillerDestroysBlocks");
		fillerLifespanTough = BuildcraftConfig.getInt("FillerItemLifespanTough");
		fillerLifespanNormal = BuildcraftConfig.getInt("FillerItemLifespanNormal");

		BuildcraftConfig.templateItemId = BuildcraftConfig.getInt("TemplateItemId");
		BuildcraftConfig.blueprintItemId = BuildcraftConfig.getInt("BlueprintItemId");
		BuildcraftConfig.markerBlockId = BuildcraftConfig.getInt("MarkerBlockId");
		BuildcraftConfig.pathMarkerBlockId = BuildcraftConfig.getInt("PathMarkerBlockId");
		BuildcraftConfig.fillerBlockId = BuildcraftConfig.getInt("FillerBlockId");
		BuildcraftConfig.builderBlockId = BuildcraftConfig.getInt("BuilderBlockId");
		BuildcraftConfig.architectBlockId = BuildcraftConfig.getInt("ArchitectBlockId");
		BuildcraftConfig.blueprintLibraryBlockId = BuildcraftConfig.getInt("BlueprintLibraryBlockId");
	}

    @Override
    public void registerConfigForSettings(BuildCraftAddon addon) {
		addon.registerProp("FillerDestroysBlocks", DefaultProps.FILLER_DESTROY, "Builders\n\n# If true, Filler will destroy blocks instead of breaking them. Default: false (Boolean)");
		addon.registerProp("FillerItemLifespanTough", 20, "Lifespan in ticks of items dropped by the filler from 'tough' blocks (those that can't be broken by hand). Default: 20 (Integer)");
		addon.registerProp("FillerItemLifespanNormal", 6000, "Lifespan in ticks of items dropped by the filler from non-tough blocks (those that can be broken by hand). Default: 6000 (Integer)");
	}

    @Override
    public void registerConfigForIds(BuildCraftAddon addon) {
		addon.registerProp("TemplateItemId", DefaultProps.TEMPLATE_ITEM_ID, "Builders\n");
		addon.registerProp("BlueprintItemId", DefaultProps.BLUEPRINT_ITEM_ID);

		addon.registerProp("MarkerBlockId", DefaultProps.MARKER_ID);
		addon.registerProp("PathMarkerBlockId", DefaultProps.PATH_MARKER_ID);
		addon.registerProp("FillerBlockId", DefaultProps.FILLER_ID);
		addon.registerProp("BuilderBlockId", DefaultProps.BUILDER_ID);
		addon.registerProp("ArchitectBlockId", DefaultProps.ARCHITECT_ID);
		addon.registerProp("BlueprintLibraryBlockId", DefaultProps.BLUEPRINT_LIBRARY_ID);
	}

	public static void loadRecipes() {

//		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(templateItem, 1), new Object[]{"ppp", "pip", "ppp", 'i',
//			new ItemStack(Item.dyePowder, 1, 0), 'p', Item.paper});

//		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(blueprintItem, 1), new Object[]{"ppp", "pip", "ppp", 'i',
//			new ItemStack(Item.dyePowder, 1, 4), 'p', Item.paper});

		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(markerBlock, 1), new Object[]{"l ", "r ", 'l',
			new ItemStack(Item.dyePowder, 1, 4), 'r', Block.torchRedstoneActive});

//		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(pathMarkerBlock, 1), new Object[]{"l ", "r ", 'l',
//			new ItemStack(Item.dyePowder, 1, 2), 'r', Block.torchRedstoneActive});

		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(fillerBlock, 1), new Object[]{"btb", "ycy", "gCg", 'b',
			new ItemStack(Item.dyePowder, 1, 0), 't', markerBlock, 'y', new ItemStack(Item.dyePowder, 1, 11),
			'c', Block.workbench, 'g', BuildCraftCore.goldGearItem, 'C', Block.chest});

//		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(builderBlock, 1), new Object[]{"btb", "ycy", "gCg", 'b',
//			new ItemStack(Item.dyePowder, 1, 0), 't', markerBlock, 'y', new ItemStack(Item.dyePowder, 1, 11),
//			'c', Block.workbench, 'g', BuildCraftCore.diamondGearItem, 'C', Block.chest});

//		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(architectBlock, 1), new Object[]{"btb", "ycy", "gCg", 'b',
//			new ItemStack(Item.dyePowder, 1, 0), 't', markerBlock, 'y', new ItemStack(Item.dyePowder, 1, 11),
//			'c', Block.workbench, 'g', BuildCraftCore.diamondGearItem, 'C',
//			new ItemStack(templateItem, 1)});

//		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(libraryBlock, 1), new Object[]{"bbb", "bBb", "bbb", 'b',
//			new ItemStack(blueprintItem), 'B', Block.bookShelf});
	}

	public static BptPlayerIndex getPlayerIndex(String name) {
		BptRootIndex rootIndex = getBptRootIndex();

		if (!playerLibrary.containsKey(name)) {
			try {
				playerLibrary.put(name, new BptPlayerIndex(name + ".list", rootIndex));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return playerLibrary.get(name);
	}

	public static BptRootIndex getBptRootIndex() {
		if (rootBptIndex == null) {
			try {
				rootBptIndex = new BptRootIndex("index.txt");
				rootBptIndex.loadIndex();

				for (IBuilderHook hook : hooks) {
					hook.rootIndexInitialized(rootBptIndex);
				}

				rootBptIndex.importNewFiles();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return rootBptIndex;
	}

	public static ItemStack getBptItemStack(int id, int damage, String name) {
		ItemStack stack = new ItemStack(id, 1, damage);
		NBTTagCompound nbt = new NBTTagCompound();
		if (name != null && !"".equals(name)) {
			nbt.setString("BptName", name);
			stack.setTagCompound(nbt);
		}
		return stack;
	}

	public static void addHook(IBuilderHook hook) {
		if (!hooks.contains(hook)) {
			hooks.add(hook);
		}
	}

/*	@EventHandler
	public void ServerStop(FMLServerStoppingEvent event) {
		TilePathMarker.clearAvailableMarkersList();
	}*/

    @Override
    public void textureHook(TextureMap map) {
        if (map.getTextureType() == 0) {
            for (FillerPattern pattern : FillerPattern.patterns) {
                pattern.registerIcon(map);
            }
        }
    }

    @Override
    public String getModId() {
        return "bcbuilders";
    }

    @Override
    public void initRecipes() {
        loadRecipes();
    }
}
