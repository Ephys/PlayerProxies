package be.ephys.utilitas.common.block;

import be.ephys.utilitas.common.Utilitas;
import be.ephys.utilitas.common.tileentity.TileEntityInterface;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockShinyGlass extends BlockBreakable implements ITileEntityProvider {
    public static boolean interfaceEnabled = true;

    public static final int METADATA_GLASS = 0;
    public static final int METADATA_INTERFACE = 1;

    protected BlockShinyGlass(Material material, boolean transparent) {
        super(material, transparent);

        this.isBlockContainer = true;
    }

    public static void register() {
        BlockShinyGlass instance = new BlockShinyGlass(Material.GLASS, false);
        Utilitas.Blocks.shinyGlass = instance;

        instance.setUnlocalizedName("shiny_glass");
        instance.setSoundType(SoundType.GLASS)
                .setLightLevel(1.0F)
                .setHardness(1.0F)
                .setCreativeTab(Utilitas.creativeTab);

        GameRegistry.registerBlock(instance, MultitemBlock.class, instance.getUnlocalizedName());

        if (interfaceEnabled) {
            GameRegistry.registerTileEntity(TileEntityInterface.class, "universal_interface");
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    @SuppressWarnings("unchecked")
    public void getSubBlocks(Item unknown, CreativeTabs tab, List subItems) {
        subItems.add(new ItemStack(this, 1, METADATA_GLASS));

        if (interfaceEnabled)
            subItems.add(new ItemStack(this, 1, METADATA_INTERFACE));
    }

    public static void registerCraft() {
        GameRegistry.addRecipe(new ItemStack(PlayerProxies.Blocks.baseShineyGlass, 12, METADATA_GLASS), "ggg", "gdg", "ggg", 'd', new ItemStack(Items.diamond), 'g', new ItemStack(Blocks.glass));

        if (interfaceEnabled)
            GameRegistry.addRecipe(new ItemStack(PlayerProxies.Blocks.baseShineyGlass, 1, METADATA_INTERFACE), "dld", "geg", "dgd", 'd', new ItemStack(Items.diamond), 'l', new ItemStack(PlayerProxies.Items.linkFocus), 'g', new ItemStack(PlayerProxies.Blocks.baseShineyGlass, METADATA_GLASS), 'e', new ItemStack(Blocks.ender_chest));
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVectX, float hitVectY, float hitVectZ) {
        int metadata = world.getBlockMetadata(x, y, z);

        switch (metadata) {
            case METADATA_INTERFACE:
                if (!world.isRemote) {
                    if (player.getHeldItem() == null || player.getHeldItem().getItem().equals(PlayerProxies.Items.linkDevice))
                        ((TileEntityInterface) world.getTileEntity(x, y, z)).link(player);
                    else
                        ((TileEntityInterface) world.getTileEntity(x, y, z)).addUpgrade(player.getHeldItem(), player);
                }

                return true;
            default:
                return false;
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        TileEntity te = world.getTileEntity(x, y, z);

        if (te instanceof TileEntityInterface) {
            ((TileEntityInterface) te).onBlockUpdate();
        }

        super.onNeighborBlockChange(world, x, y, z, block);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return metadata == METADATA_INTERFACE;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        switch (metadata) {
            case METADATA_INTERFACE:
                return new TileEntityInterface();
            default:
                return null;
        }
    }

    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int metadata) {
        super.onBlockPreDestroy(world, x, y, z, metadata);

        if (metadata == METADATA_GLASS) return;

        TileEntityInterface te = (TileEntityInterface) world.getTileEntity(x, y, z);

        if (te == null || te.upgrades == null) return;

        for (int i = 0; i < te.upgrades.length; i++) {
            if (te.upgrades[i] != null)
                InventoryHelper.dropItem(te.upgrades[i], world, x, y, z);
        }
    }

    @Override
    public boolean onBlockEventReceived(World paramWorld, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        super.onBlockEventReceived(paramWorld, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
        TileEntity localTileEntity = paramWorld.getTileEntity(paramInt1, paramInt2, paramInt3);

        return localTileEntity != null && localTileEntity.receiveClientEvent(paramInt4, paramInt5);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderBlockPass() {
        return 1;
    }
}
