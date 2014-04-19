package nf.fr.ephys.playerproxies.client.core;

import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;
import nf.fr.ephys.playerproxies.client.world.WorldProviderNoVoidFog;
import nf.fr.ephys.playerproxies.common.PlayerProxies;
import nf.fr.ephys.playerproxies.helpers.EnchantmentHelper;
import nf.fr.ephys.playerproxies.helpers.EntityHelper;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class TickHandlerVoidFog implements ITickHandler {
	private HashMap<Integer, WorldProvider> worldProvidersOrigin = new LinkedHashMap<Integer, WorldProvider>(3);
	private HashMap<Integer, WorldProvider> worldProvidersReplaced = new LinkedHashMap<Integer, WorldProvider>(3);

	public WorldProvider generateDimention(int id) {
		World world = DimensionManager.getWorld(id);
		
		PlayerProxies.getLogger().info("generating new provider for world "+world.provider.getDimensionName() + "(" + world.provider.dimensionId + ")");
			
		worldProvidersOrigin.put(world.provider.dimensionId, world.provider);
		
		WorldProviderNoVoidFog newWorldProvider = new WorldProviderNoVoidFog(world.provider);
		
		worldProvidersReplaced.put(world.provider.dimensionId, newWorldProvider);
		
		return newWorldProvider;
	}
	
	public TickHandlerVoidFog() {}
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if (type.equals(EnumSet.of(TickType.CLIENT))) {
			EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
			
			if (player == null || player.inventory == null) return;

			int dimention = player.worldObj.provider.dimensionId;
			if (EnchantmentHelper.hasEnchant(player.inventory.armorItemInSlot(EntityHelper.ARMORSLOT_HELMET), PlayerProxies.Enchantments.noVoidFog)) {
				WorldProvider newProvider = worldProvidersReplaced.get(dimention);
				
				if (newProvider == null)
					newProvider = generateDimention(player.worldObj.provider.dimensionId);

				if (player.worldObj.provider != newProvider) {
					setWorldProvider(newProvider);
				}
			} else {
				WorldProvider newProvider = worldProvidersOrigin.get(dimention);

				if (player.worldObj.provider != newProvider) {
					setWorldProvider(newProvider);
				}
			}
		}
	}
	
	public void setWorldProvider(WorldProvider provider) {
		if (provider == null) return;
		
		World theWorld = FMLClientHandler.instance().getClient().theWorld;
	
		try {
			Field worldProvider = World.class.getField("provider");
			worldProvider.setAccessible(true);

			try {
				worldProvider.set(theWorld, provider);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public String getLabel() {
		return "voidfog remover";
	}
}