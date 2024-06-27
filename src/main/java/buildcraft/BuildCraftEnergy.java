/**
 * BuildCraft is open-source. It is distributed under the terms of the
 * BuildCraft Open Source License. It grants rights to read, modify, compile or
 * run the code. It does *NOT* grant the right to redistribute this software or
 * its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */
package buildcraft;

import buildcraft.api.fuels.IronEngineCoolant;
import buildcraft.api.fuels.IronEngineFuel;
import buildcraft.api.recipes.BuildcraftRecipes;
import buildcraft.core.BlockIndex;
import buildcraft.core.DefaultProps;
import buildcraft.core.fluids.BCFluid;
import buildcraft.core.proxy.CoreProxy;
import buildcraft.core.triggers.BCTrigger;
import buildcraft.energy.*;
import buildcraft.energy.triggers.TriggerEngineHeat;
import buildcraft.energy.worldgen.BiomeGenOilDesert;
import buildcraft.energy.worldgen.BiomeGenOilOcean;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.TreeMap;

public class BuildCraftEnergy implements IBuildcraftModule {

	public final static int ENERGY_REMOVE_BLOCK = 25;
	public final static int ENERGY_EXTRACT_ITEM = 2;
	public static boolean spawnOilSprings = true;
	public static BiomeGenOilDesert biomeOilDesert;
	public static BiomeGenOilOcean biomeOilOcean;
	public static BlockEngine engineBlock;
	public static BlockEngineWood woodEngineBlock;
	public static BlockEngineStone stoneEngineBlock;
	public static BlockEngineIron ironEngineBlock;
	private static Fluid buildcraftFluidOil;
	private static Fluid buildcraftFluidFuel;
	public static Fluid fluidOil;
	public static Fluid fluidFuel;
	public static Block blockOil;
	public static Block blockFuel;
	public static Item bucketOil;
	public static Item bucketFuel;
	public static Item fuel;
	public static boolean canOilBurn;
	public static double oilWellScalar = 1.0;
	public static TreeMap<BlockIndex, Integer> saturationStored = new TreeMap<BlockIndex, Integer>();
	public static BCTrigger triggerBlueEngineHeat = new TriggerEngineHeat(TileEngine.EnergyStage.BLUE);
	public static BCTrigger triggerGreenEngineHeat = new TriggerEngineHeat(TileEngine.EnergyStage.GREEN);
	public static BCTrigger triggerYellowEngineHeat = new TriggerEngineHeat(TileEngine.EnergyStage.YELLOW);
	public static BCTrigger triggerRedEngineHeat = new TriggerEngineHeat(TileEngine.EnergyStage.RED);

	public static BuildCraftEnergy INSTANCE = new BuildCraftEnergy();

	@Override
	public void registerConfigProps(BuildCraftAddon addon) {

	}

	@Override
	public void handleConfigProps() {

	}

