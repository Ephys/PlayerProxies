package nf.fr.ephys.playerproxies.helpers;

import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class ChatHelper {
	public static void sendChatMessage(ICommandSender user, String message) {
		if (user.getEntityWorld() == null || !user.getEntityWorld().isRemote)
			user.addChatMessage(new ChatComponentText(message));
	}

	private static final String[] sides = {"down", "up", "north", "south", "west", "east"};
	public static String blockSideName(int side) {
		return StatCollector.translateToLocal("side." + sides[side]);
	}

	public static String getDisplayName(TileEntity te) {
		if (te == null) return "null";

		return getDisplayName(te.getBlockType(), te.getBlockMetadata());
	}

	public static String getDisplayName(Block block) {
		return getDisplayName(block, 0);
	}

	public static String getDisplayName(Block block, int metadata) {
		return new ItemStack(block, 1, metadata).getDisplayName();
	}

	public static String getDisplayName(Fluid fluid) {
		String name = fluid.getLocalizedName(new FluidStack(fluid, 1000));

		if (fluid.canBePlacedInWorld() && name.equals(fluid.getUnlocalizedName())) {
			return getDisplayName(fluid.getBlock());
		}

		return name;
	}
}