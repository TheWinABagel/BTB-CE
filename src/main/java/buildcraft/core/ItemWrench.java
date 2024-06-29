package buildcraft.core;

import buildcraft.api.tools.IToolWrench;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.src.Block;
import net.minecraft.src.BlockButton;
import net.minecraft.src.BlockChest;
import net.minecraft.src.BlockLever;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;

public class ItemWrench extends ItemBuildCraft implements IToolWrench {

	private final Set<Class<? extends Block>> shiftRotations = new HashSet<>();

	public ItemWrench(int i) {
		super(i);
		setFull3D();
		setMaxStackSize(1);
		shiftRotations.add(BlockLever.class);
		shiftRotations.add(BlockButton.class);
		shiftRotations.add(BlockChest.class);
	}

	private boolean isShiftRotation(Class<? extends Block> cls) {
		for (Class<? extends Block> shift : shiftRotations) {
			if (shift.isAssignableFrom(cls))
				return true;
		}
		return false;
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		int blockId = world.getBlockId(x, y, z);
		Block block = Block.blocksList[blockId];
		
		if(block == null)
			return false;

		if (player.isSneaking() != isShiftRotation(block.getClass()))
			return false;

		//todocore block rotation?
/*		if (block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side))) {
			player.swingItem();
			return !world.isRemote;
		}*/
		return false;
	}

	@Override
	public boolean canWrench(EntityPlayer player, int x, int y, int z) {
		return true;
	}

	@Override
	public void wrenchUsed(EntityPlayer player, int x, int y, int z) {
		player.swingItem();
	}

	@Override
	public boolean shouldPassSneakingClickToBlock(World par2World, int par4, int par5, int par6) {
		return true;
	}

/*	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ) {
		var box = new Box();
		box.initialize(i, j, k, 10);
		var robot = new EntityRobot(par3World, box);
		par3World.spawnEntityInWorld(robot);
		robot.setPosition(i,j + 2,k);


		return super.onItemUse(par1ItemStack, par2EntityPlayer, par3World, i, j, k, iFacing, fClickX, fClickY, fClickZ);
	}*/
}
