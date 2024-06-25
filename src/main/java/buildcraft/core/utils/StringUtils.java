package buildcraft.core.utils;

import net.minecraft.src.I18n;

public class StringUtils {

	public static String localize(String key) {
		return I18n.getString(key);
	}
}
