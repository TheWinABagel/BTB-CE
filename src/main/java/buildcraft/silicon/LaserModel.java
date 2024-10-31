package buildcraft.silicon;

import btw.block.model.BlockModel;
import net.minecraft.src.AxisAlignedBB;

import java.util.ArrayList;
import java.util.List;

public class LaserModel extends BlockModel {
    public List<AxisAlignedBB> boxBase;

    @Override
    protected void initModel() {
        boxBase = new ArrayList<>();
        AxisAlignedBB bb1 = new AxisAlignedBB(5F / 16F, 0.0F, 5F / 16F, 11F / 16F, 13F / 16F, 11F / 16F);
        this.addBox(bb1.minX, bb1.minY, bb1.minZ, bb1.maxX, bb1.maxY, bb1.maxZ);
        AxisAlignedBB bb2 = new AxisAlignedBB(0, 0, 0, 1, 4F / 16F, 1);
        this.addBox(bb2.minX, bb2.minY, bb2.minZ, bb2.maxX, bb2.maxY, bb2.maxZ);
        boxBase.add(bb1.makeTemporaryCopy());
        boxBase.add(bb2.makeTemporaryCopy());
//        this.addBox(CoreConstants.PIPE_MIN_POS, 0.0F, CoreConstants.PIPE_MIN_POS, CoreConstants.PIPE_MAX_POS, 1.0F, CoreConstants.PIPE_MAX_POS);
//        this.addBox(0, 0, 0, 1, 4F / 16F, 1);
//        this.addBox(CoreConstants.PIPE_MIN_POS, 0.0F, CoreConstants.PIPE_MIN_POS, CoreConstants.PIPE_MAX_POS, 1.0F, CoreConstants.PIPE_MAX_POS);

//        this.addBox(0.0F, 0.0F, 0.0F, 1, 4F / 16F, 1);

//        this.addBox(5F / 16F, 4F / 16F, 5F / 16F, 11F / 16F, 13F / 16F, 11F / 16F);

        //        this.addBox(0.0625, 0.4375, 0.0625, 0.9375, 1.0, 0.9375);
//        this.addBox(0.0, 0.6875, 0.0, 1.0, 0.9375, 1.0);
//        this.boxSelection = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.9375, 1.0);

    }
}
