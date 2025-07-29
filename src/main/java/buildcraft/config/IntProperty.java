package buildcraft.config;

public class IntProperty extends Property<Integer> {


    public IntProperty(String key, Integer defaultVal, Integer value, String comment, Category category) {
        super(key, defaultVal, value, comment, category);
    }

    public IntProperty(String key, Integer value, String comment, Category category) {
        super(key, value, comment, category);
    }

    public IntProperty(String key, Integer defaultVal, Integer value, Category category) {
        super(key, defaultVal, value, category);
    }

    public IntProperty(String key, Integer value, String comment) {
        super(key, value, comment);
    }

    public IntProperty(String key, Integer value, Category category) {
        super(key, value, category);
    }

    public IntProperty(String key, Integer value) {
        super(key, value);
    }
//
//    public static IntProperty of(Integer defaultVal, Integer value, String comment, Category category) {
//        return new IntProperty(defaultVal, value, comment, category);
//    }
//
//    public static IntProperty of(Integer value, String comment, Category category) {
//        return of(value, value, comment, category);
//    }
//
//    public static IntProperty of(Integer defaultVal, Integer value, Category category) {
//        return of(defaultVal, value, "", category);
//    }
//
//    public static IntProperty of(Integer value, String comment) {
//        return of(value, comment, Category.DEFAULT);
//    }
//
//    public static IntProperty of(Integer value, Category category) {
//        return of(value, "", category);
//    }
//
//    public static IntProperty of(Integer value) {
//        return of(value, "");
//    }
//
//    public static IntProperty itemId(Integer value) {
//        return of(value, Category.ITEM_ID);
//    }
//
//    public static IntProperty itemId(Integer value, String comment) {
//        return of(value, comment, Category.ITEM_ID);
//    }
//
//    public static IntProperty blockId(Integer value) {
//        return of(value, Category.BLOCK_ID);
//    }
//
//    public static IntProperty blockId(Integer value, String comment) {
//        return of(value, comment, Category.BLOCK_ID);
//    }
}
