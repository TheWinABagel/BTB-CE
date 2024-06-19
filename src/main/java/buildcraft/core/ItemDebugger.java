package buildcraft.core;

import buildcraft.api.tiles.IDebuggable;
import buildcraft.core.lib.items.ItemBuildCraft;
import buildcraft.core.lib.utils.StringUtils;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.src.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class ItemDebugger extends ItemBuildCraft {

    public ItemDebugger() {
        super();

        setFull3D();
        setMaxStackSize(1);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
            float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return false;
        }

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IDebuggable) {
            ArrayList<String> info = new ArrayList<String>();
            ((IDebuggable) tile).getDebugInfo(info, ForgeDirection.getOrientation(side), stack, player);
            for (String s : info) {
                player.addChatComponentMessage(new ChatComponentText(s));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean debug) {
        list.add(StringUtils.localize("item.debugger.warning"));
    }
}
