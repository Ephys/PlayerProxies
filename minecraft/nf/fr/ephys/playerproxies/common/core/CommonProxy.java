package nf.fr.ephys.playerproxies.common.core;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import nf.fr.ephys.playerproxies.common.PlayerProxies;
import nf.fr.ephys.playerproxies.common.block.BlockBiomeReplicator;
import nf.fr.ephys.playerproxies.common.block.BlockBiomeScanner;
import nf.fr.ephys.playerproxies.common.block.BlockEtherealGlass;
import nf.fr.ephys.playerproxies.common.block.BlockGravitationalField;
import nf.fr.ephys.playerproxies.common.block.BlockHardenedStone;
import nf.fr.ephys.playerproxies.common.block.BlockInterface;
import nf.fr.ephys.playerproxies.common.block.BlockItemTicker;
import nf.fr.ephys.playerproxies.common.block.BlockParticleGenerator;
import nf.fr.ephys.playerproxies.common.block.BlockProximitySensor;
import nf.fr.ephys.playerproxies.common.block.BlockSpawnerLoader;
import nf.fr.ephys.playerproxies.common.block.BlockToughwoodPlank;
import nf.fr.ephys.playerproxies.common.entity.Ghost;
import nf.fr.ephys.playerproxies.common.item.ItemBiomeStorage;
import nf.fr.ephys.playerproxies.common.item.ItemDebug;
import nf.fr.ephys.playerproxies.common.item.ItemLinkFocus;
import nf.fr.ephys.playerproxies.common.item.ItemLinker;
import nf.fr.ephys.playerproxies.common.openperipheral.AdaptorGravitationalField;
import nf.fr.ephys.playerproxies.common.tileentity.TileEntityGravitationalField;
import nf.fr.ephys.playerproxies.common.tileentity.TileEntityInterface;
import nf.fr.ephys.playerproxies.common.tileentity.TileEntitySpawnerLoader;
import nf.fr.ephys.playerproxies.common.tileentity.TileEntityBiomeReplicator;
import nf.fr.ephys.playerproxies.common.tileentity.TileEntityBiomeScanner;
import nf.fr.ephys.playerproxies.common.tileentity.TileEntityItemTicker;
import nf.fr.ephys.playerproxies.common.tileentity.TileEntityProximitySensor;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import dan200.computer.api.ComputerCraftAPI;
import dan200.turtle.api.ITurtleUpgrade;
import dan200.turtle.api.TurtleAPI;

public class CommonProxy {
	public void initMod() {
		registerBlocks();
		registerItems();
		registerEntities();
		registerHandlers();
	}

