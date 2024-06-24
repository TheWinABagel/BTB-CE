package buildcraft.energy.worldgen;

import net.minecraft.src.BiomeGenDesert;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public class BiomeGenOilDesert extends BiomeGenDesert {
	//todoenergy world gen
	public static BiomeGenOilDesert makeBiome(int id) {
		BiomeGenOilDesert biome = new BiomeGenOilDesert(id);
		/*BiomeDictionary.registerBiomeType(biome, BiomeDictionary.Type.DESERT);
		OilPopulate.INSTANCE.excessiveBiomes.add(biome.biomeID);
		OilPopulate.INSTANCE.surfaceDepositBiomes.add(biome.biomeID);*/
		return biome;
	}

	private BiomeGenOilDesert(int id) {
		super(id);
		setColor(16421912);
		setBiomeName("Desert Oil Field");
/*		setDisableRain();
		setTemperatureRainfall(2.0F, 0.0F);
		setMinMaxHeight(0.1F, 0.2F);*/
	}
}
