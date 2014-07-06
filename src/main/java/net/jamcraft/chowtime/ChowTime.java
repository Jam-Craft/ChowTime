/*
 * ChowTime - Dynamically updating food mod for Minecraft
 *     Copyright (C) 2014  Team JamCraft
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.jamcraft.chowtime;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.jamcraft.chowtime.core.*;
import net.jamcraft.chowtime.core.commands.ChowTimeCommand;
import net.jamcraft.chowtime.core.commands.client.ChowTimeClientCommand;
import net.jamcraft.chowtime.core.config.Config;
import net.jamcraft.chowtime.core.events.ConnectionHandler;
import net.jamcraft.chowtime.core.events.EntityEventHandler;
import net.jamcraft.chowtime.core.events.GuiEventHandler;
import net.jamcraft.chowtime.core.materials.CloudMaterial;
import net.jamcraft.chowtime.core.mobs.GingerbreadMan.EntityGingerbreadMan;
import net.jamcraft.chowtime.core.mobs.SeedMob.EntitySeedMob;
import net.jamcraft.chowtime.core.network.PacketHandler;
import net.jamcraft.chowtime.core.registrars.HarvestLevelRegistry;
import net.jamcraft.chowtime.core.registrars.SeedRegistry;
import net.jamcraft.chowtime.core.util.ObfHelper;
import net.jamcraft.chowtime.core.util.VersionChecker;
import net.jamcraft.chowtime.dyn.DynItems;
import net.jamcraft.chowtime.dyn.DynMain;
import net.jamcraft.chowtime.dyn.DynTextures;
import net.jamcraft.chowtime.remote.RemoteMain;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * Created by James Hollowell on 5/14/2014.
 */
@Mod(modid = ModConstants.MODID, name = ModConstants.NAME, version = "@VERSION@", acceptedMinecraftVersions = "@MC_VERSION@")
public class ChowTime
{
    public static String version = "@VERSION@";

    public static CreativeTabs creativeTab = new CreativeTabs("ChowTime")
    {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem()
        {
            return CTInits.BarleyCrop;
        }
    };

    public static final ArmorMaterial FARMER_BOOTS = EnumHelper.addArmorMaterial("FarmerBoots", 5, new int[] { 0, 0, 0, 2 }, 10);

    @Mod.Instance(ModConstants.MODID)
    public static ChowTime instance;

    public static Material cloud = new CloudMaterial();

    @Deprecated
    public static NBTTagCompound saveData = new NBTTagCompound();
    @Deprecated
    public static File harvestingLVL;
    @Deprecated
    public static File dir;
    @Deprecated
    public static int harvestXP = 0;
    @Deprecated
    public static int harvestLVL = 0;

    @SidedProxy(clientSide = "net.jamcraft.chowtime.core.client.ClientProxy", serverSide = "net.jamcraft.chowtime.core.CommonProxy")
    public static CommonProxy proxy;

    public static Logger logger;

    // private File configBase;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        if (version.contains("VERSION"))
            version = "1.0.1-rev4";//Hardcode if in dev environment

        // FMLInterModComms.sendMessage("Waila", "register",
        // "allout58.mods.prisoncraft.compat.waila.WailaProvider.callbackRegister");

        logger = event.getModLog();

        ObfHelper.init();
        // logger.error("Running in "+ (ObfHelper.isObf?"obf":"deobf") +
        // " environment");

        Config.init(new Configuration(event.getSuggestedConfigurationFile()));

        VersionChecker.execute();

        CTRegistry.CTBlocks();
        CTRegistry.CTMachines();
        CTRegistry.CTLiquids();
        CTRegistry.CTCrops();
        CTRegistry.CTItems();
        CTRegistry.CTTileEntities();

        PacketHandler.init();

        //        try
        //        {
        //            Field farmland = ObfHelper.getField(Blocks.class, "farmland", "ak");
        //            ObfHelper.setFinalStatic(farmland, CTInits.CTFarmland);
        //        }
        //        catch (Exception e)
        //        {
        //            Throwables.propagate(e);
        //        }

        //Do this before items registered to *hopefully* remove all the annoying errors
        DynTextures.addDynTP();
        RemoteMain.init();
        DynMain.init();

        OreDictionary.registerOre("ingotIron", Items.iron_ingot);

        MinecraftForge.EVENT_BUS.register(new EntityEventHandler());
        MinecraftForge.EVENT_BUS.register(VersionChecker.instance);

        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
            MinecraftForge.EVENT_BUS.register(new GuiEventHandler());

        //        MinecraftForge.EVENT_BUS.register(BucketHandler.INSTANCE);
        //        BucketHandler.INSTANCE.buckets.put(CTInits.ChocolateMilk, CTInits.ItemBucketChoco);

