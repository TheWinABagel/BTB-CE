package buildcraft.api.transport;

import net.minecraft.src.World;

import java.util.ArrayList;
import java.util.List;

public abstract class PipeManager {

	public static List<IExtractionHandler> extractionHandlers = new ArrayList<IExtractionHandler>();

	public static void registerExtractionHandler(IExtractionHandler handler) {
		extractionHandlers.add(handler);
	}

	/**
	 * param extractor can be null
	 */
	public static boolean canExtractItems(Object extractor, World world, int i, int j, int k) {
		for (IExtractionHandler handler : extractionHandlers)
			if (!handler.canExtractItems(extractor, world, i, j, k))
				return false;

		return true;
	}
	
	/**
	 * param extractor can be null
	 */
	public static boolean canExtractFluids(Object extractor, World world, int i, int j, int k) {
		for (IExtractionHandler handler : extractionHandlers)
			if (!handler.canExtractFluids(extractor, world, i, j, k))
				return false;

		return true;
	}
}
