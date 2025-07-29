package buildcraft.config;

import java.util.function.Supplier;

public record PropSupplier(Supplier<Property<?>> prop) implements Supplier<Property<?>> {

    @Override
    public Property<?> get() {
        if (prop == null) {
            throw new RuntimeException("Property supplier is null!");
        }
        return prop.get();
    }
}
