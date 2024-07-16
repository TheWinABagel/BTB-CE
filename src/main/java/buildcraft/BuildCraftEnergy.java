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

public class BuildCraftEnergy implements IBuildCraftModule {

	public final static int ENERGY_REMOVE_BLOCK = 25;
	public final static int ENERGY_EXTRACT_ITEM = 2;
	public static boolean spawnOilSprings = true;
	public static BiomeGenOilDesert biomeOilDesert;
	public static BiomeGenOilOcean biomeOilOcean;
	public static BlockEngineWood woodEngineBlock;
	public static BlockEngineStone stoneEngineBlock;
	public static BlockEngineIron ironEngineBlock;
	public static BlockEngineCreative creativeEngineBlock;
	private static Fluid buildcraftFluidOil;
	private static Fluid buildcraftFluidFuel;
	public static Fluid fluidOil;
	public static Fluid fluidFuel;
	public static Block blockOil;
	public static Block blockFuel;
	public static Item bucketOil;
	public static Item bucketFuel;
	public static boolean canOilBurn;
	public static double oilWellScalar = 1.0;
	public static TreeMap<BlockIndex, Integer> saturationStored = new TreeMap<>();
	public static BCTrigger triggerBlueEngineHeat = new TriggerEngineHeat(TileEngine.EnergyStage.BLUE);
	public static BCTrigger triggerGreenEngineHeat = new TriggerEngineHeat(TileEngine.EnergyStage.GREEN);
	public static BCTrigger triggerYellowEngineHeat = new TriggerEngineHeat(TileEngine.EnergyStage.YELLOW);
	public static BCTrigger triggerRedEngineHeat = new TriggerEngineHeat(TileEngine.EnergyStage.RED);

	public static BuildCraftEnergy INSTANCE = new BuildCraftEnergy();

	@Override
	public void registerConfigForSettings(BuildCraftAddon addon) {
		addon.registerProp("CanOilBurn", true, "Energy\n\n# If oil is able to be lit on fire in world. Default: true (Boolean)");
		addon.registerProp("OilWellGenerationRate", 1.0, "Probability of oil well generation. Default: 1.0 (Double)");
		addon.registerProp("OilCombustionRate", 1.0, "Energy generation multiplier for oil in a Combustion Engine. Default: 1.0 (Double)");
		addon.registerProp("FuelCombustionRate", 1.0, "Energy generation multiplier for Fuel in a Combustion Engine. Default: 1.0 (Double)");
	}

	@Override
	public void registerConfigForIds(BuildCraftAddon addon) {
		addon.registerProp("RedstoneEngineBlockId", DefaultProps.WOOD_ENGINE_ID, "Energy\n");
		addon.registerProp("StirlingEngineBlockId", DefaultProps.STONE_ENGINE_ID);
		addon.registerProp("CombustionEngineBlockId", DefaultProps.IRON_ENGINE_ID);
		addon.registerProp("CreativeEngineBlockId", DefaultProps.CREATIVE_ENGINE_ID);
		addon.registerProp("OilFluidBlockId", DefaultProps.OIL_ID);
		addon.registerProp("FuelFluidBlockId", DefaultProps.FUEL_ID);

		addon.registerProp("OilBucketItemId", DefaultProps.BUCKET_OIL_ID);
		addon.registerProp("FuelBucketItemId", DefaultProps.BUCKET_FUEL_ID);

		addon.registerProp("OilDesertBiomeId", DefaultProps.BIOME_OIL_DESERT);
		addon.registerProp("OilOceanBiomeId", DefaultProps.BIOME_OIL_OCEAN);
	}

	@Override
	public void handleConfigProps() {
		canOilBurn = BuildcraftConfig.getBoolean("CanOilBurn");
		oilWellScalar = BuildcraftConfig.getDouble("OilWellGenerationRate");

		double fuelOilMultiplier = BuildcraftConfig.getDouble("OilCombustionRate");
		double fuelFuelMultiplier = BuildcraftConfig.getDouble("FuelCombustionRate");
		IronEngineFuel.addFuel("oil", 3, (int) (5000 * fuelOilMultiplier));
		IronEngineFuel.addFuel("fuel", 6, (int) (25000 * fuelFuelMultiplier));

		BuildcraftConfig.woodenEngineBlockId = BuildcraftConfig.getInt("RedstoneEngineBlockId");
		BuildcraftConfig.stoneEngineBlockId = BuildcraftConfig.getInt("StirlingEngineBlockId");
		BuildcraftConfig.ironEngineBlockId = BuildcraftConfig.getInt("CombustionEngineBlockId");
		BuildcraftConfig.creativeEngineBlockId = BuildcraftConfig.getInt("CreativeEngineBlockId");
		BuildcraftConfig.oilBlockId = BuildcraftConfig.getInt("OilFluidBlockId");
		BuildcraftConfig.fuelBlockId = BuildcraftConfig.getInt("FuelFluidBlockId");

		BuildcraftConfig.bucketOilItemId = BuildcraftConfig.getInt("OilBucketItemId");
		BuildcraftConfig.bucketFuelItemId = BuildcraftConfig.getInt("FuelBucketItemId");

		BuildcraftConfig.oilDesertBiomeId = BuildcraftConfig.getInt("OilDesertBiomeId");
		BuildcraftConfig.oilOceanBiomeId = BuildcraftConfig.getInt("OilOceanBiomeId");
	}

