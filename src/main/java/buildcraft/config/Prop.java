package buildcraft.config;

import java.util.function.Supplier;

public class Prop<T> {
    public Supplier<T> value;
    public Supplier<T> defaultValue;
    public String comment;
    public String category;

    public PropTypeBuilder<T> builder(T type) {
        return new PropTypeBuilder<>();
    }

    public static class PropTypeBuilder<T> {
        public Supplier<T> value;
        public Supplier<T> defaultValue;
        public String comment;
        public String category;

        private PropTypeBuilder() {}

        public PropTypeBuilder<T> setValue(Supplier<T> value) {
            this.value = value;
            return this;
        }

        public PropTypeBuilder<T> setValue(T value) {
            return setValue(() -> value);
        }
    }
}