        //TODO: Remove in next update
        dir = event.getModConfigurationDirectory();

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);

        //Register Waila callbacks
        FMLInterModComms.sendMessage("Waila", "register", "net.jamcraft.chowtime.compat.waila.WailaRegistrar.registerCallbacks");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        //        try
        //        {
        //            Field farmland = ObfHelper.getField(Blocks.class, "farmland", "ak");
        //            ObfHelper.setFinalStatic(farmland, CTInits.CTFarmland);
        //        }
        //        catch (Exception e)
        //        {
        //            Throwables.propagate(e);
        //        }

        if(FMLCommonHandler.instance().getEffectiveSide().isClient())
            ClientCommandHandler.instance.registerCommand(new ChowTimeClientCommand());

        FMLCommonHandler.instance().bus().register(new ConnectionHandler());

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CTInits.Juicer, 1, 0), "WBW", "WPW", "ISI", 'W', "plankWood", 'B', Items.glass_bottle, 'P', Blocks.piston, 'I', "ingotIron", 'S', Blocks.stone));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CTInits.Fermenter, 1, 0), "WBW", "WBW", "ISI", 'W', "plankWood", 'B', Items.glass_bottle, 'I', "ingotIron", 'S', Blocks.stone));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CTInits.IceCreamMaker, 1, 0), "CBC", "C C", "SIS", 'C', Blocks.ice, 'B', Items.glass_bottle, 'I', "ingotIron", 'S', Blocks.stone));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CTInits.FarmerBoots, 1, 0), "W W", "WBW", 'W', Blocks.wool, 'B', Items.leather_boots));

        DynItems.registerRecipes();

        SeedRegistry.AddSeed((ItemSeeds) CTInits.BarleySeeds);
        SeedRegistry.AddSeed((ItemSeeds) CTInits.BlueberrySeeds);
        SeedRegistry.AddSeed((ItemSeeds) CTInits.CornSeeds);
        SeedRegistry.AddSeed((ItemSeeds) CTInits.CranberrySeeds);
        SeedRegistry.AddSeed((ItemSeeds) CTInits.GrapeSeeds);
        SeedRegistry.AddSeed((ItemSeeds) CTInits.RaspberrySeeds);
        SeedRegistry.AddSeed((ItemSeeds) CTInits.StrawberrySeeds);
        SeedRegistry.AddSeed((ItemSeeds) CTInits.TomatoSeeds);

        HarvestLevelRegistry.AddBlockHavestXP(CTInits.CropBarley, CTInits.BarleySeeds, 1);
        HarvestLevelRegistry.AddBlockHavestXP(CTInits.CropBlueberry, CTInits.BlueberrySeeds, 1);
        HarvestLevelRegistry.AddBlockHavestXP(CTInits.CropTomato, CTInits.TomatoSeeds, 2);
        HarvestLevelRegistry.AddBlockHavestXP(CTInits.CropCranberry, CTInits.CranberrySeeds, 2);
        HarvestLevelRegistry.AddBlockHavestXP(CTInits.CropRaspberry, CTInits.RaspberrySeeds, 2);
        HarvestLevelRegistry.AddBlockHavestXP(CTInits.CropGrape, CTInits.GrapeSeeds, 3);
        HarvestLevelRegistry.AddBlockHavestXP(CTInits.CropCorn, CTInits.CornSeeds, 4);
        HarvestLevelRegistry.AddBlockHavestXP(CTInits.CropStrawberry, CTInits.StrawberrySeeds, 5);

        proxy.registerRenderers();

        createEntity(EntitySeedMob.class, "SeedMob", 0x00AF00, 0xAA2167);
        createEntity(EntityGingerbreadMan.class, "GingerbreadMan", 0xAF4200, 0x612400);
        EntityRegistry.addSpawn(EntitySeedMob.class, 5, 2, 3, EnumCreatureType.creature, BiomeGenBase.forest, BiomeGenBase.forestHills, BiomeGenBase.birchForest, BiomeGenBase.birchForestHills, BiomeGenBase.plains, BiomeGenBase.beach, BiomeGenBase.coldBeach, BiomeGenBase.frozenRiver);
    }

    public void createEntity(Class<? extends EntityLiving> entity, String entityName, int solidColor, int spotColor)
    {
        int randomID = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(entity, entityName, randomID, solidColor, spotColor);
        EntityRegistry.registerModEntity(entity, entityName, randomID, this, 40, 3, true);
    }

    @Mod.EventHandler
    public void handleIMC(FMLInterModComms.IMCEvent event)
    {
        // IMCHandler.HandleIMC(event.getMessages());
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new ChowTimeCommand());
//        HarvestXPServer.INSTANCE.init();
    }

    @Mod.EventHandler
    public void serverUnload(FMLServerStoppingEvent event)
    {
    }
}
