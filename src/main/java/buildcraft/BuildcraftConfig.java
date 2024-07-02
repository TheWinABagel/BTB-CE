package buildcraft;

import buildcraft.core.utils.BCLog;
import net.minecraft.src.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class BuildcraftConfig {
    private static final Map<String, String> propertyValues = new HashMap<>();

    //Core
    public static int wrenchItemId;
    public static int gearWoodItemId;
    public static int gearStoneItemId;
    public static int gearIronItemId;
    public static int gearGoldItemId;
    public static int gearDiamondItemId;
    public static int springBlockId;

    //Silicon
    public static int laserId;
    public static int assemblyTableId;
    public static int advancedCraftingTableId;
    public static int integrationTableId;
    public static int redstoneChipsetId;

    //Transport
    public static int filteredBufferBlockId;
    public static int genericPipeBlockId;
    public static int pipeWaterproofItemId;
    public static int pipeWireItemId;
    public static int pipeGateItemId;
    public static int pipeFacadeItemId;
    public static int pipePlugItemId;

    //Factory
    public static int miningWellId;
    public static int plainPipeId;
    public static int autoWorkbenchId;
    public static int frameId;
    public static int quarryId;
    public static int pumpId;
    public static int floodGateId;
    public static int tankId;
    public static int refineryId;
	public static int hopperId;

    //Energy
    public static int oilDesertBiomeId;
    public static int oilOceanBiomeId;
    public static int woodenEngineBlockId;
    public static int stoneEngineBlockId;
    public static int ironEngineBlockId;
    public static int creativeEngineBlockId;
    public static int oilBlockId;
    public static int fuelBlockId;
    public static int bucketOilItemId;
    public static int bucketFuelItemId;


    public static void putAll(Map<String, String> newProps) {
        propertyValues.putAll(newProps);
    }

    public static int getInt(String value) {
        try {
            return Integer.parseInt(propertyValues.get(value));
        }
        catch (NumberFormatException e) {
            BCLog.logger.severe("String " + value + " is not a valid integer!");
            return 0;
        }
    }

    public static boolean getBoolean(String value) {
        try {
            return Boolean.parseBoolean(propertyValues.get(value));
        }
        catch (NumberFormatException e) {
            BCLog.logger.severe("String " + value + " is not a valid boolean!");
            return false;
        }
    }

    public static float getFloat(String value) {
        try {
            return Float.parseFloat(propertyValues.get(value));
        }
        catch (NumberFormatException e) {
            BCLog.logger.severe("String " + value + " is not a valid float!");
            return 0;
        }
    }

    public static long getLong(String value) {
        try {
            return Long.parseLong(propertyValues.get(value));
        }
        catch (NumberFormatException e) {
            BCLog.logger.severe("String " + value + " is not a valid long!");
            return 0;
        }
    }

    public static double getDouble(String value) {
        try {
            return Double.parseDouble(propertyValues.get(value));
        }
        catch (NumberFormatException e) {
            BCLog.logger.severe("String " + value + " is not a valid double!");
            return 0;
        }
    }

    public static String getString(String value) {
        return propertyValues.get(value);
    }

    @Nullable
    public static String[] getStringList(String value) {
        try {
            return propertyValues.get(value).split(", ", 0);
        }
        catch (NullPointerException e) {
            BCLog.logger.severe("String " + value + " is not a valid string list!");
            return null;
        }
    }

    public static int getClampedInt(String value, int min, int max) {
        try {
            int parsedVal = Integer.parseInt(propertyValues.get(value));
            return MathHelper.clamp_int(parsedVal, min, max);
        }
        catch (NumberFormatException e) {
            BCLog.logger.severe("String " + value + " is not a valid integer!");
            return 0;
        }
    }

    public static float getClampedFloat(String value, float min, float max) {
        try {
            float parsedVal = Float.parseFloat(propertyValues.get(value));
            return MathHelper.clamp_float(parsedVal, min, max);
        }
        catch (NumberFormatException e) {
            BCLog.logger.severe("String " + value + " is not a valid float!");
            return 0;
        }
    }
}
