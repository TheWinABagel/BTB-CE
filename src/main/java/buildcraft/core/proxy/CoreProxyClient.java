/**
 * Copyright (c) SpaceToad, 2011
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package buildcraft.core.proxy;

import dev.bagel.btb.mixin.accessors.EntityPlayerAccessor;
import buildcraft.BuildCraftCore;
import buildcraft.api.core.LaserKind;
import buildcraft.core.EntityBlock;
import buildcraft.core.EntityEnergyLaser;
import buildcraft.core.EntityPowerLaser;
import buildcraft.core.EntityRobot;
import buildcraft.core.render.RenderEnergyLaser;
import buildcraft.core.render.RenderEntityBlock;
import buildcraft.core.render.RenderLaser;
import buildcraft.core.render.RenderRobot;
import buildcraft.core.render.RenderingEntityBlocks;
import buildcraft.core.render.RenderingMarkers;
import buildcraft.core.render.RenderingOil;

import java.util.List;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.src.Block;
import net.minecraft.src.Minecraft;
import net.minecraft.src.WorldClient;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet;
import net.minecraft.src.TileEntity;
import net.minecraft.src.ChatMessageComponent;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.World;

public class CoreProxyClient extends CoreProxy {

	public static final CoreProxyClient INSTANCE = new CoreProxyClient();
	/* INSTANCES */
	@Override
	public Object getClient() {
		return Minecraft.getMinecraft();
	}

	@Override
	public World getClientWorld() {
		return Minecraft.getMinecraft().theWorld;
	}

	/* ENTITY HANDLING */
	@Override
	public void removeEntity(Entity entity) {
		super.removeEntity(entity);

		if (isClientWorld(entity.worldObj)) {
			((WorldClient) entity.worldObj).removeEntityFromWorld(entity.entityId);
		}
	}

	/* WRAPPER */
	@SuppressWarnings("rawtypes")
	@Override
	public void feedSubBlocks(int id, CreativeTabs tab, List itemList) {
		if (Block.blocksList[id] == null)
			return;

		Block.blocksList[id].getSubBlocks(id, tab, itemList);
	}

	/* LOCALIZATION */
	@Override
	public String getCurrentLanguage() {
		return Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();
	}

	@Override
	public void addName(Object obj, String s) {
		/*LanguageRegistry.addName(obj, s);*/
	}

	@Override
	public void addLocalization(String s1, String string) {
		/*LanguageRegistry.instance().addStringLocalization(s1, string);*/
	}

	@Override
	public String getItemDisplayName(ItemStack stack) {
		if (Item.itemsList[stack.itemID] == null)
			return "";

		return Item.itemsList[stack.itemID].getItemDisplayName(stack);
	}

	/* GFX */
	@Override
	public void obsidianPipePickup(World world, EntityItem item, TileEntity tile) {
		/*FMLClientHandler.instance().getClient().effectRenderer.addEffect(new TileEntityPickupFX(world, item, tile));*/
	}

	@Override
	public void initializeRendering() {
		BuildCraftCore.blockByEntityModel = RenderingRegistry.getNextAvailableRenderId();
		BuildCraftCore.legacyPipeModel = RenderingRegistry.getNextAvailableRenderId();
		BuildCraftCore.markerModel = RenderingRegistry.getNextAvailableRenderId();
		BuildCraftCore.oilModel = RenderingRegistry.getNextAvailableRenderId();

		RenderingRegistry.registerBlockHandler(new RenderingEntityBlocks());
		RenderingRegistry.registerBlockHandler(BuildCraftCore.legacyPipeModel, new RenderingEntityBlocks());
		RenderingRegistry.registerBlockHandler(new RenderingOil());
		RenderingRegistry.registerBlockHandler(new RenderingMarkers());
	}

	@Override
	public void initializeEntityRendering() {
		RenderingRegistry.registerEntityRenderingHandler(EntityBlock.class, RenderEntityBlock.INSTANCE);
		RenderingRegistry.registerEntityRenderingHandler(EntityPowerLaser.class, new RenderLaser());
		RenderingRegistry.registerEntityRenderingHandler(EntityEnergyLaser.class, new RenderEnergyLaser());
		RenderingRegistry.registerEntityRenderingHandler(EntityRobot.class, new RenderRobot());
	}

	/* NETWORKING */
	@Override
	public void sendToServer(Packet packet) {
		Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
	}

	/* BUILDCRAFT PLAYER */
	@Override
	public String playerName() {
		return ((EntityPlayerAccessor) Minecraft.getMinecraft().thePlayer).getUsername();
	}

	private EntityPlayer createNewPlayer(World world) {
		EntityPlayer player = new EntityPlayer(world, "[BuildCraft]") {
			@Override
			public void sendChatToPlayer(ChatMessageComponent var1) {
			}

			@Override
			public boolean canCommandSenderUseCommand(int var1, String var2) {
				return false;
			}

			@Override
			public ChunkCoordinates getPlayerCoordinates() {
				return null;
			}
		};
		return player;
	}

	@Override
	public EntityPlayer getBuildCraftPlayer(World world) {
		if (CoreProxy.buildCraftPlayer == null) {
			CoreProxy.buildCraftPlayer = createNewPlayer(world);
		}

		return CoreProxy.buildCraftPlayer;
	}

	@Override
	public EntityBlock newEntityBlock(World world, double i, double j,	double k, double iSize, double jSize, double kSize, LaserKind laserKind) {
		EntityBlock eb = super.newEntityBlock(world, i, j, k, iSize, jSize, kSize, laserKind);
		switch (laserKind) {
		case Blue:
			eb.texture = BuildCraftCore.blueLaserTexture;
			break;

		case Red:
			eb.texture = BuildCraftCore.redLaserTexture;
			break;

		case Stripes:
			eb.texture = BuildCraftCore.stripesLaserTexture;
			break;
		}
		return eb;
	}
}
