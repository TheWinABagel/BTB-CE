package buildcraft.core.statements;

import buildcraft.api.statements.IStatement;
import buildcraft.api.statements.IStatementContainer;
import buildcraft.api.statements.IStatementParameter;
import buildcraft.api.statements.StatementMouseClick;
import buildcraft.core.lib.utils.StringUtils;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Icon;

public class StatementParameterRedstoneGateSideOnly implements IStatementParameter {

    private static Icon icon;

    public boolean isOn = false;

    public StatementParameterRedstoneGateSideOnly() {}

    @Override
    public ItemStack getItemStack() {
        return null;
    }

    @Override
    public Icon getIcon() {
        if (!isOn) {
            return null;
        } else {
            return icon;
        }
    }

    @Override
    public void onClick(IStatementContainer source, IStatement stmt, ItemStack stack, StatementMouseClick mouse) {
        isOn = !isOn;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        compound.setByte("isOn", isOn ? (byte) 1 : (byte) 0);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("isOn")) {
            isOn = compound.getByte("isOn") == 1;
        }
    }

    @Override
    public String getDescription() {
        return isOn ? StringUtils.localize("gate.parameter.redstone.gateSideOnly") : "";
    }

    @Override
    public String getUniqueTag() {
        return "buildcraft:redstoneGateSideOnly";
    }

    @Override
    public void registerIcons(IconRegister iconRegister) {
        icon = iconRegister.registerIcon("buildcraftcore:triggers/redstone_gate_side_only");
    }

    @Override
    public IStatementParameter rotateLeft() {
        return this;
    }
}
