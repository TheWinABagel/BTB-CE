package buildcraft.core.builders;

import buildcraft.api.blueprints.IBuilderContext;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

import java.util.List;

public class BuilderItemMetaPair {

    public Item item;
    public int meta;
    public int position = 0;

    public BuilderItemMetaPair(ItemStack stack) {
        if (stack != null) {
            this.item = stack.getItem();
            this.meta = stack.getItemDamage();
        } else {
            this.item = Item.itemsList[0];
            this.meta = 0;
        }
    }

    public BuilderItemMetaPair(IBuilderContext context, BuildingSlotBlock block) {
        this(findStack(context, block));
    }

    private static ItemStack findStack(IBuilderContext context, BuildingSlotBlock block) {
        List<ItemStack> s = block.getRequirements(context);
        return s.size() > 0 ? s.get(0) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BuilderItemMetaPair) {
            BuilderItemMetaPair imp = (BuilderItemMetaPair) o;
            return imp.item == item && imp.meta == meta;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return item.itemID * 17 + meta;
    }
}
