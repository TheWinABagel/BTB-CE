package buildcraft.energy.worldgen;

import buildcraft.BuildCraftEnergy;
import net.minecraft.src.GenLayer;
import net.minecraft.src.WorldType;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public class BiomeInitializer {
	//todoenergy world gen
	public BiomeInitializer() {
	}
	
	public static GenLayer[] initBcBiomes(WorldType worldType, long seed, GenLayer[] original) {
		if (BuildCraftEnergy.biomeOilDesert != null) {
			original[0] = new GenLayerAddOilDesert(seed, 1500L, original[0]);
			original[1] = new GenLayerAddOilDesert(seed, 1500L, original[1]);
			original[2] = new GenLayerAddOilDesert(seed, 1500L, original[2]);
		}
		if (BuildCraftEnergy.biomeOilOcean != null) {
			original[0] = new GenLayerAddOilOcean(seed, 1500L, original[0]);
			original[1] = new GenLayerAddOilOcean(seed, 1500L, original[1]);
			original[2] = new GenLayerAddOilOcean(seed, 1500L, original[2]);
		}
		return original;
	}
//		int range = GenLayerBiomeReplacer.OFFSET_RANGE;
//		Random rand = new Random(seed);
//		double xOffset = rand.nextInt(range) - (range / 2);
//		double zOffset = rand.nextInt(range) - (range / 2);
//		double noiseScale = GenLayerAddOilOcean.NOISE_FIELD_SCALE;
//		double noiseThreshold = GenLayerAddOilOcean.NOISE_FIELD_THRESHOLD;
//		for (int x = -5000; x < 5000; x += 128) {
//			for (int z = -5000; z < 5000; z += 128) {
//				if (SimplexNoise.noise((x + xOffset) * noiseScale, (z + zOffset) * noiseScale) > noiseThreshold) {
//					System.out.printf("Oil Biome: %d, %d\n", x, z);
//				}
//			}
//		}

}
