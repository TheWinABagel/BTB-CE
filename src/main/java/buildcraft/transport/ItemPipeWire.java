package buildcraft.transport;

import buildcraft.api.transport.PipeWire;
import buildcraft.core.ItemBuildCraft;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.IconRegister;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Icon;

import java.util.List;

public class ItemPipeWire extends ItemBuildCraft {

	private Icon[] icons;

	public ItemPipeWire(int i) {
		super(i);
		setHasSubtypes(true);
		setMaxDamage(0);
		setPassSneakClick(true);
		setUnlocalizedName("pipeWire");
	}

	@Override
	public Icon getIconFromDamage(int damage) {
		return icons[damage];
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "item." + PipeWire.fromOrdinal(stack.getItemDamage()).getTag();
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	@Environment(EnvType.CLIENT)
	public void getSubItems(int id, CreativeTabs tab, List itemList) {
		for (PipeWire pipeWire : PipeWire.VALUES) {
			itemList.add(pipeWire.getStack());
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		icons = new Icon[PipeWire.VALUES.length];
		for (PipeWire pipeWire : PipeWire.VALUES) {
			icons[pipeWire.ordinal()] = par1IconRegister.registerIcon("buildcraft:" + pipeWire.getTag());
		}
	}

/*	public void registerItemStacks() {
		for (PipeWire pipeWire : PipeWire.VALUES) {
			GameRegistry.registerCustomItemStack(pipeWire.getTag(), pipeWire.getStack());
		}
	}*/
}
