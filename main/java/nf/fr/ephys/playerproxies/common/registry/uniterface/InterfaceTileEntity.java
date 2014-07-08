package nf.fr.ephys.playerproxies.common.registry.uniterface;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.IFluidHandler;
import nf.fr.ephys.playerproxies.common.tileentity.TileEntityInterface;
import nf.fr.ephys.playerproxies.helpers.BlockHelper;
import org.lwjgl.opengl.GL11;

public class InterfaceTileEntity extends UniversalInterface {
	private TileEntity blockEntity = null;

	protected int[] tileLocation = null;

	public InterfaceTileEntity(TileEntityInterface tileEntity) {
		super(tileEntity);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderInventory(int tickCount, double par1, double par3, double par5, float par7) {
		GL11.glRotatef(tickCount, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);

		nf.fr.ephys.playerproxies.client.renderer.TileEntityInterfaceRenderer.renderBlocksInstance.renderBlockAsItem(Blocks.chest, 0, 1.0F);
	}

	@Override
	public boolean setLink(Object link, EntityPlayer linker) {
		if (link instanceof TileEntity && (link instanceof IInventory || link instanceof IFluidHandler)) {
			this.blockEntity = (TileEntity) link;

			return true;
		}

		return false;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setIntArray("entityLocation", BlockHelper.getCoords(blockEntity));
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.tileLocation = nbt.getIntArray("entityLocation");
	}

	@Override
	public void onBlockUpdate() {}

	@Override
	public IInventory getInventory() {
		return blockEntity instanceof IInventory ? (IInventory) blockEntity : null;
	}

	@Override
	public IFluidHandler getFluidHandler() {
		return blockEntity instanceof IFluidHandler ? (IFluidHandler) blockEntity : null;
	}

	@Override
	public void onTick(int tick) {
		if (getTileEntity().getWorldObj().isRemote) return;

		if (blockEntity == null) {
			if (tileLocation == null || tileLocation.length != 3) {
				this.getTileEntity().unlink();
				return;
			}

			this.blockEntity = this.getTileEntity().getWorldObj().getTileEntity(tileLocation[0], tileLocation[1], tileLocation[2]);
		}

		if (this.blockEntity.isInvalid()) {
			this.blockEntity = null;
			this.getTileEntity().unlink();
		}
	}

	@Override
	public String getName() {
		return BlockHelper.getDisplayName(blockEntity);
	}

	@Override
	public void validate() {}
}
