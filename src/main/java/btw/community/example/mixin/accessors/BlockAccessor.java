package btw.community.example.mixin.accessors;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Block.class)
public interface BlockAccessor {
    @Invoker
    void callSetBlockBounds(float par1, float par2, float par3, float par4, float par5, float par6);

    @Accessor
    void setMinX(double minX);

    @Accessor
    void setMinY(double minY);

    @Accessor
    void setMinZ(double minZ);

    @Accessor
    void setMaxX(double maxX);

    @Accessor
    void setMaxY(double maxY);

    @Accessor
    void setMaxZ(double maxZ);

    @Invoker
    void callInitBlockBounds(double dMinX, double dMinY, double dMinZ, double dMaxX, double dMaxY, double dMaxZ);

    @Accessor
    AxisAlignedBB getFixedBlockBounds();
}
