package buildcraft.config;

import java.util.HashMap;
import java.util.Map;

public class Category {
    public static Category DEFAULT = new Category("Config", "", 0);
    public static Category ITEM_ID = new Category("Item Ids", "Warning: Changing these will mess up existing worlds!", 90);
    public static Category BLOCK_ID = new Category("Block Ids", "Warning: Changing these will mess up existing worlds!", 100);

    public final String categoryName;
    public final String categoryComment;
    public final int order;
    private final Map<String, Property<?>> properties = new HashMap<>();

    public Category(String categoryName, String categoryComment, int order) {
        this.categoryName = categoryName;
        this.categoryComment = categoryComment;
        this.order = order;
    }

    protected Property<?> register(String key, Property<?> property) {
        return this.properties.put(key, property);
    }

    protected Property<?> get(String key) {
        return this.properties.get(key);
    }
}
