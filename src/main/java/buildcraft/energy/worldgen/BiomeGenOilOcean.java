package buildcraft.energy.worldgen;

import btw.community.example.mixin.accessors.BiomeGenBaseAccessor;
import net.minecraft.src.BiomeGenOcean;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public class BiomeGenOilOcean extends BiomeGenOcean {
	public static BiomeGenOilOcean makeBiome(int id) {
		BiomeGenOilOcean biome = new BiomeGenOilOcean(id);
		OilPopulate.INSTANCE.excessiveBiomes.add(biome.biomeID);
		OilPopulate.INSTANCE.surfaceDepositBiomes.add(biome.biomeID);
		return biome;
	}

	private BiomeGenOilOcean(int id) {
		super(id);
		setBiomeName("Ocean Oil Field");
		setColor(112);
		((BiomeGenBaseAccessor) this).callSetMinMaxHeight(-1.0F, 0.4F);
	}
}