	@Override
	public void init() {

		woodEngineBlock = new BlockEngineWood(BuildcraftConfig.woodenEngineBlockId);
		stoneEngineBlock = new BlockEngineStone(BuildcraftConfig.stoneEngineBlockId);
		ironEngineBlock = new BlockEngineIron(BuildcraftConfig.ironEngineBlockId);
		creativeEngineBlock = new BlockEngineCreative(BuildcraftConfig.creativeEngineBlockId);

		// Oil and fuel
		buildcraftFluidOil = new BCFluid("oil").setDensity(800).setViscosity(1500);

		FluidRegistry.registerFluid(buildcraftFluidOil);

		buildcraftFluidFuel = new BCFluid("fuel");
		FluidRegistry.registerFluid(buildcraftFluidFuel);

		fluidOil = FluidRegistry.getFluid("oil");
		if (BuildcraftConfig.oilBlockId > 0) {
			blockOil = new BlockBuildcraftFluid(BuildcraftConfig.oilBlockId, fluidOil, Material.water).setFlammable(canOilBurn).setFlammability(0);
			blockOil.setUnlocalizedName("blockOil");
			CoreProxy.getProxy().addName(blockOil, "Oil");
			CoreProxy.getProxy().registerBlock(blockOil);
			fluidOil.setBlockID(blockOil);
		}

		fluidFuel = FluidRegistry.getFluid("fuel");
		if (BuildcraftConfig.fuelBlockId > 0) {
			blockFuel = new BlockBuildcraftFluid(BuildcraftConfig.fuelBlockId, fluidFuel, Material.water).setFlammable(true).setFlammability(5).setParticleColor(0.7F, 0.7F, 0.0F);
			blockFuel.setUnlocalizedName("blockFuel");
			CoreProxy.getProxy().addName(blockFuel, "Fuel");
			CoreProxy.getProxy().registerBlock(blockFuel);
			fluidFuel.setBlockID(blockFuel);
		}

		class BiomeIdException extends RuntimeException {

			public BiomeIdException(String biome, int id) {
				super(String.format("You have a Biome Id conflict at %d for %s", id, biome));
			}
		}

		if (BuildcraftConfig.oilDesertBiomeId > 0) {
			if (BiomeGenBase.biomeList[BuildcraftConfig.oilDesertBiomeId] != null) {
				throw new BiomeIdException("oilDesert", BuildcraftConfig.oilDesertBiomeId);
			}
			biomeOilDesert = BiomeGenOilDesert.makeBiome(BuildcraftConfig.oilDesertBiomeId);
		}

		if (BuildcraftConfig.oilOceanBiomeId > 0) {
			if (BiomeGenBase.biomeList[BuildcraftConfig.oilOceanBiomeId] != null) {
				throw new BiomeIdException("oilOcean", BuildcraftConfig.oilOceanBiomeId);
			}
			biomeOilOcean = BiomeGenOilOcean.makeBiome(BuildcraftConfig.oilOceanBiomeId);
		}

		// Buckets

		if (blockOil != null && BuildcraftConfig.bucketOilItemId > 0) {
			bucketOil = new ItemBucketBuildcraft(BuildcraftConfig.bucketOilItemId, blockOil.blockID);
			bucketOil.setUnlocalizedName("bucketOil").setContainerItem(Item.bucketEmpty);
			CoreProxy.getProxy().registerItem(bucketOil);
			FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluidStack("oil", FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(bucketOil), new ItemStack(Item.bucketEmpty));
		}

		if (blockFuel != null && BuildcraftConfig.bucketFuelItemId > 0) {
			bucketFuel = new ItemBucketBuildcraft(BuildcraftConfig.bucketFuelItemId, blockFuel.blockID);
			bucketFuel.setUnlocalizedName("bucketFuel").setContainerItem(Item.bucketEmpty);
			CoreProxy.getProxy().registerItem(bucketFuel);
			FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluidStack("fuel", FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(bucketFuel), new ItemStack(Item.bucketEmpty));
		}

		BucketHandler.INSTANCE.buckets.put(blockOil, bucketOil);
		BucketHandler.INSTANCE.buckets.put(blockFuel, bucketFuel);
//		MinecraftForge.EVENT_BUS.register(BucketHandler.INSTANCE);

		BuildcraftRecipes.refinery.addRecipe(new FluidStack(fluidOil, 1), new FluidStack(fluidFuel, 1), 12, 1);

		// Iron Engine Coolants
		IronEngineCoolant.addCoolant(FluidRegistry.getFluid("water"), 0.0023F);
		IronEngineCoolant.addCoolant(Block.ice.blockID, 0, FluidRegistry.getFluidStack("water", FluidContainerRegistry.BUCKET_VOLUME * 2));


        NetworkRegistry.instance().registerGuiHandler("bcenergy", new GuiHandlerEnergy());

		new BptBlockEngine(woodEngineBlock.blockID);
		new BptBlockEngine(stoneEngineBlock.blockID);
		new BptBlockEngine(ironEngineBlock.blockID);
		new BptBlockEngine(creativeEngineBlock.blockID);


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
