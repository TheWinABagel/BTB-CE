package buildcraft.core.network;

import buildcraft.core.lib.network.Packet;
import buildcraft.core.lib.network.PacketHandler;
import buildcraft.core.proxy.CoreProxy;
import buildcraft.core.tablet.PacketTabletMessage;
import buildcraft.core.tablet.TabletBase;
import buildcraft.core.tablet.manager.TabletManagerClient;
import buildcraft.core.tablet.manager.TabletManagerServer;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.fabricmc.api.EnvType;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetHandler;

public class PacketHandlerCore extends PacketHandler {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) {
        super.channelRead0(ctx, packet);
        NetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
        EntityPlayer player = CoreProxy.proxy.getPlayerFromNetHandler(netHandler);

        switch (packet.getID()) {
            case PacketIds.TABLET_MESSAGE: {
                if (ctx.channel().attr(NetworkRegistry.CHANNEL_SOURCE).get() == Side.SERVER) {
                    handleTabletClient((PacketTabletMessage) packet);
                } else {
                    handleTabletServer(player, (PacketTabletMessage) packet);
                }
            }
                break;
        }
    }

    private void handleTabletClient(PacketTabletMessage packet) {
        TabletBase tablet = TabletManagerClient.INSTANCE.get().getTablet();
        tablet.receiveMessage(packet.getTag());
    }

    private void handleTabletServer(EntityPlayer player, PacketTabletMessage packet) {
        TabletBase tablet = TabletManagerServer.INSTANCE.get(player);
        tablet.receiveMessage(packet.getTag());
    }
}
