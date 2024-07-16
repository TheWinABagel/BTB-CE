package cpw.mods.fml.common.network;

import dev.bagel.btb.injected.EntityPlayerExtension;
import btw.network.packet.handler.CustomPacketHandler;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.minecraft.src.Minecraft;
import net.minecraft.src.Packet250CustomPayload;

import java.io.IOException;

public class ForgeCustomPacket implements CustomPacketHandler {
    private int windowId;
    private int networkId;
    private int modGuiId;
    /**
     * The x coordinate of this ChunkPosition
     */
    private int x;
    /**
     * The y coordinate of this ChunkPosition
     */
    private int y;
    /**
     * The z coordinate of this ChunkPosition
     */
    private int z;
    @Override
    public void handleCustomPacket(Packet250CustomPayload data) throws IOException {
        ByteArrayDataInput dat = ByteStreams.newDataInput(data.data);
        this.windowId = dat.readInt();
        this.networkId = dat.readInt();
        this.modGuiId = dat.readInt();
        this.x = dat.readInt();
        this.y = dat.readInt();
        this.z = dat.readInt();

        ((EntityPlayerExtension) Minecraft.getMinecraft().thePlayer).btb$openGui(String.valueOf(this.networkId), this.modGuiId, Minecraft.getMinecraft().thePlayer.worldObj, this.x, this.y, this.z);
        Minecraft.getMinecraft().thePlayer.openContainer.windowId = this.windowId;
    }
}
