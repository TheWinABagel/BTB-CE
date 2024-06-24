package btw.community.example.mixin;

import btw.community.example.injected.EntityPlayerExtension;
import buildcraft.core.utils.BCLog;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityPlayer.class)
public class EntityPlayerMixin implements EntityPlayerExtension {

    @Override
    public void openGui(String mod, int modGuiId, World world, int x, int y, int z) {
        EntityPlayer player = ((EntityPlayer) (Object) this);
        if (!player.worldObj.isRemote) {
            NetworkRegistry.instance().openRemoteGui(mod, ((EntityPlayerMP) player), modGuiId, world, x, y, z);
        } else if (player.worldObj.isRemote) {
            NetworkRegistry.instance().openLocalGui(mod, player, modGuiId, world, x, y, z);
        } else {
            BCLog.logger.fine("Invalid attempt to open a local GUI on a dedicated server. This is likely a bug. GUIID: " + mod + " " + modGuiId);
        }
    }
}
