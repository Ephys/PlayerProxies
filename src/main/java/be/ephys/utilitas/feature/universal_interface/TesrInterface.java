package be.ephys.utilitas.feature.universal_interface;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TesrInterface extends TileEntitySpecialRenderer<TileEntityInterface> {

    @Override
    public void renderTileEntityAt(TileEntityInterface tile, double x, double y, double z, float partialTicks, int destroyStage) {
        super.renderTileEntityAt(tile, x, y, z, partialTicks, destroyStage);

        GL11.glPushMatrix();
//        GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
//        GL11.glTranslatef((float) x, (float) y, (float) z);
        GL11.glTranslated(x, y, z);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);

        final float scale = 0.4375F;
        GL11.glScalef(scale, scale, scale);

        long tick = FMLCommonHandler.instance().getMinecraftServerInstance().getTickCounter();

        tile.getAdapter().renderInventory(tick, x, y, z, partialTicks);
        GL11.glPopMatrix();
    }
}
