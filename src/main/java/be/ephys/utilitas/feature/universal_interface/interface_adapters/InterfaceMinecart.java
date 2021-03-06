package be.ephys.utilitas.feature.universal_interface.interface_adapters;

import be.ephys.utilitas.api.registry.UniversalInterfaceAdapter;
import be.ephys.utilitas.base.helpers.EntityHelper;
import be.ephys.utilitas.base.helpers.NBTHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.UUID;

public class InterfaceMinecart extends UniversalInterfaceAdapter<EntityMinecartContainer> {

    private EntityMinecartContainer minecart;
    private UUID uuid;

    @Override
    @SideOnly(Side.CLIENT)
    public void renderInventory(long tickCount, double x, double y, double z, float tickTime) {
        Block displayTile;
        if (minecart == null) {
            displayTile = Blocks.CHEST;
        } else {
            displayTile = minecart.getDisplayTile().getBlock();
            if (displayTile == Blocks.AIR) {
                displayTile = Blocks.CHEST;
            }
        }

        renderBlock(displayTile, tickCount);
    }

    @Override
    public boolean setLink(EntityMinecartContainer link, EntityPlayer linker) {
        minecart = link;
        uuid = minecart.getPersistentID();
        return true;
    }

    @Override
    public ITextComponent getName() {
        return minecart.getDisplayName();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTHelper.setUuid(nbt, "minecart", uuid);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        uuid = NBTHelper.getUuid(nbt, "minecart", null);
    }

    @Override
    public void onLoad() {
        if (uuid == null) {
            getInterface().unlink();
        }
    }

    @Override
    public void onTick(long tick) {
        if (minecart == null && uuid != null) {
            minecart = (EntityMinecartContainer) EntityHelper.getEntityByUuid(uuid);
        }

        if (!isRemote()) {
            if (minecart == null || minecart.isDead) {
                getInterface().unlink();
            }
        }
    }

    @Override
    public IInventory getInventory() {
        return minecart;
    }

    @Override
    public boolean isNextTo(BlockPos pos) {
        return false;
    }

    @Override
    public int getDimension() {
        return minecart == null ? 0 : minecart.worldObj.provider.getDimension();
    }

    @Override
    public void onBlockUpdate() {
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return false;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return null;
    }
}
