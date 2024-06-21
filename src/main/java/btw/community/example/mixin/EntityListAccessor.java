package btw.community.example.mixin;

import net.minecraft.src.EntityList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(EntityList.class)
public interface EntityListAccessor {
    @Accessor
    static Map getClassToStringMapping() {
        throw new UnsupportedOperationException();
    }
}
