package btw.community.example.mixin;

import btw.community.example.injected.EntityPlayerExtension;
import buildcraft.core.utils.BCLog;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityPlayer.class)
public class EntityPlayerMixin implements EntityPlayerExtension {

    @Override
    public void openGui(String mod, int modGuiId, World world, int x, int y, int z) {

        if (((EntityPlayer) (Object) this) instanceof EntityPlayerMP mp) {
            NetworkRegistry.instance().openRemoteGui(mod, mp, modGuiId, world, x, y, z);
        } else if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT)) {
            NetworkRegistry.instance().openLocalGui(mod, ((EntityPlayer) (Object) this), modGuiId, world, x, y, z);
        } else {
            BCLog.logger.fine("Invalid attempt to open a local GUI on a dedicated server. This is likely a bug. GUIID: " + mod + " " + modGuiId);
        }
    }
}
