package dev.bagel.btb;

import btw.item.tag.Tag;
import buildcraft.BuildCraftTransport;
import buildcraft.transport.ItemPipe;
import net.minecraft.src.ResourceLocation;

public class BuildcraftTags {
    static {
        System.out.println("Loaded Buildcraft Tags");
    }
    public static void init(){}

    public static final Tag itemPipes = Tag.of(new ResourceLocation("buildcraft:item_pipes")).add(BuildCraftTransport.itemPipes.toArray(new ItemPipe[0]));
    public static final Tag fluidPipes = Tag.of(new ResourceLocation("buildcraft:fluid_pipes")).add(BuildCraftTransport.fluidPipes.toArray(new ItemPipe[0]));
    public static final Tag energyPipes = Tag.of(new ResourceLocation("buildcraft:energy_pipes")).add(BuildCraftTransport.energyPipes.toArray(new ItemPipe[0]));
}
