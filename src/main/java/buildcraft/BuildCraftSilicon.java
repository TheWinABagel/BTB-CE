/**
 * BuildCraft is open-source. It is distributed under the terms of the
 * BuildCraft Open Source License. It grants rights to read, modify, compile or
 * run the code. It does *NOT* grant the right to redistribute this software or
 * its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */
package buildcraft;

import btw.util.color.Color;
import buildcraft.api.bptblocks.BptBlockInventory;
import buildcraft.api.bptblocks.BptBlockRotateMeta;
import buildcraft.api.recipes.BuildcraftRecipes;
import buildcraft.api.transport.PipeWire;
import buildcraft.core.DefaultProps;
import buildcraft.core.InterModComms;
import buildcraft.core.Version;
import buildcraft.core.proxy.CoreProxy;
import buildcraft.silicon.*;
import buildcraft.silicon.ItemRedstoneChipset.Chipset;
import buildcraft.silicon.network.PacketHandlerSilicon;
import buildcraft.silicon.recipes.GateExpansionRecipe;
import buildcraft.silicon.recipes.GateLogicSwapRecipe;
import buildcraft.transport.gates.GateDefinition.GateLogic;
import buildcraft.transport.gates.GateDefinition.GateMaterial;
import buildcraft.transport.gates.GateExpansionPulsar;
import buildcraft.transport.gates.GateExpansionRedstoneFader;
import buildcraft.transport.gates.GateExpansionTimer;
import buildcraft.transport.gates.ItemGate;
import cpw.mods.fml.common.network.NetworkRegistry;
import emi.shims.java.net.minecraft.util.DyeColor;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemDye;
import net.minecraft.src.ItemStack;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class BuildCraftSilicon extends BuildcraftAddon {

	public static ItemRedstoneChipset redstoneChipset;
	public static BlockLaser laserBlock;
	public static BlockLaserTable assemblyTableBlock;
	public static BlockLaserTable advancedCraftingTableBlock;
	public static BlockLaserTable integrationTableBlock;

	public static BuildCraftSilicon instance = new BuildCraftSilicon();

	public BuildCraftSilicon() {
		super("bcsilicon");
	}

	@Override
	public void preInitialize() {
/*		Property laserId = BuildCraftCore.mainConfiguration.getBlock("laser.id", DefaultProps.LASER_ID);

		Property assemblyTableId = BuildCraftCore.mainConfiguration.getBlock("assemblyTable.id", DefaultProps.ASSEMBLY_TABLE_ID);

		Property redstoneChipsetId = BuildCraftCore.mainConfiguration.getItem("redstoneChipset.id", DefaultProps.REDSTONE_CHIPSET);

		BuildCraftCore.mainConfiguration.save();*/

		laserBlock = new BlockLaser(DefaultProps.LASER_ID);
		CoreProxy.getProxy().addName(laserBlock.setUnlocalizedName("laserBlock"), "Laser");
		CoreProxy.getProxy().registerBlock(laserBlock);

		assemblyTableBlock = new BlockLaserTableAssembly(DefaultProps.ASSEMBLY_TABLE_ID);
		assemblyTableBlock.setUnlocalizedName("assemblyTableBlock");
		advancedCraftingTableBlock = new BlockLaserTableAdvancedCrafting(DefaultProps.ADVANCED_CRAFTING_TABLE_ID);
		advancedCraftingTableBlock.setUnlocalizedName("assemblyWorkbenchBlock");
		integrationTableBlock = new BlockLaserTableIntegration(DefaultProps.INTEGRATION_TABLE_ID);
		integrationTableBlock.setUnlocalizedName("autoWorkbenchBlock");

		CoreProxy.getProxy().registerBlock(assemblyTableBlock, ItemLaserTable.class);

/*		LanguageRegistry.addName(new ItemStack(assemblyTableBlock, 0, 0), "Assembly Table");
		LanguageRegistry.addName(new ItemStack(assemblyTableBlock, 0, 1), "Advanced Crafting Table");
		LanguageRegistry.addName(new ItemStack(assemblyTableBlock, 0, 2), "Integration Table");*/

		redstoneChipset = new ItemRedstoneChipset(DefaultProps.REDSTONE_CHIPSET);
		redstoneChipset.setUnlocalizedName("redstoneChipset");
		CoreProxy.getProxy().registerItem(redstoneChipset);
		redstoneChipset.registerItemStacks();
	}

	@Override
	public void initialize() {
		NetworkRegistry.instance().registerGuiHandler("bcsilicon", new GuiHandler());
		CoreProxy.getProxy().registerTileEntity(TileLaser.class, "net.minecraft.src.buildcraft.factory.TileLaser");
		CoreProxy.getProxy().registerTileEntity(TileAssemblyTable.class, "net.minecraft.src.buildcraft.factory.TileAssemblyTable");
		CoreProxy.getProxy().registerTileEntity(TileAdvancedCraftingTable.class, "net.minecraft.src.buildcraft.factory.TileAssemblyAdvancedWorkbench");
		CoreProxy.getProxy().registerTileEntity(TileIntegrationTable.class, "net.minecraft.src.buildcraft.factory.TileIntegrationTable");

		new BptBlockRotateMeta(laserBlock.blockID, new int[]{2, 5, 3, 4}, true);
		new BptBlockInventory(assemblyTableBlock.blockID);

		if (BuildCraftCore.loadDefaultRecipes) {
			loadRecipes();
		}

		SiliconProxy.proxy.registerRenderers();
	}

	public static void loadRecipes() {

		// TABLES
		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(laserBlock),
				"ORR",
				"DDR",
				"ORR",
				'O', Block.obsidian,
				'R', Item.redstone,
				'D', Item.diamond);

		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(assemblyTableBlock, 1, 0),
				"ORO",
				"ODO",
				"OGO",
				'O', Block.obsidian,
				'R', Item.redstone,
				'D', Item.diamond,
				'G', BuildCraftCore.diamondGearItem);

		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(assemblyTableBlock, 1, 1),
				"OWO",
				"OCO",
				"ORO",
				'O', Block.obsidian,
				'W', Block.workbench,
				'C', Block.chest,
				'R', new ItemStack(redstoneChipset, 1, 0));

		CoreProxy.getProxy().addCraftingRecipe(new ItemStack(assemblyTableBlock, 1, 2),
				"ORO",
				"OCO",
				"OGO",
				'O', Block.obsidian,
				'R', Item.redstone,
				'C', new ItemStack(redstoneChipset, 1, 0),
				'G', BuildCraftCore.diamondGearItem);

		// PIPE WIRE
		BuildcraftRecipes.assemblyTable.addRecipe(500, PipeWire.RED.getStack(8), new ItemStack(Item.dyePowder, 1, Color.RED.colorID), 1, Item.redstone, Item.ingotIron);
		BuildcraftRecipes.assemblyTable.addRecipe(500, PipeWire.BLUE.getStack(8), new ItemStack(Item.dyePowder, 1, Color.BLUE.colorID), 1, Item.redstone, Item.ingotIron);
		BuildcraftRecipes.assemblyTable.addRecipe(500, PipeWire.GREEN.getStack(8), new ItemStack(Item.dyePowder, 1, Color.GREEN.colorID), 1, Item.redstone, Item.ingotIron);
		BuildcraftRecipes.assemblyTable.addRecipe(500, PipeWire.YELLOW.getStack(8), new ItemStack(Item.dyePowder, 1, Color.YELLOW.colorID), 1, Item.redstone, Item.ingotIron);

		// CHIPSETS
		BuildcraftRecipes.assemblyTable.addRecipe(10000, Chipset.RED.getStack(), Item.redstone);
		BuildcraftRecipes.assemblyTable.addRecipe(20000, Chipset.IRON.getStack(), Item.redstone, Item.ingotIron);
		BuildcraftRecipes.assemblyTable.addRecipe(40000, Chipset.GOLD.getStack(), Item.redstone, Item.ingotGold);
		BuildcraftRecipes.assemblyTable.addRecipe(80000, Chipset.DIAMOND.getStack(), Item.redstone, Item.diamond);
		BuildcraftRecipes.assemblyTable.addRecipe(40000, Chipset.PULSATING.getStack(2), Item.redstone, Item.enderPearl);
		BuildcraftRecipes.assemblyTable.addRecipe(60000, Chipset.QUARTZ.getStack(), Item.redstone, Item.netherQuartz);
		BuildcraftRecipes.assemblyTable.addRecipe(60000, Chipset.COMP.getStack(), Item.redstone, Item.comparator);

		// GATES		
		BuildcraftRecipes.assemblyTable.addRecipe(10000, ItemGate.makeGateItem(GateMaterial.REDSTONE, GateLogic.AND), Chipset.RED.getStack(), PipeWire.RED.getStack());

		addGateRecipe(20000, GateMaterial.IRON, Chipset.IRON, PipeWire.RED, PipeWire.BLUE);
		addGateRecipe(40000, GateMaterial.GOLD, Chipset.GOLD, PipeWire.RED, PipeWire.BLUE, PipeWire.YELLOW);
		addGateRecipe(80000, GateMaterial.DIAMOND, Chipset.DIAMOND, PipeWire.RED, PipeWire.BLUE, PipeWire.YELLOW, PipeWire.GREEN);

		// REVERSAL RECIPES
		EnumSet<GateMaterial> materials = EnumSet.allOf(GateMaterial.class);
		materials.remove(GateMaterial.REDSTONE);
		for (GateMaterial material : materials) {
			BuildcraftRecipes.integrationTable.addRecipe(new GateLogicSwapRecipe(material, GateLogic.AND, GateLogic.OR));
			BuildcraftRecipes.integrationTable.addRecipe(new GateLogicSwapRecipe(material, GateLogic.OR, GateLogic.AND));
		}

		// EXPANSIONS
		BuildcraftRecipes.integrationTable.addRecipe(new GateExpansionRecipe(GateExpansionPulsar.INSTANCE, Chipset.PULSATING.getStack()));
		BuildcraftRecipes.integrationTable.addRecipe(new GateExpansionRecipe(GateExpansionTimer.INSTANCE, Chipset.QUARTZ.getStack()));
		BuildcraftRecipes.integrationTable.addRecipe(new GateExpansionRecipe(GateExpansionRedstoneFader.INSTANCE, Chipset.COMP.getStack()));
	}

	private static void addGateRecipe(double energyCost, GateMaterial material, Chipset chipset, PipeWire... pipeWire) {
		List temp = new ArrayList();
		temp.add(chipset.getStack());
		for (PipeWire wire : pipeWire) {
			temp.add(wire.getStack());
		}
		Object[] inputs = temp.toArray();
		BuildcraftRecipes.assemblyTable.addRecipe(energyCost, ItemGate.makeGateItem(material, GateLogic.AND), inputs);
		BuildcraftRecipes.assemblyTable.addRecipe(energyCost, ItemGate.makeGateItem(material, GateLogic.OR), inputs);
	}

/*	@EventHandler
	public void processIMCRequests(FMLInterModComms.IMCEvent event) {
		InterModComms.processIMC(event);
	}*/
}
