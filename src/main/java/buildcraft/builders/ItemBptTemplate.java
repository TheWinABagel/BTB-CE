package buildcraft.builders;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Icon;

public class ItemBptTemplate extends ItemBptBase {
	private Icon usedTemplate;
	public ItemBptTemplate(int i) {
		super(i);
	}

	@Override
	public Icon getIconFromDamage(int i) {
		if (i == 0)
			return itemIcon;
		else
			return usedTemplate;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		itemIcon = par1IconRegister.registerIcon("buildcraft:template_clean");
		usedTemplate = par1IconRegister.registerIcon("buildcraft:template_used");
	}
}
