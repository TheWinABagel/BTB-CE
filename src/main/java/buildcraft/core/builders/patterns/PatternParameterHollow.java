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

public class PatternParameterHollow implements IStatementParameter {

    private static Icon iconHollow, iconFilled;

    public boolean filled = false;

    public PatternParameterHollow() {
        super();
    }

    public PatternParameterHollow(boolean hollow) {
        this();
        this.filled = !hollow;
    }

    @Override
    public String getUniqueTag() {
        return "buildcraft:fillerParameterHollow";
    }

    @Override
    public Icon getIcon() {
        return filled ? iconFilled : iconHollow;
    }

    @Override
    public ItemStack getItemStack() {
        return null;
    }

    @Override
    public void registerIcons(IconRegister iconRegister) {
        iconFilled = iconRegister.registerIcon("buildcraftcore:fillerParameters/filled");
        iconHollow = iconRegister.registerIcon("buildcraftcore:fillerParameters/hollow");
    }

    @Override
    public String getDescription() {
        return StringUtils.localize("fillerpattern.parameter." + (filled ? "filled" : "hollow"));
    }

    @Override
    public void onClick(IStatementContainer source, IStatement stmt, ItemStack stack, StatementMouseClick mouse) {
        filled = !filled;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        filled = compound.getBoolean("filled");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("filled", filled);
    }

    @Override
    public IStatementParameter rotateLeft() {
        return this;
    }
}