	private void registerBlocks() {
		PlayerProxies.blockInterface = new BlockInterface();
		PlayerProxies.blockInterface.setUnlocalizedName("PP_UniversalInterface");
		GameRegistry.registerBlock(PlayerProxies.blockInterface, "PP_UniversalInterface");
		GameRegistry.registerTileEntity(TileEntityInterface.class, "PP_UniversalInterface");
		LanguageRegistry.instance().addName(PlayerProxies.blockInterface, "Universal Interface");
		
		PlayerProxies.blockSpawnerLoader = new BlockSpawnerLoader();
		PlayerProxies.blockSpawnerLoader.setUnlocalizedName("PP_SpawnerLoader");
		GameRegistry.registerBlock(PlayerProxies.blockSpawnerLoader, "PP_SpawnerLoader");
		GameRegistry.registerTileEntity(TileEntitySpawnerLoader.class, "PP_SpawnerLoader");
		LanguageRegistry.instance().addName(PlayerProxies.blockSpawnerLoader, "Ghost Stabilizer");

		PlayerProxies.blockHardenedStone = new BlockHardenedStone();
		PlayerProxies.blockHardenedStone.setUnlocalizedName("PP_HardenedStone");
		GameRegistry.registerBlock(PlayerProxies.blockHardenedStone, "PP_HardenedStone");
		LanguageRegistry.instance().addName(PlayerProxies.blockHardenedStone, "Hardened Stone");
		
		PlayerProxies.blockParticleGenerator = new BlockParticleGenerator();
		PlayerProxies.blockParticleGenerator.setUnlocalizedName("PP_ParticleGenerator");
		GameRegistry.registerBlock(PlayerProxies.blockParticleGenerator, "PP_ParticleGenerator");
		LanguageRegistry.instance().addName(PlayerProxies.blockParticleGenerator, "Particle Generator");

		PlayerProxies.blockEtherealGlass = new BlockEtherealGlass();
		PlayerProxies.blockEtherealGlass.setUnlocalizedName("PP_EtherealGlass");
		GameRegistry.registerBlock(PlayerProxies.blockEtherealGlass, "PP_EtherealGlass");
		LanguageRegistry.instance().addName(PlayerProxies.blockEtherealGlass, "Ethereal Glass");
		
		PlayerProxies.blockProximitySensor = new BlockProximitySensor();
		PlayerProxies.blockProximitySensor.setUnlocalizedName("PP_ProximitySensor");
		GameRegistry.registerBlock(PlayerProxies.blockProximitySensor, "PP_ProximitySensor");
		GameRegistry.registerTileEntity(TileEntityProximitySensor.class, "PP_ProximitySensor");
		LanguageRegistry.instance().addName(PlayerProxies.blockProximitySensor, "Proximity Sensor");
		
		PlayerProxies.blockBiomeChanger = new BlockBiomeReplicator();
		PlayerProxies.blockBiomeChanger.setUnlocalizedName("PP_BiomeChanger");
		GameRegistry.registerBlock(PlayerProxies.blockBiomeChanger, "PP_BiomeChanger");
		GameRegistry.registerTileEntity(TileEntityBiomeReplicator.class, "PP_BiomeChanger");
		LanguageRegistry.instance().addName(PlayerProxies.blockBiomeChanger, "Biome transmuter");
		
		PlayerProxies.blockBiomeScanner = new BlockBiomeScanner();
		PlayerProxies.blockBiomeScanner.setUnlocalizedName("PP_BiomeScanner");
		GameRegistry.registerBlock(PlayerProxies.blockBiomeScanner, "PP_BiomeScanner");
		GameRegistry.registerTileEntity(TileEntityBiomeScanner.class, "PP_BiomeScanner");
		LanguageRegistry.instance().addName(PlayerProxies.blockBiomeScanner, "Biome scanner");
		
		PlayerProxies.blockToughwoodPlank = new BlockToughwoodPlank();
		PlayerProxies.blockToughwoodPlank.setUnlocalizedName("PP_ToughwoodPlank");
		GameRegistry.registerBlock(PlayerProxies.blockToughwoodPlank, "PP_ToughwoodPlank");
		LanguageRegistry.instance().addName(PlayerProxies.blockToughwoodPlank, "Toughwood");
		
		PlayerProxies.blockItemTicker = new BlockItemTicker();
		PlayerProxies.blockItemTicker.setUnlocalizedName("PP_ItemActivator");
		GameRegistry.registerBlock(PlayerProxies.blockItemTicker, "PP_ItemActivator");
		GameRegistry.registerTileEntity(TileEntityItemTicker.class, "PP_ItemActivator");
		LanguageRegistry.instance().addName(PlayerProxies.blockItemTicker, "Sylladex");
		
		PlayerProxies.blockGravitationalField = new BlockGravitationalField();
		PlayerProxies.blockGravitationalField.setUnlocalizedName("PP_GravitationalField");
		GameRegistry.registerBlock(PlayerProxies.blockGravitationalField, "PP_GravitationalField");
		GameRegistry.registerTileEntity(TileEntityGravitationalField.class, "PP_GravitationalField");
		LanguageRegistry.instance().addName(PlayerProxies.blockGravitationalField, "Gravitational Field Handler");
	}

	private void registerItems() {
		PlayerProxies.itemLinker = new ItemLinker();
		PlayerProxies.itemLinker.setUnlocalizedName("PP_LinkWand");
		MinecraftForge.EVENT_BUS.register(PlayerProxies.itemLinker);
		GameRegistry.registerItem(PlayerProxies.itemLinker, "PP_LinkWand");
		LanguageRegistry.instance().addName(PlayerProxies.itemLinker,
				"Linking wand");

		PlayerProxies.itemLinkFocus = new ItemLinkFocus();
		PlayerProxies.itemLinkFocus.setUnlocalizedName("PP_LinkFocus");
		GameRegistry.registerItem(PlayerProxies.itemLinkFocus, "PP_LinkFocus");
		LanguageRegistry.instance().addName(PlayerProxies.itemLinkFocus,
				"Link focus");

		PlayerProxies.itemBiomeStorage = new ItemBiomeStorage();
		PlayerProxies.itemBiomeStorage.setUnlocalizedName("PP_BiomeStorage");
		GameRegistry.registerItem(PlayerProxies.itemBiomeStorage, "PP_BiomeStorage");
		LanguageRegistry.instance().addName(PlayerProxies.itemBiomeStorage,
				"Biome signature handler");
		
		if (PlayerProxies.DEV_MODE) {
			ItemDebug itemDebug = new ItemDebug();
			itemDebug.setUnlocalizedName("PP_Debug");
			GameRegistry.registerItem(itemDebug, "PP_Debug");
			LanguageRegistry.instance().addName(itemDebug,
					"Debug tool -- REPORT THIS A BUG");
		}
	}

