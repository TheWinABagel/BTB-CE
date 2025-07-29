package buildcraft.config;

import buildcraft.BuildCraftAddon;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private final Map<String, String> backingProps;
    private final Map<String, Category> categories = new HashMap<>();
    public final Category defaultCategory;

    public Config(Map<String, String> backingProps, BuildCraftAddon bca) {
        this.backingProps = backingProps;
        this.defaultCategory = new Category(bca.getName(), "", 100);
    }

    public boolean registerCategory(Category category) {
        if (!this.categories.containsKey(category.categoryName)) {
            categories.put(category.categoryName, category);
            return true;
        }
        return false;
    }

    public <T> Property<T> registerProp(Category category, Property<T> property) {
        category.register(property.key, property);
        return property;
    }

    public <T> Property<T> registerProp(Property<T> property) {
        this.defaultCategory.register(property.key, property);
        return property;
    }

    public PropSupplier get(Category category, String name) {
        return new PropSupplier(() -> this.categories.get(category.categoryName).get(name));
    }

    public PropSupplier getBlockId(String name) {
        return get(Category.BLOCK_ID, name);
    }

    public PropSupplier getItemId(String name) {
        return get(Category.ITEM_ID, name);
    }

    public boolean save() {
        categories.entrySet().stream()
                .sorted(Comparator.comparingInt(value -> value.getValue().order))
                .forEach(entry -> {

        });
        return true;
    }
}