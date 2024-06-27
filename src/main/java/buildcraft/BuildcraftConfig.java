package buildcraft;

import java.util.HashMap;
import java.util.Map;

public class BuildcraftConfig {
    private static final Map<String, String> propertyValues = new HashMap<>();

    public static void putAll(Map<String, String> newProps) {
        propertyValues.putAll(newProps);
    }

    public static int getInt(String value) {
        return Integer.parseInt(propertyValues.get(value));
    }
}
