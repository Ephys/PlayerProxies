package nf.fr.ephys.playerproxies.common.block;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nf.fr.ephys.playerproxies.common.PlayerProxies;
import nf.fr.ephys.playerproxies.helpers.EntityHelper;

import java.util.Random;

/**
 * Upgrade to the Twilight Forest stronghold shield
 *
 * @author ephys
 */
public class BlockHomeShield extends Block {
	public static boolean requiresTwilightForest = true;
	public static boolean requiresSilkTouch = false;

	public static Block tfShield = null;

	private IIcon accessible;
	private IIcon unaccessible;
	private IIcon unbreakable;

	/*
	 * Metadata
	 * 0 - 5 breakable
	 * 6 - 11 unbreakable
	 */
	public BlockHomeShield(Material material) {
		super(material);
	}

	@Override
	public int quantityDropped(Random random) {
		return requiresSilkTouch ? 0 : 1;
	}

	@Override
	protected boolean canSilkHarvest() {
		return requiresSilkTouch;
	}


	@Override
	public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
		return entity instanceof EntityPlayer && !EntityHelper.isFakePlayer((EntityPlayer) entity);
	}

	public static void register() {
		boolean tfExists = Loader.isModLoaded("TwilightForest");

		PlayerProxies.Blocks.homeShield = new BlockHomeShield(Material.rock);
		PlayerProxies.Blocks.homeShield.setResistance(6000000)
			.setBlockUnbreakable()
			.setStepSound(soundTypeMetal)
			.setCreativeTab(PlayerProxies.creativeTab);


		if (tfExists) {
			try {
				Class<?> tfBlocks = Class.forName("twilightforest.block.TFBlocks");
				tfShield = (Block) ReflectionHelper.findField(tfBlocks, "shield").get(null);
			} catch (Exception e) {
				PlayerProxies.getLogger().warn("Error while accessing the TF Shield block.");
				e.printStackTrace();

				if (PlayerProxies.DEV_MODE) {
					PlayerProxies.getLogger().fatal("Exciting loading because an error occured in dev mode.");
					System.exit(1);
				}
			}
		} else {
			requiresTwilightForest = false;
		}

		if (requiresTwilightForest) {
			PlayerProxies.getLogger().info("Twilight Forest found. PP will try to overwrite the TF shield block...");

			PlayerProxies.Blocks.homeShield.setBlockName("TwilightForest:"+tfShield.getUnlocalizedName());
			//GameRegistry.registerBlock(PlayerProxies.Blocks.homeShield, tfShield.getUnlocalizedName());

			/*Block.blocksList[twilightForestShieldID] = null;
			PlayerProxies.Blocks.homeShield = new BlockHomeShield(twilightForestShieldID, Material.rock);

			Item.itemsList[twilightForestShieldID] = null;
			ItemBlock shieldItem = new ItemBlock(twilightForestShieldID - 256);*/
		} else {
			PlayerProxies.Blocks.homeShield.setBlockName("PP_HomeShield");
			//GameRegistry.registerBlock(PlayerProxies.Blocks.homeShield, PlayerProxies.Blocks.homeShield.getUnlocalizedName());
		}

		GameRegistry.registerBlock(PlayerProxies.Blocks.homeShield, PlayerProxies.Blocks.homeShield.getUnlocalizedName());
	}

	public static void registerCraft() {
		if (requiresTwilightForest) return;

		GameRegistry.addRecipe(new ItemStack(PlayerProxies.Blocks.homeShield, 4),
				"isi", "opo", "ioi",
				'i', Items.iron_ingot,
				's', Blocks.stone,
				'o', Blocks.obsidian,
				'p', Items.ender_eye);
	}

	@Override
	public IIcon getIcon(int side, int metadata) {
		if (isUnbreakable(metadata)) {
			if (side == metadata - 6)
				return unaccessible;
		} else if(side == metadata)
			return accessible;

		return unbreakable;
	}

	public static int toggleBreakable(World world, int x, int y, int z, int metadata) {
		if (isUnbreakable(metadata))
			metadata -= 6;
		else
			metadata += 6;

		world.setBlockMetadataWithNotify(x, y, z, metadata, 1 + 2);

		return metadata;
	}

	public static boolean isUnbreakable(World world, int x, int y, int z) {
		return isUnbreakable(world.getBlockMetadata(x, y, z));
	}

	public static boolean isUnbreakable(int metadata) {
		return metadata >= 6;
	}

	public static boolean isSideBreakable(int side, int metadata) {
		return isUnbreakable(metadata) ? side == (metadata - 6) : side == metadata;
	}

	@Override
	public int damageDropped(int metadata) {
		return 0;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemstack) {
		int metadata = BlockPistonBase.determineOrientation(world, x, y, z, entity);

		world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister register) {
		if (requiresTwilightForest) {
			accessible = register.registerIcon("TwilightForest:shield_inside");
			unbreakable = register.registerIcon("TwilightForest:shield_outside");
			unaccessible = register.registerIcon("ephys.pp:strongholdshield_unaccessible");
		} else {
			accessible = register.registerIcon("ephys.pp:homeshield_accessible");
			unbreakable = register.registerIcon("ephys.pp:homeshield_unbreakable");
			unaccessible = register.registerIcon("ephys.pp:homeshield_unaccessible");
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9) {
		if (EntityHelper.isFakePlayer(player)) return false;

		int metadata = world.getBlockMetadata(x, y, z);

		if (isSideBreakable(side, metadata)) {
			if (!world.isRemote)
				toggleBreakable(world, x, y, z, metadata);

			world.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "random.orb", 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);

			return true;
		}

		return false;
	}

	@Override
	public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);

		if (!isUnbreakable(metadata)) {
			MovingObjectPosition mop = EntityHelper.rayTrace(player, 6.0D);

			int facing = mop != null ? mop.sideHit : -1;

			if (facing == metadata) {
				return player.getBreakSpeed(Blocks.stone, false, 0, x, y, z) / 1.5F / 100.0F;
			}
		}

		return super.getPlayerRelativeBlockHardness(player, world, x, y, z);
	}
}