package buildcraft.transport;

import buildcraft.core.ItemBuildCraft;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ItemPlug extends ItemBuildCraft {

	public ItemPlug(int i) {
		super(i);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return "item.PipePlug";
	}

	
	@Override
	public boolean shouldPassSneakingClickToBlock(World worldObj, int x, int y, int z ) {
		return true;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
	    // NOOP
	}

	@Override
    @Environment(EnvType.CLIENT)
    public int getSpriteNumber()
    {
        return 0;
	}
	
}
