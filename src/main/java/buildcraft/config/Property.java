package buildcraft.config;

public class Property<T> {
    protected String key;
    protected T defaultVal;
    protected T value;
    protected String comment;
    protected Category category;

    public Property(String key, T defaultVal, T value, String comment, Category category) {
        this.defaultVal = defaultVal;
        this.value = value;
        this.comment = comment;
        this.category = category;
        category.register(key, this);
    }

    public Property(String key, T value, String comment, Category category) {
        this(key, value, value, comment, category);
    }

    public Property(String key, T defaultVal, T value, Category category) {
        this(key, defaultVal, value, "", category);
    }

    public Property(String key, T value, String comment) {
        this(key, value, comment, null);
    }

    public Property(String key, T value, Category category) {
        this(key, value, "", category);
    }

    public Property(String key, T value) {
        this(key, value, "");
    }

    public String getKey() {
        return this.key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getDefault() {
        return defaultVal;
    }

    public boolean isDefault() {
        return value.equals(defaultVal);
    }

    public String getValAsString() {
        return String.valueOf(value);
    }

    public void resetToDefault() {
        this.value = defaultVal;
    }

    public String getComment() {
        return this.comment;
    }

    public Category getCategory() {
        return this.category;
    }
}
