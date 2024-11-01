package buildcraft.factory;

import btw.block.model.BlockModel;
import buildcraft.core.CoreConstants;
import net.minecraft.src.AxisAlignedBB;

import java.util.ArrayList;
import java.util.List;

public class RefineryModel extends BlockModel {
    public List<AxisAlignedBB> boxBase;

    @Override
    protected void initModel() {
        boxBase = new ArrayList<>();
        AxisAlignedBB bb1 = new AxisAlignedBB(CoreConstants.PIPE_MIN_POS, 8F / 16F, 0.0F, CoreConstants.PIPE_MAX_POS, 1.0F, 1.0F);
        this.addPrimitive(bb1);
        AxisAlignedBB bb2 = new AxisAlignedBB(0F, 0F, 0F, 1, 8F / 16F, 1);
        this.addPrimitive(bb2);
        boxBase.add(bb1.makeTemporaryCopy());
        boxBase.add(bb2.makeTemporaryCopy());
    }
}