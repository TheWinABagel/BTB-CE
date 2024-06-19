/**
 * Copyright (c) 2011-2017, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 * <p/>
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL. Please check the contents
 * of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core;

import buildcraft.BuildCraftCore;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.src.Block;
import net.minecraft.init.Blocks;
import net.minecraft.src.World;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

public class SpringPopulate {

    @SubscribeEvent
    public void populate(PopulateChunkEvent.Post event) {
        boolean doGen = TerrainGen.populate(
                event.chunkProvider,
                event.world,
                event.rand,
                event.chunkX,
                event.chunkZ,
                event.hasVillageGenerated,
                PopulateChunkEvent.Populate.EventType.CUSTOM);

        if (!doGen || !BlockSpring.EnumSpring.WATER.canGen) {
            event.setResult(Result.ALLOW);
            return;
        }

        // shift to world coordinates
        int worldX = event.chunkX << 4;
        int worldZ = event.chunkZ << 4;

        doPopulate(event.world, event.rand, worldX, worldZ);
    }

    private void doPopulate(World world, Random random, int x, int z) {
        int dimId = world.provider.dimensionId;
        // No water springs will generate in the Nether or End.
        if (dimId == -1 || dimId == 1) {
            return;
        }

        // A spring will be generated every 40th chunk.
        if (random.nextFloat() > 0.025f) {
            return;
        }

        int posX = x + random.nextInt(16);
        int posZ = z + random.nextInt(16);

        for (int i = 0; i < 5; i++) {
            int candidate = world.getBlockId(posX, i, posZ);

            if (candidate != Block.bedrock.blockID) {
                continue;
            }

            // Handle flat bedrock maps
            int y = i > 0 ? i : i - 1;

            world.setBlock(posX, y + 1, posZ, BuildCraftCore.springBlock.blockID);

            for (int j = y + 2; j < world.getHeight(); j++) {
                if (world.isAirBlock(posX, j, posZ)) {
                    break;
                } else {
                    world.setBlock(posX, j, posZ, Block.waterStill.blockID);
                }
            }

            break;
        }
    }
}
