package buildcraft.core.tablet;

import buildcraft.core.lib.items.ItemBuildCraft;
import buildcraft.core.tablet.manager.TabletManagerServer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Minecraft;
import net.minecraft.src.World;

public class ItemTablet extends ItemBuildCraft {

    public ItemTablet(int id) {
        super(id);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (world.isRemote) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiTablet(player));
//            FMLCommonHandler.instance().showGuiScreen(new GuiTablet(player));
        } else {
            TabletManagerServer.INSTANCE.get(player);
        }

        return stack;
    }
}