	public void registerCrafts() {
		GameRegistry.addRecipe(new ItemStack(PlayerProxies.itemLinkFocus),
				"ipi", "qeq", "ipi", 
				'e', new ItemStack(Item.emerald), 
				'p', new ItemStack(Item.enderPearl), 
				'q', new ItemStack(Item.eyeOfEnder), 
				'i', new ItemStack(Item.blazePowder));

		GameRegistry.addRecipe(new ItemStack(PlayerProxies.itemLinker), 
				" il", " si", "s  ", 
				'l', new ItemStack(PlayerProxies.itemLinkFocus),
				'i', new ItemStack(Item.ingotIron), 
				's', new ItemStack(Item.stick));

		GameRegistry.addRecipe(new ItemStack(PlayerProxies.blockEtherealGlass, 24),
				"ggg", "gdg", "ggg", 
				'd', new ItemStack(Item.diamond), 
				'g', new ItemStack(Block.glass));

		GameRegistry.addRecipe(new ItemStack(PlayerProxies.blockInterface),
				"dld", "geg", "dgd", 
				'd', new ItemStack(Item.diamond), 
				'l', new ItemStack(PlayerProxies.itemLinkFocus), 
				'g', new ItemStack(PlayerProxies.blockEtherealGlass), 'e', new ItemStack(Block.enderChest));

		if(!Loader.isModLoaded("IC2")) {
			GameRegistry.addRecipe(new ItemStack(PlayerProxies.blockHardenedStone, 6),
				"ioi", "oso", "ioi", 
				'i', new ItemStack(Item.ingotIron), 
				's', new ItemStack(Block.stone), 
				'o', new ItemStack(Block.obsidian));
		} else {
			GameRegistry.addRecipe(new ItemStack(PlayerProxies.blockHardenedStone, 8),
				"ioi", "oso", "ioi", 
				'i', ic2.api.item.Items.getItem("advancedAlloy"), 
				's', ic2.api.item.Items.getItem("reinforcedStone"), 
				'o', new ItemStack(Block.obsidian));
		}

		GameRegistry.addRecipe(new ItemStack(PlayerProxies.blockProximitySensor), 
				"hhh", "hlh", "hrh",
				'h', new ItemStack(PlayerProxies.blockHardenedStone), 
				'l', new ItemStack(PlayerProxies.itemLinkFocus), 
				'r', new ItemStack(Item.redstone));
		
		GameRegistry.addRecipe(new ItemStack(PlayerProxies.blockSpawnerLoader), 
				"hlh", "hdh", "hhh",
				'h', new ItemStack(PlayerProxies.blockHardenedStone), 
				'l', new ItemStack(PlayerProxies.itemLinkFocus), 
				'd', new ItemStack(Item.diamond));

		GameRegistry.addRecipe(new ItemStack(PlayerProxies.blockToughwoodPlank, 4), 
				" b ", "bwb", " b ",
				'b', new ItemStack(Block.planks, 1, 2),
				'w', new ItemStack(Block.wood)
		);
		
		GameRegistry.addRecipe(new ItemStack(PlayerProxies.blockBiomeChanger), 
				" c ", "pdp", " p ",
				'c', new ItemStack(PlayerProxies.itemLinkFocus),
				'p', new ItemStack(PlayerProxies.blockToughwoodPlank),
				'd', new ItemStack(Block.blockDiamond)
		);

		GameRegistry.addRecipe(new ItemStack(PlayerProxies.blockBiomeScanner), 
				" g ", "pop", " p ",
				'g', new ItemStack(Item.goldNugget),
				'o', new ItemStack(Block.obsidian),
				'p', new ItemStack(PlayerProxies.blockToughwoodPlank)
		);

		GameRegistry.addShapelessRecipe(new ItemStack(PlayerProxies.blockParticleGenerator), Block.fenceIron, PlayerProxies.blockHardenedStone);
	}

	private void registerEntities() {
		EntityRegistry.registerModEntity(Ghost.class, "PP_Ghost",
				EntityRegistry.findGlobalUniqueEntityId(),
				PlayerProxies.instance, 100, 20, true);
	}

	private void registerHandlers() {
		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		AdaptorGravitationalField.register();
	}
}
