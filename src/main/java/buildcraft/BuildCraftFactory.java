/**
 * BuildCraft is open-source. It is distributed under the terms of the
 * BuildCraft Open Source License. It grants rights to read, modify, compile or
 * run the code. It does *NOT* grant the right to redistribute this software or
 * its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */
package buildcraft;
import buildcraft.core.DefaultProps;
import buildcraft.core.InterModComms;
import buildcraft.core.Version;
import buildcraft.core.proxy.CoreProxy;
import buildcraft.core.utils.ConfigUtils;
import buildcraft.factory.*;
import buildcraft.factory.network.PacketHandlerFactory;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.TextureMap;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import net.minecraftforge.event.ForgeSubscribe;

import java.util.List;
//@Mod(name = "BuildCraft Factory", version = Version.VERSION, useMetadata = false, modid = "BuildCraft|Factory", dependencies = DefaultProps.DEPENDENCY_CORE)
//@NetworkMod(channels = {DefaultProps.NET_CHANNEL_NAME}, packetHandler = PacketHandlerFactory.class, clientSideRequired = true, serverSideRequired = true)
public class BuildCraftFactory extends BuildcraftAddon {
    public BuildCraftFactory() {
        super("bcfactory");
    }

    @Override
    public void initialize() {

    }

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

	public static BuildCraftFactory instance = new BuildCraftFactory();


	public void postInit(FMLPostInitializationEvent evt) {
		FactoryProxy.proxy.initializeNEIIntegration();
		ForgeChunkManager.setForcedChunkLoadingCallback(instance, new QuarryChunkloadCallback());
	}

	public class QuarryChunkloadCallback implements ForgeChunkManager.OrderedLoadingCallback {

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
	}

	@EventHandler
	public void load(FMLInitializationEvent evt) {
		NetworkRegistry.instance().registerGuiHandler(instance.getModId(), new GuiHandler());

		// EntityRegistry.registerModEntity(EntityMechanicalArm.class, "bcMechanicalArm", EntityIds.MECHANICAL_ARM, instance, 50, 1, true);

		CoreProxy.getProxy().registerTileEntity(TileQuarry.class, "Machine");
		CoreProxy.getProxy().registerTileEntity(TileMiningWell.class, "MiningWell");
		CoreProxy.getProxy().registerTileEntity(TileAutoWorkbench.class, "AutoWorkbench");
		CoreProxy.getProxy().registerTileEntity(TilePump.class, "net.minecraft.src.buildcraft.factory.TilePump");
		CoreProxy.getProxy().registerTileEntity(TileFloodGate.class, "net.minecraft.src.buildcraft.factory.TileFloodGate");
		CoreProxy.getProxy().registerTileEntity(TileTank.class, "net.minecraft.src.buildcraft.factory.TileTank");
		CoreProxy.getProxy().registerTileEntity(TileRefinery.class, "net.minecraft.src.buildcraft.factory.Refinery");
		CoreProxy.getProxy().registerTileEntity(TileHopper.class, "net.minecraft.src.buildcraft.factory.TileHopper");

		FactoryProxy.proxy.initializeTileEntities();

		new BptBlockAutoWorkbench(autoWorkbenchBlock.blockID);
		new BptBlockFrame(frameBlock.blockID);
		new BptBlockRefinery(refineryBlock.blockID);
		new BptBlockTank(tankBlock.blockID);

		if (BuildCraftCore.loadDefaultRecipes) {
			loadRecipes();
		}
	}

