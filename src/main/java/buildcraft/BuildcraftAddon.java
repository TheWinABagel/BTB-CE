package buildcraft;

import btw.BTWAddon;

public abstract class BuildcraftAddon extends BTWAddon {

    public BuildcraftAddon(String modID) {
        this.modID = modID;
    }

    public String getModId() {
        return this.modID;
    }
}