	@Override
	public void init() { //todoenergy config
		/*		Property engineId = BuildCraftCore.mainConfiguration.getBlock("engine.id", DefaultProps.ENGINE_ID);

		// Update oil tag
		int defaultOilId = DefaultProps.OIL_ID;
		if (BuildCraftCore.mainConfiguration.hasKey(Configuration.CATEGORY_BLOCK, "oilStill.id")) {
			defaultOilId = BuildCraftCore.mainConfiguration.get(Configuration.CATEGORY_BLOCK, "oilStill.id", defaultOilId).getInt(defaultOilId);
			BuildCraftCore.mainConfiguration.getCategory(Configuration.CATEGORY_BLOCK).remove("oilStill.id");
		}
		int blockOilId = BuildCraftCore.mainConfiguration.getBlock("oil.id", defaultOilId).getInt(defaultOilId);

		int blockFuelId = BuildCraftCore.mainConfiguration.getBlock("fuel.id", DefaultProps.FUEL_ID).getInt(DefaultProps.FUEL_ID);
		int bucketOilId = BuildCraftCore.mainConfiguration.getItem("bucketOil.id", DefaultProps.BUCKET_OIL_ID).getInt(DefaultProps.BUCKET_OIL_ID);
		int bucketFuelId = BuildCraftCore.mainConfiguration.getItem("bucketFuel.id", DefaultProps.BUCKET_FUEL_ID).getInt(DefaultProps.BUCKET_FUEL_ID);
		int oilDesertBiomeId = BuildCraftCore.mainConfiguration.get("biomes", "oilDesert", DefaultProps.BIOME_OIL_DESERT).getInt(DefaultProps.BIOME_OIL_DESERT);
		int oilOceanBiomeId = BuildCraftCore.mainConfiguration.get("biomes", "oilOcean", DefaultProps.BIOME_OIL_OCEAN).getInt(DefaultProps.BIOME_OIL_OCEAN);
		canOilBurn = BuildCraftCore.mainConfiguration.get(Configuration.CATEGORY_GENERAL, "burnOil", true, "Can oil burn?").getBoolean(true);
		oilWellScalar = BuildCraftCore.mainConfiguration.get(Configuration.CATEGORY_GENERAL, "oilWellGenerationRate", 1.0, "Probability of oil well generation").getDouble(1.0);

		double fuelOilMultiplier = BuildCraftCore.mainConfiguration.get(Configuration.CATEGORY_GENERAL, "fuel.oil.combustion", 1.0F, "adjust energy value of Oil in Combustion Engines").getDouble(1.0F);
		double fuelFuelMultiplier = BuildCraftCore.mainConfiguration.get(Configuration.CATEGORY_GENERAL, "fuel.fuel.combustion", 1.0F, "adjust energy value of Fuel in Combustion Engines").getDouble(1.0F);
		BuildCraftCore.mainConfiguration.save();

		if (oilDesertBiomeId > 0) {
			if (BiomeGenBase.biomeList[oilDesertBiomeId] != null) {
				throw new BiomeIdException("oilDesert", oilDesertBiomeId);
			}
			biomeOilDesert = BiomeGenOilDesert.makeBiome(oilDesertBiomeId);
		}

		if (oilOceanBiomeId > 0) {
			if (BiomeGenBase.biomeList[oilOceanBiomeId] != null) {
				throw new BiomeIdException("oilOcean", oilOceanBiomeId);
			}
			biomeOilOcean = BiomeGenOilOcean.makeBiome(oilOceanBiomeId);
		}*/

		int blockOilId = DefaultProps.OIL_ID;
		int blockFuelId = DefaultProps.FUEL_ID;
		int bucketOilId = DefaultProps.BUCKET_OIL_ID;
		int bucketFuelId = DefaultProps.BUCKET_FUEL_ID;
		canOilBurn = true;

		class BiomeIdException extends RuntimeException {

			public BiomeIdException(String biome, int id) {
				super(String.format("You have a Biome Id conflict at %d for %s", id, biome));
			}
		}




		/*engineBlock = new BlockEngine(DefaultProps.ENGINE_ID);*/
		woodEngineBlock = new BlockEngineWood(DefaultProps.WOOD_ENGINE_ID);
		stoneEngineBlock = new BlockEngineStone(DefaultProps.STONE_ENGINE_ID);
		ironEngineBlock = new BlockEngineIron(DefaultProps.IRON_ENGINE_ID);

/*		LanguageRegistry.addName(new ItemStack(engineBlock, 1, 0), "Redstone Engine");
		LanguageRegistry.addName(new ItemStack(engineBlock, 1, 1), "Steam Engine");
		LanguageRegistry.addName(new ItemStack(engineBlock, 1, 2), "Combustion Engine");*/


		// Oil and fuel
		buildcraftFluidOil = new BCFluid("oil").setDensity(800).setViscosity(1500);

		FluidRegistry.registerFluid(buildcraftFluidOil);
		fluidOil = FluidRegistry.getFluid("oil");

		buildcraftFluidFuel = new BCFluid("fuel");
		FluidRegistry.registerFluid(buildcraftFluidFuel);
		fluidFuel = FluidRegistry.getFluid("fuel");

		if (fluidOil.getBlockID() == -1) {
			if (blockOilId > 0) {
				blockOil = new BlockBuildcraftFluid(blockOilId, fluidOil, Material.water).setFlammable(canOilBurn).setFlammability(0);
				blockOil.setUnlocalizedName("blockOil");
				CoreProxy.getProxy().addName(blockOil, "Oil");
				CoreProxy.getProxy().registerBlock(blockOil);
				fluidOil.setBlockID(blockOil);
			}
		} else {
			blockOil = Block.blocksList[fluidOil.getBlockID()];
		}

/*		if (blockOil != null) {
			spawnOilSprings = BuildCraftCore.mainConfiguration.get("worldgen", "oilSprings", true).getBoolean(true);
			BlockSpring.EnumSpring.OIL.canGen = spawnOilSprings;
			BlockSpring.EnumSpring.OIL.liquidBlock = blockOil;
		}*/

		if (fluidFuel.getBlockID() == -1) {
			if (blockFuelId > 0) {
				blockFuel = new BlockBuildcraftFluid(blockFuelId, fluidFuel, Material.water).setFlammable(true).setFlammability(5).setParticleColor(0.7F, 0.7F, 0.0F);
				blockFuel.setUnlocalizedName("blockFuel");
				CoreProxy.getProxy().addName(blockFuel, "Fuel");
				CoreProxy.getProxy().registerBlock(blockFuel);
				fluidFuel.setBlockID(blockFuel);
			}
		} else {
			blockFuel = Block.blocksList[fluidFuel.getBlockID()];
		}

		// Buckets

		if (blockOil != null && bucketOilId > 0) {
			bucketOil = new ItemBucketBuildcraft(bucketOilId, blockOil.blockID);
			bucketOil.setUnlocalizedName("bucketOil").setContainerItem(Item.bucketEmpty);
			/*LanguageRegistry.addName(bucketOil, "Oil Bucket");*/
			CoreProxy.getProxy().registerItem(bucketOil);
			FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluidStack("oil", FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(bucketOil), new ItemStack(Item.bucketEmpty));
		}

		if (blockFuel != null && bucketFuelId > 0) {
			bucketFuel = new ItemBucketBuildcraft(bucketFuelId, blockFuel.blockID);
			bucketFuel.setUnlocalizedName("bucketFuel").setContainerItem(Item.bucketEmpty);
			/*LanguageRegistry.addName(bucketFuel, "Fuel Bucket");*/
			CoreProxy.getProxy().registerItem(bucketFuel);
			FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluidStack("fuel", FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(bucketFuel), new ItemStack(Item.bucketEmpty));
		}

		BucketHandler.INSTANCE.buckets.put(blockOil, bucketOil);
		BucketHandler.INSTANCE.buckets.put(blockFuel, bucketFuel);
		/*MinecraftForge.EVENT_BUS.register(BucketHandler.INSTANCE);*/

		BuildcraftRecipes.refinery.addRecipe(new FluidStack(fluidOil, 1), new FluidStack(fluidFuel, 1), 12, 1);

		float fuelOilMultiplier = 1.0f; //normally config
		float fuelFuelMultiplier = 1.0f;

		// Iron Engine Fuels
//		IronEngineFuel.addFuel("lava", 1, 20000);
		IronEngineFuel.addFuel("oil", 3, (int) (5000 * fuelOilMultiplier));
		IronEngineFuel.addFuel("fuel", 6, (int) (25000 * fuelFuelMultiplier));

		// Iron Engine Coolants
		IronEngineCoolant.addCoolant(FluidRegistry.getFluid("water"), 0.0023F);
		IronEngineCoolant.addCoolant(Block.ice.blockID, 0, FluidRegistry.getFluidStack("water", FluidContainerRegistry.BUCKET_VOLUME * 2));


		NetworkRegistry.instance().registerGuiHandler("bcenergy", new GuiHandler());

		/*new BptBlockEngine(engineBlock.blockID);*/


		EnergyProxy.getProxy().registerBlockRenderers();
		EnergyProxy.getProxy().registerTileEntities();
	}

