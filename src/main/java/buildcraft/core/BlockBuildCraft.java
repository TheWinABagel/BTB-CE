package buildcraft.core;

import buildcraft.core.utils.Utils;
import java.util.Random;

import net.minecraft.src.*;

public abstract class BlockBuildCraft extends BlockContainer {

	protected static boolean keepInventory = false;
	protected final Random rand = new Random();

	protected BlockBuildCraft(int id, Material material) {
		super(id, material);
		setCreativeTab(CreativeTabs.tabRedstone);
		setHardness(5F);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
		super.onBlockPlacedBy(world, x, y, z, entity, stack);
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if (tile instanceof TileBuildCraft tileBC) {
			tileBC.onBlockPlacedBy(entity, stack);
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		Utils.preDestroyBlock(world, x, y, z);
		super.breakBlock(world, x, y, z, par5, par6);
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if (tile instanceof IMachine machine && machine.isActive())
			return super.getLightValue(world, x, y, z) + 8;
		return super.getLightValue(world, x, y, z);
	}

	@Override
	public String getLocalizedName() {
		return StatCollector.translateToLocal(this.getUnlocalizedName());
	}
}
