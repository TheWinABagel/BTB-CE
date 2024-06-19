package buildcraft.api.robots;

import net.minecraft.src.World;

public interface IRobotRegistryProvider {

    IRobotRegistry getRegistry(World world);
}
