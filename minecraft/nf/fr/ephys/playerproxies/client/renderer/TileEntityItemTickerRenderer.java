package nf.fr.ephys.playerproxies.client.renderer;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import nf.fr.ephys.playerproxies.common.tileentity.TileEntityBlockInterface;
import nf.fr.ephys.playerproxies.common.tileentity.TileEntityItemTicker;

public class TileEntityItemTickerRenderer extends TileEntitySpecialRenderer {
	public static void renderTickerRenderer(TileEntityItemTicker bi, double par1, double par3, double par5, float par7) {
		/*final float scale = 0.4375F;
	
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glTranslatef(0.0F, 0.5F, 0.0F);
		GL11.glScalef(scale, scale, scale);
	
		int invtype = bi.getCurrentInventoryType();
	
		if (invtype == TEBlockInterface.INVTYPE_TE || invtype == TEBlockInterface.INVTYPE_TURTLE || (invtype == TEBlockInterface.INVTYPE_PLAYER && bi.enderMode)) {
			GL11.glRotatef(tick++, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);
	
			Block chest = (bi.enderMode) ? Block.enderChest : Block.chest;

			TileEntityBlockInterfaceRenderer.renderBlocksInstance.renderBlockAsItem(chest, 0, 1.0F);
		} else if(invtype == TEBlockInterface.INVTYPE_PLAYER) {
			World world = Minecraft.getMinecraft().theWorld;
			EntityPlayer player = world.getPlayerEntityByName(bi.getLinkedPlayer());
	
			if(player == null)
				RenderManager.instance.renderEntity(new EntityZombie(Minecraft.getMinecraft().theWorld), 1.0F);
			else {
				if(player != Minecraft.getMinecraft().thePlayer)
					GL11.glTranslatef(0.0F, -1.5F, 0.0F);
				else
					GL11.glRotatef(tick++, 0.0F, 1.0F, 0.0F);
	
				RenderManager.instance.getEntityRenderObject(player).doRender(player, 0.0D, 0.5D, 0.0D, 1.0F, par7);
			}
		}*/
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double par2, double par4, double par6, float par8) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) par2 + 0.5F, (float) par4, (float) par6 + 0.5F);
		renderTickerRenderer((TileEntityItemTicker) tileentity, par2, par4, par6, par8);
		GL11.glPopMatrix();
	}
}