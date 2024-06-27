package buildcraft.factory;

import btw.community.example.injected.EntityPlayerExtension;
import buildcraft.BuildCraftCore;
import buildcraft.BuildCraftFactory;
import buildcraft.core.BlockBuildCraft;
import buildcraft.core.GuiIds;
import buildcraft.core.IItemPipe;
import buildcraft.core.proxy.CoreProxy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Material;
import net.minecraft.src.IconRegister;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Icon;
import net.minecraft.src.World;

public class BlockHopper extends BlockBuildCraft {

	private static Icon icon;

	public BlockHopper(int blockId) {
		super(blockId, Material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileHopper();
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderType() {
		return BuildCraftCore.blockByEntityModel;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		super.onBlockActivated(world, x, y, z, entityplayer, par6, par7, par8, par9);

		// Drop through if the player is sneaking
		if (entityplayer.isSneaking()) {
			return false;
		}

		if (entityplayer.getCurrentEquippedItem() != null) {
			if (entityplayer.getCurrentEquippedItem().getItem() instanceof IItemPipe) {
				return false;
			}
		}

		if (!CoreProxy.getProxy().isRenderWorld(world)) {
			((EntityPlayerExtension) entityplayer).openGui(BuildCraftFactory.INSTANCE.getModId(), GuiIds.HOPPER, world, x, y, z);
		}

		return true;
	}

/*	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public void addCreativeItems(ArrayList itemList) {
		itemList.add(new ItemStack(this));
	}*/

	@Override
	@Environment(EnvType.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		icon = par1IconRegister.registerIcon("buildcraft:hopperBottom");
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Icon getIcon(int par1, int par2) {
		return icon;
	}
}
