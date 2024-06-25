package buildcraft.silicon;

import btw.community.example.injected.EntityPlayerExtension;
import buildcraft.BuildCraftSilicon;
import buildcraft.core.CreativeTabBuildCraft;
import buildcraft.core.proxy.CoreProxy;
import buildcraft.core.utils.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import java.util.List;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.Material;
import net.minecraft.src.IconRegister;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Icon;
import net.minecraft.src.World;

public class BlockLaserTable extends BlockContainer {

	@Environment(EnvType.CLIENT)
	private Icon[][] icons;

	public BlockLaserTable(int i) {
		super(i, Material.iron);

		setBlockBounds(0, 0, 0, 1, 9F / 16F, 1);
		setHardness(10F);
		setCreativeTab(CreativeTabBuildCraft.MACHINES.get());
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean isACube() {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		// Drop through if the player is sneaking
		if (entityplayer.isSneaking())
			return false;

		if (!CoreProxy.getProxy().isRenderWorld(world)) {
			int meta = world.getBlockMetadata(i, j, k);
			((EntityPlayerExtension) entityplayer).openGui(BuildCraftSilicon.instance.getModId(), meta, world, i, j, k);
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return null;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		Utils.preDestroyBlock(world, x, y, z);
		super.breakBlock(world, x, y, z, par5, par6);
	}

	@Override
	public Icon getIcon(int side, int meta) {
		int s = side > 1 ? 2 : side;
		return icons[meta][s];
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	@Environment(EnvType.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(this, 1, 0));
		par3List.add(new ItemStack(this, 1, 1));
		par3List.add(new ItemStack(this, 1, 2));
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		icons = new Icon[3][];
		icons[0] = new Icon[3];
		icons[1] = new Icon[3];
		icons[2] = new Icon[3];

		icons[0][0] = par1IconRegister.registerIcon("buildcraft:assemblytable_bottom");
		icons[0][1] = par1IconRegister.registerIcon("buildcraft:assemblytable_top");
		icons[0][2] = par1IconRegister.registerIcon("buildcraft:assemblytable_side");

		icons[1][0] = par1IconRegister.registerIcon("buildcraft:advworkbenchtable_bottom");
		icons[1][1] = par1IconRegister.registerIcon("buildcraft:advworkbenchtable_top");
		icons[1][2] = par1IconRegister.registerIcon("buildcraft:advworkbenchtable_side");

		icons[2][0] = par1IconRegister.registerIcon("buildcraft:integrationtable_bottom");
		icons[2][1] = par1IconRegister.registerIcon("buildcraft:integrationtable_top");
		icons[2][2] = par1IconRegister.registerIcon("buildcraft:integrationtable_side");
	}
}
