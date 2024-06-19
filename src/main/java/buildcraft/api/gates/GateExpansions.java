/**
 * Copyright (c) 2011-2017, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 *
 * The BuildCraft API is distributed under the terms of the MIT License. Please check the contents of the license, which
 * should be located as "LICENSE.API" in the BuildCraft source code distribution.
 */
package buildcraft.api.gates;

import com.google.common.collect.HashBiMap;
import net.minecraft.src.ItemStack;

import java.util.*;

public final class GateExpansions {

    private static final Map<String, IGateExpansion> expansions = new HashMap<String, IGateExpansion>();
    private static final ArrayList<IGateExpansion> expansionIDs = new ArrayList<IGateExpansion>();
    private static final Map<IGateExpansion, ItemStack> recipes = HashBiMap.create();

    private GateExpansions() {}

    public static void registerExpansion(IGateExpansion expansion) {
        registerExpansion(expansion.getUniqueIdentifier(), expansion);
    }

    public static void registerExpansion(String identifier, IGateExpansion expansion) {
        expansions.put(identifier, expansion);
        expansionIDs.add(expansion);
    }

    public static void registerExpansion(IGateExpansion expansion, ItemStack addedRecipe) {
        registerExpansion(expansion.getUniqueIdentifier(), expansion);
        recipes.put(expansion, addedRecipe);
    }

    public static IGateExpansion getExpansion(String identifier) {
        return expansions.get(identifier);
    }

    public static Set<IGateExpansion> getExpansions() {
        Set<IGateExpansion> set = new HashSet<IGateExpansion>();
        set.addAll(expansionIDs);
        return set;
    }

    public static Map<IGateExpansion, ItemStack> getRecipesForPostInit() {
        return recipes;
    }

    // The code below is used by networking.

    public static IGateExpansion getExpansionByID(int id) {
        return expansionIDs.get(id);
    }

    public static int getExpansionID(IGateExpansion expansion) {
        return expansionIDs.indexOf(expansion);
    }
}