	@EventHandler
	public void initialize(FMLPreInitializationEvent evt) {
		ConfigUtils genCat = new ConfigUtils(BuildCraftCore.mainConfiguration, Configuration.CATEGORY_GENERAL);

		allowMining = genCat.get("mining.enabled", true, "disables the recipes for automated mining machines");
		quarryOneTimeUse = genCat.get("quarry.one.time.use", false, "Quarry cannot be picked back up after placement");
		miningMultiplier = genCat.get("mining.cost.multipler", 1F, 1F, 10F, "cost multiplier for mining operations, range (1.0 - 10.0)\nhigh values may render engines incapable of powering machines directly");
		miningDepth = genCat.get("mining.depth", 2, 256, 256, "how far below the machine can mining machines dig, range (2 - 256), default 256");

	//todofactory whacky comment

		Property pumpList = BuildCraftCore.mainConfiguration.get(Configuration.CATEGORY_GENERAL, "pumping.controlList", DefaultProps.PUMP_DIMENSION_LIST);
		pumpList.comment = "Allows admins to whitelist or blacklist pumping of specific fluids in specific dimensions.\n"
				+ "Eg. \"-/-1/Lava\" will disable lava in the nether. \"-/*/Lava\" will disable lava in any dimension. \"+/0/*\" will enable any fluid in the overworld.\n"
				+ "Entries are comma seperated, banned fluids have precedence over allowed ones."
				+ "Default is \"+/*/*,+/-1/Lava\" - the second redundant entry (\"+/-1/lava\") is there to show the format.";
		pumpDimensionList = new PumpDimensionList(pumpList.getString());

		int miningWellId = BuildCraftCore.mainConfiguration.getBlock("miningWell.id", DefaultProps.MINING_WELL_ID).getInt(DefaultProps.MINING_WELL_ID);
		int plainPipeId = BuildCraftCore.mainConfiguration.getBlock("drill.id", DefaultProps.DRILL_ID).getInt(DefaultProps.DRILL_ID);
		int autoWorkbenchId = BuildCraftCore.mainConfiguration.getBlock("autoWorkbench.id", DefaultProps.AUTO_WORKBENCH_ID).getInt(DefaultProps.AUTO_WORKBENCH_ID);
		int frameId = BuildCraftCore.mainConfiguration.getBlock("frame.id", DefaultProps.FRAME_ID).getInt(DefaultProps.FRAME_ID);
		int quarryId = BuildCraftCore.mainConfiguration.getBlock("quarry.id", DefaultProps.QUARRY_ID).getInt(DefaultProps.QUARRY_ID);
		int pumpId = BuildCraftCore.mainConfiguration.getBlock("pump.id", DefaultProps.PUMP_ID).getInt(DefaultProps.PUMP_ID);
		int floodGateId = BuildCraftCore.mainConfiguration.getBlock("floodGate.id", DefaultProps.FLOOD_GATE_ID).getInt(DefaultProps.FLOOD_GATE_ID);
		int tankId = BuildCraftCore.mainConfiguration.getBlock("tank.id", DefaultProps.TANK_ID).getInt(DefaultProps.TANK_ID);
		int refineryId = BuildCraftCore.mainConfiguration.getBlock("refinery.id", DefaultProps.REFINERY_ID).getInt(DefaultProps.REFINERY_ID);
		int hopperId = BuildCraftCore.mainConfiguration.getBlock("hopper.id", DefaultProps.HOPPER_ID).getInt(DefaultProps.HOPPER_ID);

		if (BuildCraftCore.mainConfiguration.hasChanged()) {
			BuildCraftCore.mainConfiguration.save();
		}

		if (miningWellId > 0) {
			miningWellBlock = new BlockMiningWell(miningWellId);
			CoreProxy.getProxy().registerBlock(miningWellBlock.setUnlocalizedName("miningWellBlock"));
			CoreProxy.getProxy().addName(miningWellBlock, "Mining Well");
		}
		if (plainPipeId > 0) {
			plainPipeBlock = new BlockPlainPipe(plainPipeId);
			CoreProxy.getProxy().registerBlock(plainPipeBlock.setUnlocalizedName("plainPipeBlock"));
			CoreProxy.getProxy().addName(plainPipeBlock, "Mining Pipe");
		}
		if (autoWorkbenchId > 0) {
			autoWorkbenchBlock = new BlockAutoWorkbench(autoWorkbenchId);
			CoreProxy.getProxy().registerBlock(autoWorkbenchBlock.setUnlocalizedName("autoWorkbenchBlock"));
			CoreProxy.getProxy().addName(autoWorkbenchBlock, "Automatic Crafting Table");
		}
		if (frameId > 0) {
			frameBlock = new BlockFrame(frameId);
			CoreProxy.getProxy().registerBlock(frameBlock.setUnlocalizedName("frameBlock"));
			CoreProxy.getProxy().addName(frameBlock, "Frame");
		}
		if (quarryId > 0) {
			quarryBlock = new BlockQuarry(quarryId);
			CoreProxy.getProxy().registerBlock(quarryBlock.setUnlocalizedName("machineBlock"));
			CoreProxy.getProxy().addName(quarryBlock, "Quarry");
		}
		if (tankId > 0) {
			tankBlock = new BlockTank(tankId);
			CoreProxy.getProxy().registerBlock(tankBlock.setUnlocalizedName("tankBlock"));
			CoreProxy.getProxy().addName(tankBlock, "Tank");
		}
		if (pumpId > 0) {
			pumpBlock = new BlockPump(pumpId);
			CoreProxy.getProxy().registerBlock(pumpBlock.setUnlocalizedName("pumpBlock"));
			CoreProxy.getProxy().addName(pumpBlock, "Pump");
		}
		if (floodGateId > 0) {
			floodGateBlock = new BlockFloodGate(floodGateId);
			CoreProxy.getProxy().registerBlock(floodGateBlock.setUnlocalizedName("floodGateBlock"));
			CoreProxy.getProxy().addName(floodGateBlock, "Flood Gate");
		}
		if (refineryId > 0) {
			refineryBlock = new BlockRefinery(refineryId);
			CoreProxy.getProxy().registerBlock(refineryBlock.setUnlocalizedName("refineryBlock"));
			CoreProxy.getProxy().addName(refineryBlock, "Refinery");
		}
		if (hopperId > 0) {
			hopperBlock = new BlockHopper(hopperId);
			CoreProxy.getProxy().registerBlock(hopperBlock.setUnlocalizedName("blockHopper"));
			CoreProxy.getProxy().addName(hopperBlock, "Hopper");
		}

		FactoryProxy.proxy.initializeEntityRenders();
		if (BuildCraftCore.mainConfiguration.hasChanged()) {
			BuildCraftCore.mainConfiguration.save();
		}

		MinecraftForge.EVENT_BUS.register(this);
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
	
/*	@EventHandler
    public void processIMCRequests(FMLInterModComms.IMCEvent event) {
        InterModComms.processIMC(event);
    }*/

	@ForgeSubscribe
	@Environment(EnvType.CLIENT)
	public void loadTextures(TextureStitchEvent.Pre evt) {
        if (evt.map.textureType == 0) {
            TextureMap terrainTextures = evt.map;
            FactoryProxyClient.pumpTexture = terrainTextures.registerIcon("buildcraft:pump_tube");
            FactoryProxyClient.drillTexture = terrainTextures.registerIcon("buildcraft:blockDrillTexture");
            FactoryProxyClient.drillHeadTexture = terrainTextures.registerIcon("buildcraft:blockDrillHeadTexture");
        }
    }
}