	@Override
	public void initRecipes() {
		loadRecipes();
	}

	@Override
	public void postInit() {
/*		if (BuildCraftCore.modifyWorld) {
			MinecraftForge.EVENT_BUS.register(OilPopulate.INSTANCE);
			MinecraftForge.TERRAIN_GEN_BUS.register(new BiomeInitializer());
		}*/
	}


	@Environment(EnvType.CLIENT)
	@Override
	public void textureHook(TextureMap map) {
		if (blockOil == null || blockFuel == null) return;
		if (map.getTextureType() == 0) {
			buildcraftFluidOil.setIcons(blockOil.getBlockTextureFromSide(1), blockOil.getBlockTextureFromSide(2));
			buildcraftFluidFuel.setIcons(blockFuel.getBlockTextureFromSide(1), blockFuel.getBlockTextureFromSide(2));
		}
	}

	@Override
	public String getModId() {
		return "bcenergy";
	}

	public static void loadRecipes() {
		//todoenergy recipes
/*		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(woodEngineBlock),
                "www", " g ", "GpG", 'w', "plankWood", 'g', Block.glass, 'G',
                BuildCraftCore.woodenGearItem, 'p', Block.pistonBase);
		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(stoneEngineBlock), "www", " g ", "GpG", 'w', Block.cobblestone,
                'g', Block.glass, 'G', BuildCraftCore.stoneGearItem, 'p', Block.pistonBase);
		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(ironEngineBlock), "www", " g ", "GpG", 'w', Item.ingotIron,
                'g', Block.glass, 'G', BuildCraftCore.ironGearItem, 'p', Block.pistonBase);*/
	}
}
