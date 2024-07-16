/**
 * BuildCraft is open-source. It is distributed under the terms of the
 * BuildCraft Open Source License. It grants rights to read, modify, compile or
 * run the code. It does *NOT* grant the right to redistribute this software or
 * its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */
package buildcraft;
import buildcraft.core.DefaultProps;
import buildcraft.core.proxy.CoreProxy;
import buildcraft.factory.*;

import buildcraft.factory.network.PacketHandlerFactory;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.TextureMap;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class BuildCraftFactory implements IBuildCraftModule {
	public static final int MINING_MJ_COST_PER_BLOCK = 64;
	public static BlockQuarry quarryBlock;
	public static BlockMiningWell miningWellBlock;
	public static BlockAutoWorkbench autoWorkbenchBlock;
	public static BlockFrame frameBlock;
	public static BlockPlainPipe plainPipeBlock;
	public static BlockPump pumpBlock;
	public static BlockFloodGate floodGateBlock;
	public static BlockTank tankBlock;
	public static BlockRefinery refineryBlock;
	public static BlockHopper hopperBlock;
	public static boolean allowMining = true;
	public static boolean quarryOneTimeUse = false;
	public static float miningMultiplier = 1;
	public static int miningDepth = 256;
	public static PumpDimensionList pumpDimensionList;

	public static BuildCraftFactory INSTANCE = new BuildCraftFactory();


/*	public class QuarryChunkloadCallback implements ForgeChunkManager.OrderedLoadingCallback {

		@Override
		public void ticketsLoaded(List<Ticket> tickets, World world) {
			for (Ticket ticket : tickets) {
				int quarryX = ticket.getModData().getInteger("quarryX");
				int quarryY = ticket.getModData().getInteger("quarryY");
				int quarryZ = ticket.getModData().getInteger("quarryZ");
				TileQuarry tq = (TileQuarry) world.getBlockTileEntity(quarryX, quarryY, quarryZ);
				tq.forceChunkLoading(ticket);

			}
		}

		@Override
		public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world, int maxTicketCount) {
			List<Ticket> validTickets = Lists.newArrayList();
			for (Ticket ticket : tickets) {
				int quarryX = ticket.getModData().getInteger("quarryX");
				int quarryY = ticket.getModData().getInteger("quarryY");
				int quarryZ = ticket.getModData().getInteger("quarryZ");

				int blId = world.getBlockId(quarryX, quarryY, quarryZ);
				if (blId == quarryBlock.blockID) {
					validTickets.add(ticket);
				}
			}
			return validTickets;
		}
	}*/

	@Override
	public void registerConfigForSettings(BuildCraftAddon addon) {
		addon.registerProp("AllowMiningMachines", true, "Factory\n\n# Set to false to disable the crafting recipes for mining machines. Default: true (Boolean)");
		addon.registerProp("QuarryOneTimeUse", false, "Set to true to disable the ability to pick up quarries after placement. Default: false (Boolean)");
		addon.registerProp("QuarryCostMultiplier", 1.0, "Cost multiplier for mining operations.\n# High values may render engines incapable of powering machines directly. Range (1.0 - 10.0), Default: 1.0 (Float)");
		addon.registerProp("MiningDepth", 256, "Set to true to disable the ability to pick up quarries after placement. Range (2 - 256), Default: 256 (Integer)");

		String pumpListComment = "Allows admins to whitelist or blacklist pumping of specific fluids in specific dimensions.\n"
				+ "# E.g. \"-/-1/Lava\" will disable lava in the nether. \"-/*/Lava\" will disable lava in any dimension. \"+/0/*\" will enable any fluid in the overworld.\n"
				+ "# Entries are comma seperated, banned fluids have precedence over allowed ones."
				+ "Default is \"+/*/*,+/-1/Lava\" - the second redundant entry (\"+/-1/lava\") is there to show the format.";
		addon.registerProp("PumpList", DefaultProps.PUMP_DIMENSION_LIST, pumpListComment);
	}

	@Override
	public void registerConfigForIds(BuildCraftAddon addon) {
		addon.registerProp("MiningWellBlockId", DefaultProps.MINING_WELL_ID, "Factory\n");
		addon.registerProp("PlainPipeBlockId", DefaultProps.DRILL_ID);
		addon.registerProp("AutoWorkbenchBlockId", DefaultProps.AUTO_WORKBENCH_ID);
		addon.registerProp("FrameBlockId", DefaultProps.FRAME_ID);
		addon.registerProp("QuarryBlockId", DefaultProps.QUARRY_ID);
		addon.registerProp("FloodGateBlockId", DefaultProps.FLOOD_GATE_ID);
		addon.registerProp("TankBlockId", DefaultProps.TANK_ID);
		addon.registerProp("RefineryBlockId", DefaultProps.REFINERY_ID);
		addon.registerProp("HopperBlockId", DefaultProps.HOPPER_ID);
	}

	@Override
	public void handleConfigProps() {
		allowMining = BuildcraftConfig.getBoolean("AllowMiningMachines");
		quarryOneTimeUse = BuildcraftConfig.getBoolean("QuarryOneTimeUse");
		miningMultiplier = BuildcraftConfig.getClampedFloat("QuarryCostMultiplier", 1f, 10f);
		miningDepth = BuildcraftConfig.getClampedInt("MiningDepth", 2, 256);

		pumpDimensionList = new PumpDimensionList(BuildcraftConfig.getString("PumpList"));

		BuildcraftConfig.miningWellId = BuildcraftConfig.getInt("MiningWellBlockId");
		BuildcraftConfig.plainPipeId = BuildcraftConfig.getInt("PlainPipeBlockId");
		BuildcraftConfig.autoWorkbenchId = BuildcraftConfig.getInt("AutoWorkbenchBlockId");
		BuildcraftConfig.frameId = BuildcraftConfig.getInt("FrameBlockId");
		BuildcraftConfig.quarryId = BuildcraftConfig.getInt("QuarryBlockId");
		BuildcraftConfig.floodGateId = BuildcraftConfig.getInt("FloodGateBlockId");
		BuildcraftConfig.tankId = BuildcraftConfig.getInt("TankBlockId");
		BuildcraftConfig.refineryId = BuildcraftConfig.getInt("RefineryBlockId");
		BuildcraftConfig.hopperId = BuildcraftConfig.getInt("HopperBlockId");
	}

	@Override
	public void init() {
		if (BuildcraftConfig.miningWellId > 0) {
			miningWellBlock = new BlockMiningWell(BuildcraftConfig.miningWellId);
			CoreProxy.getProxy().registerBlock(miningWellBlock.setUnlocalizedName("miningWellBlock"));
			CoreProxy.getProxy().addName(miningWellBlock, "Mining Well");
		}
		if (BuildcraftConfig.plainPipeId > 0) {
			plainPipeBlock = new BlockPlainPipe(BuildcraftConfig.plainPipeId);
			CoreProxy.getProxy().registerBlock(plainPipeBlock.setUnlocalizedName("plainPipeBlock"));
			CoreProxy.getProxy().addName(plainPipeBlock, "Mining Pipe");
		}
		if (BuildcraftConfig.autoWorkbenchId > 0) {
			autoWorkbenchBlock = new BlockAutoWorkbench(BuildcraftConfig.autoWorkbenchId);
			CoreProxy.getProxy().registerBlock(autoWorkbenchBlock.setUnlocalizedName("autoWorkbenchBlock"));
			CoreProxy.getProxy().addName(autoWorkbenchBlock, "Automatic Crafting Table");
		}
		if (BuildcraftConfig.frameId > 0) {
			frameBlock = new BlockFrame(BuildcraftConfig.frameId);
			CoreProxy.getProxy().registerBlock(frameBlock.setUnlocalizedName("frameBlock"));
			CoreProxy.getProxy().addName(frameBlock, "Frame");
		}
		if (BuildcraftConfig.quarryId > 0) {
			quarryBlock = new BlockQuarry(BuildcraftConfig.quarryId);
			CoreProxy.getProxy().registerBlock(quarryBlock.setUnlocalizedName("machineBlock"));
			CoreProxy.getProxy().addName(quarryBlock, "Quarry");
		}
		if (BuildcraftConfig.tankId > 0) {
			tankBlock = new BlockTank(BuildcraftConfig.tankId);
			tankBlock.setUnlocalizedName("tankBlock");
			System.out.println("tank " + tankBlock);
		}
		if (BuildcraftConfig.pumpId > 0) {
			pumpBlock = new BlockPump(BuildcraftConfig.pumpId);
			CoreProxy.getProxy().registerBlock(pumpBlock.setUnlocalizedName("pumpBlock"));
			CoreProxy.getProxy().addName(pumpBlock, "Pump");
		}
		if (BuildcraftConfig.floodGateId > 0) {
			floodGateBlock = new BlockFloodGate(BuildcraftConfig.floodGateId);
			CoreProxy.getProxy().registerBlock(floodGateBlock.setUnlocalizedName("floodGateBlock"));
			CoreProxy.getProxy().addName(floodGateBlock, "Flood Gate");
		}
		if (BuildcraftConfig.refineryId > 0) {
			refineryBlock = new BlockRefinery(BuildcraftConfig.refineryId);
			CoreProxy.getProxy().registerBlock(refineryBlock.setUnlocalizedName("refineryBlock"));
			CoreProxy.getProxy().addName(refineryBlock, "Refinery");
		}
		if (BuildcraftConfig.hopperId > 0) {
			hopperBlock = new BlockHopper(BuildcraftConfig.hopperId);
			CoreProxy.getProxy().registerBlock(hopperBlock.setUnlocalizedName("blockHopper"));
			CoreProxy.getProxy().addName(hopperBlock, "Hopper");
		}

		FactoryProxy.getProxy().initializeEntityRenders();
        NetworkRegistry.instance().registerGuiHandler("bcfactory", new GuiHandlerFactory());

		CoreProxy.getProxy().registerTileEntity(TileQuarry.class, "Machine");
		CoreProxy.getProxy().registerTileEntity(TileMiningWell.class, "MiningWell");
		CoreProxy.getProxy().registerTileEntity(TileAutoWorkbench.class, "AutoWorkbench");
		CoreProxy.getProxy().registerTileEntity(TilePump.class, "net.minecraft.src.buildcraft.factory.TilePump");
		CoreProxy.getProxy().registerTileEntity(TileFloodGate.class, "net.minecraft.src.buildcraft.factory.TileFloodGate");
		CoreProxy.getProxy().registerTileEntity(TileTank.class, "net.minecraft.src.buildcraft.factory.TileTank");
		CoreProxy.getProxy().registerTileEntity(TileRefinery.class, "net.minecraft.src.buildcraft.factory.Refinery");
		CoreProxy.getProxy().registerTileEntity(TileHopper.class, "net.minecraft.src.buildcraft.factory.TileHopper");

		FactoryProxy.getProxy().initializeTileEntities();

		new BptBlockAutoWorkbench(autoWorkbenchBlock.blockID);
		new BptBlockFrame(frameBlock.blockID);
		new BptBlockRefinery(refineryBlock.blockID);
		new BptBlockTank(tankBlock.blockID);

		BuildCraftAddon.registerBCPacketHandler(DefaultProps.FACTORY_CHANNEL_NAME, new PacketHandlerFactory());
	}

	@Override
	public void initRecipes() {
		loadRecipes();
	}

	@Override
	public void postInit() {
//		FactoryProxy.getProxy().initializeNEIIntegration();

		/*ForgeChunkManager.setForcedChunkLoadingCallback(instance, new QuarryChunkloadCallback());*/
	}

	@Override
	public String getModId() {
		return "bcfactory";
	}

	public static void loadRecipes() {

		if (allowMining) {
			if (miningWellBlock != null)
				CoreProxy.getProxy().addCraftingRecipe(new ItemStack(miningWellBlock, 1),
						"ipi",
						"igi",
						"iPi",
						'p', Item.redstone,
						'i', Item.ingotIron,
						'g', BuildCraftCore.ironGearItem,
						'P', Item.pickaxeIron);

			if (quarryBlock != null)
				CoreProxy.getProxy().addCraftingRecipe(
						new ItemStack(quarryBlock),
						"ipi",
						"gig",
						"dDd",
						'i', BuildCraftCore.ironGearItem,
						'p', Item.redstone,
						'g', BuildCraftCore.goldGearItem,
						'd', BuildCraftCore.diamondGearItem,
						'D', Item.pickaxeDiamond);

			if (pumpBlock != null && miningWellBlock != null)
				CoreProxy.getProxy().addCraftingRecipe(new ItemStack(pumpBlock),
						"T",
						"W",
						'T', tankBlock != null ? tankBlock : Block.glass,
						'W', miningWellBlock);
		}

		if (!allowMining || miningWellBlock == null) {
			if (pumpBlock != null)
				CoreProxy.getProxy().addCraftingRecipe(new ItemStack(pumpBlock),
						"iri",
						"iTi",
						"gpg",
						'r', Item.redstone,
						'i', Item.ingotIron,
						'T', tankBlock != null ? tankBlock : Block.glass,
						'g', BuildCraftCore.ironGearItem,
						'p', BuildCraftTransport.pipeFluidsGold);
		}

		if (autoWorkbenchBlock != null)
			CoreProxy.getProxy().addCraftingRecipe(new ItemStack(autoWorkbenchBlock),
					" g ",
					"gwg",
					" g ",
					'w', Block.workbench,
					'g', BuildCraftCore.woodenGearItem);


		if (tankBlock != null)
			CoreProxy.getProxy().addCraftingRecipe(new ItemStack(tankBlock),
					"ggg",
					"g g",
					"ggg",
					'g', Block.glass);

		if (refineryBlock != null)
			CoreProxy.getProxy().addCraftingRecipe(new ItemStack(refineryBlock),
					"RTR",
					"TGT",
					'T', tankBlock != null ? tankBlock : Block.glass,
					'G', BuildCraftCore.diamondGearItem,
					'R', Block.torchRedstoneActive);

		if (hopperBlock != null)
			CoreProxy.getProxy().addCraftingRecipe(new ItemStack(hopperBlock),
					"ICI",
					"IGI",
					" I ",
					'I', Item.ingotIron,
					'C', Block.chest,
					'G', BuildCraftCore.stoneGearItem);

		if (floodGateBlock != null)
			CoreProxy.getProxy().addCraftingRecipe(new ItemStack(floodGateBlock),
					"IGI",
					"FTF",
					"IFI",
					'I', Item.ingotIron,
					'T', tankBlock != null ? tankBlock : Block.glass,
					'G', BuildCraftCore.ironGearItem,
					'F', new ItemStack(Block.fenceIron));
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void textureHook(TextureMap map) {
        if (map.getTextureType() == 0) {
            TextureMap terrainTextures = map;
            FactoryProxyClient.pumpTexture = terrainTextures.registerIcon("buildcraft:pump_tube");
            FactoryProxyClient.drillTexture = terrainTextures.registerIcon("buildcraft:blockDrillTexture");
            FactoryProxyClient.drillHeadTexture = terrainTextures.registerIcon("buildcraft:blockDrillHeadTexture");
        }
    }
}
