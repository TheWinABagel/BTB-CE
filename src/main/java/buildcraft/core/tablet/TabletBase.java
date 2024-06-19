package buildcraft.core.tablet;

import buildcraft.api.core.BCLog;
import buildcraft.api.tablet.ITablet;
import buildcraft.api.tablet.TabletAPI;
import buildcraft.api.tablet.TabletProgram;
import buildcraft.api.tablet.TabletProgramFactory;
import net.minecraft.src.NBTTagCompound;

import java.util.LinkedList;

public abstract class TabletBase implements ITablet {

    protected final LinkedList<TabletProgram> programs = new LinkedList<TabletProgram>();

    protected TabletBase() {}

    public void tick(float time) {
        if (programs.size() > 0) {
            programs.getLast().tick(time);
        }
    }

    @Override
    public int getScreenWidth() {
        return 244;
    }

    @Override
    public int getScreenHeight() {
        return 306;
    }

    protected boolean launchProgramInternal(String name) {
        TabletProgramFactory factory = TabletAPI.getProgram(name);
        if (factory == null) {
            BCLog.logger.error("Tried to launch non-existent tablet program on side CLIENT: " + name);
            return false;
        }
        TabletProgram program = factory.create(this);
        if (program == null) {
            BCLog.logger.error("Factory could not create program on side CLIENT: " + name);
            return false;
        }
        programs.add(program);
        return true;
    }

    public abstract void receiveMessage(NBTTagCompound compound);

    protected boolean receiveMessageInternal(NBTTagCompound compound) {
        if (compound.hasKey("__program")) {
            compound.removeTag("__program");
            if (programs.getLast() != null) {
                programs.getLast().receiveMessage(compound);
            }
            return true;
        } else {
            if (compound.hasKey("programToLaunch")) {
                launchProgramInternal(compound.getString("programToLaunch"));
                return true;
            }
        }
        return false;
    }
}
