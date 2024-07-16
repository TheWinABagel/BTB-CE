package buildcraft.energy;

import buildcraft.core.proxy.CoreProxy;
import net.minecraft.src.ResourceLocation;

public class TileEngineCreative extends TileEngine {
    public TileEngineCreative() {
        needsRedstonePower = true;
    }

    @Override
    public ResourceLocation getTextureFile() {
        return STONE_TEXTURE;
    }

    @Override
    public boolean isBurning() {
        return isRedstonePowered;
    }

    @Override
    public int getScaledBurnTime(int scale) {
        return 0;
    }

    @Override
    public double getMaxEnergy() {
        return 10000;
    }

    @Override
    public double maxEnergyReceived() {
        return 10000;
    }

    @Override
    public double maxEnergyExtracted() {
        return 10000;
    }

    @Override
    public float explosionRange() {
        return 0;
    }

    @Override
    public double getCurrentOutput() {
        return 10;
    }

    @Override
    public void engineUpdate() {
        addEnergy(10);
    }

    @Override
    protected EnergyStage computeEnergyStage() {
        double energyLevel = getEnergyLevel();
        if (energyLevel < 0.25f)
            return EnergyStage.BLUE;
        else if (energyLevel < 0.5f)
            return EnergyStage.GREEN;
        else if (energyLevel < 0.75f)
            return EnergyStage.YELLOW;
        else
            return EnergyStage.RED;
    }

    @Override
    public float getPistonSpeed() {
        if (CoreProxy.getProxy().isServerWorld(worldObj))
            return Math.max(0.08f * getHeatLevel(), 0.01f);
        switch (getEnergyStage()) {
            case GREEN:
                return 0.02F;
            case YELLOW:
                return 0.04F;
            case RED:
                return 0.08F;
            default:
                return 0.01F;
        }
    }

    @Override
    public void checkRedstonePower() {
        this.isRedstonePowered = true;
    }
}
