package net.jamcraft.chowtime;

import cpw.mods.fml.common.registry.GameRegistry;
import net.jamcraft.chowtime.core.CTInits;
import net.jamcraft.chowtime.core.CTRegistry;
import net.jamcraft.chowtime.core.CommonProxy;
import net.jamcraft.chowtime.core.Config;
import net.jamcraft.chowtime.core.ModConstants;
import net.jamcraft.chowtime.core.events.BucketHandler;
import net.jamcraft.chowtime.core.gen.candyLand.BiomeGenCandyLand;
import net.jamcraft.chowtime.core.materials.CloudMaterial;
import net.jamcraft.chowtime.dyn.DynItems;
import net.jamcraft.chowtime.dyn.DynMain;
import net.jamcraft.chowtime.remote.DynClassDescription;
import net.jamcraft.chowtime.remote.RemoteMain;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Created by James Hollowell on 5/14/2014.
 */
@Mod(modid = ModConstants.MODID, name = ModConstants.NAME)
public class ChowTime
{

    public static final BiomeGenBase TutorialBiome1 = new BiomeGenCandyLand(55);

    public static CreativeTabs creativeTab = new CreativeTabs("ChowTime")
    {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem()
        {
            return Items.chainmail_chestplate;
        }
    };

    @Mod.Instance(ModConstants.MODID)
    public static ChowTime instance;

    public static Material cloud = new CloudMaterial();

    @SidedProxy(clientSide = "net.jamcraft.chowtime.core.client.ClientProxy", serverSide = "net.jamcraft.chowtime.core.CommonProxy")
    public static CommonProxy proxy;

    public static Logger logger;

    //    private File configBase;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
//        FMLInterModComms.sendMessage("Waila", "register", "allout58.mods.prisoncraft.compat.waila.WailaProvider.callbackRegister");

//        channels = NetworkRegistry.INSTANCE.newChannel(ModConstants.MODID, new ChannelHandler());
//        proxy.registerRenderers();
        logger = event.getModLog();

        Config.init(new Configuration(event.getSuggestedConfigurationFile()));
        RemoteMain.init();
        CTRegistry.CTBlocks();
        CTRegistry.CTMachines();
        CTRegistry.CTLiquids();
        CTRegistry.CTCrops();
        CTRegistry.CTItems();
        CTRegistry.CTTileEntities();
        DynItems.registerRecipes();
        MinecraftForge.EVENT_BUS.register(BucketHandler.INSTANCE);
        BucketHandler.INSTANCE.buckets.put(CTInits.ChocolateMilk, CTInits.ItemBucketChoco);
        DynMain.init();
        //        configBase=event.getModConfigurationDirectory();

//        MinecraftForge.EVENT_BUS.register(new ConfigToolHighlightHandler());

//        BlockList.init();
//        ItemList.init();
//        TileEntityList.init();

        //NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        DynItems.registerRecipes();
        //FMLInterModComms.sendMessage("prisoncraft", "blacklist", Block.blockRegistry.getNameForObject(Blocks.bookshelf));
        //GameRegistry.registerWorldGenerator();
    }

    @Mod.EventHandler
    public void handleIMC(FMLInterModComms.IMCEvent event)
    {
        //IMCHandler.HandleIMC(event.getMessages());
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
//        event.registerServerCommand(new JailCommand());
    }

    @Mod.EventHandler
    public void serverUnload(FMLServerStoppingEvent event)
    {
    }
}