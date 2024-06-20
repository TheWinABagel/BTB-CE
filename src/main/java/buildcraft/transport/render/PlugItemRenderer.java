package buildcraft.transport.render;

import buildcraft.BuildCraftTransport;
import buildcraft.transport.PipeIconProvider;
import net.minecraft.src.Block;
import net.minecraft.src.renderer.RenderBlocks;
import net.minecraft.src.renderer.Tessellator;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Icon;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON;

public class PlugItemRenderer implements IItemRenderer {

	private void renderPlugItem(RenderBlocks render, ItemStack item, float translateX, float translateY, float translateZ) {
		// Render StructurePipe
		Block block = BuildCraftTransport.genericPipeBlock;
		Tessellator tessellator = Tessellator.instance;

		block = BuildCraftTransport.genericPipeBlock;
		Icon textureID = BuildCraftTransport.instance.pipeIconProvider.getIcon(PipeIconProvider.TYPE.PipeStructureCobblestone.ordinal()); // Structure pipe

		block.setBlockBounds(0.25F, 0.25F, 0.25F, 0.75F, 0.375F, 0.75F);
		block.setBlockBoundsForItemRender();
		render.setRenderBoundsFromBlock(block);
		GL11.glTranslatef(translateX, translateY, translateZ + 0.25F);

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -0F, 0.0F);
		render.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, textureID);
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		render.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, textureID);
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1F);
		render.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, textureID);
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		render.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, textureID);
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(-1F, 0.0F, 0.0F);
		render.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, textureID);
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		render.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, textureID);
		tessellator.draw();
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		switch (type) {
			case ENTITY:
				return true;
			case EQUIPPED:
				return true;
			case EQUIPPED_FIRST_PERSON:
				return true;
			case INVENTORY:
				return true;
			default:
				return false;
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return helper != ItemRendererHelper.BLOCK_3D;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		switch (type) {
			case ENTITY:
				GL11.glScalef(0.50F, 0.50F, 0.50F);
				renderPlugItem((RenderBlocks) data[0], item, -0.6F, 0f, -0.6F);
				break;
			case EQUIPPED:
			case EQUIPPED_FIRST_PERSON:
				GL11.glRotatef(70, 0, 0, 1F);
				GL11.glRotatef(-55, 1, 0, 0);
				GL11.glScalef(2F, 2F, 2F);
				GL11.glTranslatef(0, -0.6F, -0.4F);
				renderPlugItem((RenderBlocks) data[0], item, 0F, 0F, 0f);
				break;
			case INVENTORY:
				GL11.glScalef(1.1F, 1.1F, 1.1F);
				renderPlugItem((RenderBlocks) data[0], item, -0.3f, -0.35f, -0.7f);
				break;
			default:
		}
	}
}
