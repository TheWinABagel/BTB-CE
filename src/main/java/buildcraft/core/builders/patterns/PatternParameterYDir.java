package buildcraft.core.builders.patterns;

import buildcraft.api.statements.IStatement;
import buildcraft.api.statements.IStatementContainer;
import buildcraft.api.statements.IStatementParameter;
import buildcraft.api.statements.StatementMouseClick;
import buildcraft.core.lib.utils.StringUtils;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Icon;

public class PatternParameterYDir implements IStatementParameter {

    private static Icon iconUp, iconDown;

    public boolean up = false;

    public PatternParameterYDir() {
        super();
    }

    public PatternParameterYDir(boolean up) {
        this();
        this.up = up;
    }

    @Override
    public String getUniqueTag() {
        return "buildcraft:fillerParameterYDir";
    }

    @Override
    public Icon getIcon() {
        return up ? iconUp : iconDown;
    }

    @Override
    public ItemStack getItemStack() {
        return null;
    }

    @Override
    public void registerIcons(IconRegister iconRegister) {
        iconUp = iconRegister.registerIcon("buildcraftcore:fillerParameters/stairs_ascend");
        iconDown = iconRegister.registerIcon("buildcraftcore:fillerParameters/stairs_descend");
    }

    @Override
    public String getDescription() {
        return StringUtils.localize("direction." + (up ? "up" : "down"));
    }

    @Override
    public void onClick(IStatementContainer source, IStatement stmt, ItemStack stack, StatementMouseClick mouse) {
        up = !up;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        up = compound.getBoolean("up");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("up", up);
    }

    @Override
    public IStatementParameter rotateLeft() {
        return this;
    }
}
