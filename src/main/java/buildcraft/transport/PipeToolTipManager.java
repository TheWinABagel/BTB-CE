/*
 * Copyright (c) SpaceToad, 2011-2012
 * http://www.mod-buildcraft.com
 * 
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.transport;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.I18n;

import java.util.*;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info/>
 */
@Environment(EnvType.CLIENT)
public class PipeToolTipManager {

	private static final Map<Class<? extends Pipe>, String> toolTips = new HashMap<>();

	static {
		for (Map.Entry<Class<? extends Pipe>, Integer> pipe : PipeTransportPower.powerCapacities.entrySet()) {
			PipeToolTipManager.addToolTip(pipe.getKey(), String.format("%d MJ/t", pipe.getValue()));
		}
	}

	public static void addToolTip(Class<? extends Pipe> pipe, String toolTip) {
		toolTips.put(pipe, toolTip);
	}

	public static List<String> getToolTip(Class<? extends Pipe> pipe) {
		List<String> tips = new ArrayList<>();
		String tipTag = "tip." + pipe.getSimpleName();
		String localized = I18n.getString(tipTag);
		String[] lines = localized.split("\\\\n");
		tips.addAll(Arrays.asList(lines));

		String tip = toolTips.get(pipe);
		if (tip != null)
			tips.add(tip);
		return tips;
	}
}
