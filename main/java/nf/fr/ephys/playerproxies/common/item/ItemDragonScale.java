package nf.fr.ephys.playerproxies.common.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import nf.fr.ephys.playerproxies.common.PlayerProxies;
import nf.fr.ephys.playerproxies.helpers.CommandHelper;
import nf.fr.ephys.playerproxies.helpers.MathHelper;

public class ItemDragonScale extends Item {
	public final boolean isIngot;

	public static final int[] RED = new int[] { 180, 0, 0 };
	public static final int[] PURPLE = new int[] { 60, 0, 180 };

	public ItemDragonScale(boolean isIngot) {
		this.isIngot = isIngot;
	}

	public static void register() {
		PlayerProxies.Items.dragonScale = new ItemDragonScale(false);
		PlayerProxies.Items.dragonScale
				.setTextureName("ephys.pp:dragonScale")
				.setUnlocalizedName("PP_DragonScale")
				.setCreativeTab(PlayerProxies.creativeTab);

		GameRegistry.registerItem(PlayerProxies.Items.dragonScale, PlayerProxies.Items.dragonScale.getUnlocalizedName());

		PlayerProxies.Items.dragonScaleIngot = new ItemDragonScale(true);
		PlayerProxies.Items.dragonScaleIngot
				.setTextureName("iron_ingot")
				.setUnlocalizedName("PP_DragonScaleIngot")
				.setCreativeTab(PlayerProxies.creativeTab);

		GameRegistry.registerItem(PlayerProxies.Items.dragonScaleIngot, PlayerProxies.Items.dragonScaleIngot.getUnlocalizedName());
	}

	public static void registerCraft() {
		GameRegistry.addShapelessRecipe(new ItemStack(PlayerProxies.Items.dragonScaleIngot), PlayerProxies.Items.dragonScale, Items.iron_ingot, Items.ender_pearl);
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int par2) {
		double percent = (Minecraft.getMinecraft().theWorld.getTotalWorldTime() % (Math.PI * 100)) / 100;
		float sin = (float) Math.sin(percent);

		int r = MathHelper.gradientRGB(RED[0], PURPLE[0], sin) << 16;
		int g = MathHelper.gradientRGB(RED[1], PURPLE[1], sin) << 8;
		int b = MathHelper.gradientRGB(RED[2], PURPLE[2], sin);

		return r + g + b;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10) {
		// the dragonSpawner block is bugued, for now. Don't use it
		if (!PlayerProxies.DEV_MODE) return false;

		if (isIngot) return false;

		Block block = world.getBlock(x, y, z);

		if (!block.equals(Blocks.mob_spawner)) return false;

		if (world.isRemote) return true;

		World worldEnd = MinecraftServer.getServer().worldServerForDimension(1);
		Block endBlock = worldEnd.getBlock(0, 2, 0);

		if (endBlock.equals(PlayerProxies.Blocks.dragonSpawner)) {
			CommandHelper.sendChatMessage(player, "There is already a spawn in progress");
		} else {
			stack.stackSize--;

			world.setBlockToAir(x, y, z);

			if (player.worldObj.provider.dimensionId != 1)
				player.travelToDimension(1);

			worldEnd.setBlock(0, 2, 0, PlayerProxies.Blocks.dragonSpawner);

			CommandHelper.sendChatMessage(player, "An enderdragon is approaching");
		}

		return true;
	}
}