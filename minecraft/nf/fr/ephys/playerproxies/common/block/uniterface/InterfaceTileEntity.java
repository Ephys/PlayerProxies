package nf.fr.ephys.playerproxies.common.block.uniterface;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.IFluidHandler;
import nf.fr.ephys.playerproxies.client.renderer.TileEntityInterfaceRenderer;
import nf.fr.ephys.playerproxies.common.tileentity.TileEntityInterface;
import nf.fr.ephys.playerproxies.helpers.BlockHelper;

public class InterfaceTileEntity extends UniversalInterface {
	private TileEntity blockEntity = null;

	protected int[] tileLocation = null;
	
	public InterfaceTileEntity(TileEntityInterface tileEntity) {
		super(tileEntity);
	}

	@Override
	public void renderInventory(int tickCount, double par1, double par3, double par5, float par7) {
		GL11.glRotatef(tickCount, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);

		TileEntityInterfaceRenderer.renderBlocksInstance.renderBlockAsItem(Block.chest, 0, 1.0F);
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
	public void onBlockUpdate(int side) {}

	@Override
	public IInventory getInventory() {
		return blockEntity instanceof IInventory ? (IInventory) blockEntity : null;
	}

	@Override
	public IFluidHandler getFluidHandler() {
		return blockEntity instanceof IFluidHandler ? (IFluidHandler) blockEntity : null;
	}

	@Override
	public void onTick() {
		if (getTileEntity().worldObj.isRemote) return;

		if (blockEntity == null) {
			if (tileLocation == null || tileLocation.length != 3) {
				this.getTileEntity().unlink();
				return;
			}

			this.blockEntity = this.getTileEntity().getWorldObj().getBlockTileEntity(tileLocation[0], tileLocation[1], tileLocation[2]);
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
