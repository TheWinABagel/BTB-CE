package buildcraft.config;

public class BooleanProperty extends Property<Boolean> {

    public BooleanProperty(String key, Boolean defaultVal, Boolean value, String comment, Category category) {
        super(key, defaultVal, value, comment, category);
    }

    public BooleanProperty(String key, Boolean value, String comment, Category category) {
        super(key, value, comment, category);
    }

    public BooleanProperty(String key, Boolean defaultVal, Boolean value, Category category) {
        super(key, defaultVal, value, category);
    }

    public BooleanProperty(String key, Boolean value, String comment) {
        super(key, value, comment);
    }

    public BooleanProperty(String key, Boolean value, Category category) {
        super(key, value, category);
    }

    public BooleanProperty(String key, Boolean value) {
        super(key, value);
    }

    public boolean getAsBoolean() {
        return this.value;
    }
}
