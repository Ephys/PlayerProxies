package nf.fr.ephys.playerproxies.common.block.uniterface;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.fluids.IFluidHandler;
import nf.fr.ephys.playerproxies.client.renderer.TileEntityInterfaceRenderer;
import nf.fr.ephys.playerproxies.common.tileentity.TileEntityInterface;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.shared.turtle.blocks.ITurtleTile;

public class InterfaceTurtle extends UniversalInterface {
	private TileEntity tileEntity = null;
	private ITurtleAccess turtleAccess = null;
	private int[] tileLocation;

	public InterfaceTurtle(TileEntityInterface tileEntity) {
		super(tileEntity);
	}

	@Override
	public IFluidHandler getFluidHandler() {
		return null;
	}

	@Override
	public IInventory getInventory() {
		return turtleAccess == null ? null : turtleAccess.getInventory();
	}
	
	@Override
	public boolean setLink(Object link, EntityPlayer linker) {
		if (link instanceof ITurtleTile) {
			this.turtleAccess = ((ITurtleTile) link).getAccess();
			

			return true;
		}

		return false;
	}

	@Override
	public void onTick() {
		if (turtleAccess == null) {
			TileEntity turtle = this.getTileEntity().getWorldObj().getBlockTileEntity(tileLocation[0], tileLocation[1], tileLocation[2]);
			
			if (turtle instanceof ITurtleTile) {
				this.turtleAccess = ((ITurtleTile) turtle).getAccess();

				ChunkCoordinates pos = turtleAccess.getPosition();
				tileLocation[0] = pos.posX;
				tileLocation[1] = pos.posY;
				tileLocation[2] = pos.posZ;
			} else {
				this.getTileEntity().unlink();
				return;
			}
		}

		if (tileEntity == null || tileEntity.isInvalid()) {
			if (turtleAccess == null) {
				this.getTileEntity().unlink();
				return;
			}

			ChunkCoordinates turtlePos = turtleAccess.getPosition();
			tileEntity = turtleAccess.getWorld().getBlockTileEntity(turtlePos.posX, turtlePos.posY, turtlePos.posZ);
			
			if (tileEntity == null || tileEntity.isInvalid()) {
				this.getTileEntity().unlink();
			}
		}
	}

	@Override
	public void renderInventory(int tickCount, double par1, double par3, double par5, float par7) {
		GL11.glRotatef(tickCount, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);

		TileEntityInterfaceRenderer.renderBlocksInstance.renderBlockAsItem(Block.chest, 0, 1.0F);
	}

	@Override
	public String getName() {
		return "turtle";
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		ChunkCoordinates pos = turtleAccess.getPosition();

		int[] posArray = {pos.posX, pos.posY, pos.posZ};
		nbt.setIntArray("position", posArray);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.tileLocation = nbt.getIntArray("position");
	}

	@Override
	public void validate() {}
	
	@Override
	public void onBlockUpdate(int side) {}
}
