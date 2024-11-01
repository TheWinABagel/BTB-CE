package buildcraft.energy;

import btw.block.model.BlockModel;
import net.minecraft.src.AxisAlignedBB;

import java.util.ArrayList;
import java.util.List;

public class EngineModel extends BlockModel {
    public List<AxisAlignedBB> boxBase;

    @Override
    protected void initModel() {
        boxBase = new ArrayList<>();
        AxisAlignedBB bb1 = new AxisAlignedBB(4F / 16F, 0.0F, 4F / 16F, 12F / 16F, 1.0F, 12F / 16F);
        this.addBox(bb1.minX, bb1.minY, bb1.minZ, bb1.maxX, bb1.maxY, bb1.maxZ);
        AxisAlignedBB bb2 = new AxisAlignedBB(0, 0, 0, 1, 8F / 16F, 1);
        this.addBox(bb2.minX, bb2.minY, bb2.minZ, bb2.maxX, bb2.maxY, bb2.maxZ);
        boxBase.add(bb1.makeTemporaryCopy());
        boxBase.add(bb2.makeTemporaryCopy());
    }
}