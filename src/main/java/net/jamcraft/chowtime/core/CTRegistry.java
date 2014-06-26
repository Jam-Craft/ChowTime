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

package net.jamcraft.chowtime.core;

import cpw.mods.fml.common.registry.GameRegistry;
import net.jamcraft.chowtime.core.blocks.BlockCottonCandy;
import net.jamcraft.chowtime.core.blocks.CTBlock;
import net.jamcraft.chowtime.core.blocks.machines.Fermenter;
import net.jamcraft.chowtime.core.blocks.machines.IceCreamMaker;
import net.jamcraft.chowtime.core.blocks.machines.Juicer;
import net.jamcraft.chowtime.core.crops.*;
import net.jamcraft.chowtime.core.items.*;
import net.jamcraft.chowtime.core.items.produce.*;
import net.jamcraft.chowtime.core.items.seeds.*;
import net.jamcraft.chowtime.core.lib.CTStrings;
import net.jamcraft.chowtime.core.tileentities.TEFermenter;
import net.jamcraft.chowtime.core.tileentities.TEIceCreamMaker;
import net.jamcraft.chowtime.core.tileentities.TEJuicer;
import net.minecraft.block.material.Material;

/**
 * Created by Kayla Marie on 5/14/14.
 */
public class CTRegistry
{
    public static void CTBlocks()
    {
        // CTInits.CTLeaves = new
        // CTLeaves().setBlockName(CTStrings.BlockLeaves_Basic);
        // GameRegistry.registerBlock(CTInits.CTLeaves,
        // CTStrings.BlockLeaves_Basic);
        // CTInits.CTLog = new CTLog().setBlockName(CTStrings.BlockLogs_Basic);
        // GameRegistry.registerBlock(CTInits.CTLog, CTStrings.BlockLogs_Basic);
        // CTInits.CTSapling = new
        // CTSapling().setBlockName(CTStrings.BlockSapling_Basic);
        // GameRegistry.registerBlock(CTInits.CTSapling,
        // CTStrings.BlockSapling_Basic);
        CTInits.IceCream = new CTBlock(Material.clay).setBlockName(CTStrings.BlockIceCream_Strawberry);
        GameRegistry.registerBlock(CTInits.IceCream, CTStrings.BlockIceCream_Strawberry);
        CTInits.CottonCandyBLUE = new BlockCottonCandy().setBlockName(CTStrings.BlockCottonCandy_Blue);
        GameRegistry.registerBlock(CTInits.CottonCandyBLUE, CTStrings.BlockCottonCandy_Blue);

//        CTInits.CTFarmland = new CTFarmland();
//        GameRegistry.registerBlock(CTInits.CTFarmland, CTStrings.BlockFarmland);
    }

    public static void CTMachines()
    {
        CTInits.Juicer = new Juicer();
        GameRegistry.registerBlock(CTInits.Juicer, "Juicer");
        CTInits.Fermenter = new Fermenter();
        GameRegistry.registerBlock(CTInits.Fermenter, "Fermenter");
        CTInits.IceCreamMaker = new IceCreamMaker();
        GameRegistry.registerBlock(CTInits.IceCreamMaker, "IceCreamMaker");

    }

    public static void CTLiquids()
    {
        // CTInits.ChocolateMilkFluid = new
        // Fluid("ChocolateMilk").setUnlocalizedName("ChocolateMilk");
        // FluidRegistry.registerFluid(CTInits.ChocolateMilkFluid);
        // CTInits.ChocolateMilk = new CTFluid(CTInits.ChocolateMilkFluid,
        // Material.water,
        // "ChocolateMilk").setBlockName("ChocolateMilk").setCreativeTab(ChowTime.creativeTab);
        // GameRegistry.registerBlock(CTInits.ChocolateMilk,
        // "Chocolate Milk Fluid");

        // CTInits.FruitPunchFluid = new Fluid("fruitPunch");
        // FluidRegistry.registerFluid(CTInits.FruitPunchFluid);
        // CTInits.FruitPunch = new CTFluid(CTInits.ChocolateMilkFluid,
        // Material.water, "fruitPunch").setCreativeTab(ChowTime.creativeTab);
        // GameRegistry.registerBlock(CTInits.FruitPunch, "Fruit Punch");
    }

    public static void CTCrops()
    {
        CTInits.CropBarley = new CropBarley();
        GameRegistry.registerBlock(CTInits.CropBarley, "barleyCrop").setBlockName("barleyCrop");
        CTInits.BarleySeeds = new SeedBarley();
        GameRegistry.registerItem(CTInits.BarleySeeds, "barleySeed");

        CTInits.CropStrawberry = new CropStrawberry();
        GameRegistry.registerBlock(CTInits.CropStrawberry, "strawberryCrop").setBlockName("strawberryCrop");
        CTInits.StrawberrySeeds = new SeedStrawberry();
        GameRegistry.registerItem(CTInits.StrawberrySeeds, "strawberrySeed");

        CTInits.CropGrape = new CropGrape();
        GameRegistry.registerBlock(CTInits.CropGrape, "grapeCrop").setBlockName("grapeCrop");
        CTInits.GrapeSeeds = new SeedGrape();
        GameRegistry.registerItem(CTInits.GrapeSeeds, "grapeSeed");

        CTInits.CropBlueberry = new CropBlueberry();
        GameRegistry.registerBlock(CTInits.CropBlueberry, "blueberryCrop").setBlockName("blueberryCrop");
        CTInits.BlueberrySeeds = new SeedBlueberry();
        GameRegistry.registerItem(CTInits.BlueberrySeeds, "blueberrySeed");

        CTInits.CropCorn = new CropCorn();
        GameRegistry.registerBlock(CTInits.CropCorn, "cornCrop").setBlockName("cornCrop");
        CTInits.CornSeeds = new SeedCorn();
        GameRegistry.registerItem(CTInits.CornSeeds, "cornSeed");

        CTInits.CropCranberry = new CropCranberry();
        GameRegistry.registerBlock(CTInits.CropCranberry, "cranberryCrop").setBlockName("cranberryCrop");
        CTInits.CranberrySeeds = new SeedCranberry();
        GameRegistry.registerItem(CTInits.CranberrySeeds, "cranberrySeed");

        CTInits.CropRaspberry = new CropRaspberry();
        GameRegistry.registerBlock(CTInits.CropRaspberry, "raspberryCrop").setBlockName("raspberryCrop");
        CTInits.RaspberrySeeds = new SeedRaspberry();
        GameRegistry.registerItem(CTInits.RaspberrySeeds, "raspberrySeed");

        CTInits.CropTomato = new CropTomato();
        GameRegistry.registerBlock(CTInits.CropTomato, "tomatoCrop").setBlockName("tomatoCrop");
        CTInits.TomatoSeeds = new SeedTomato();
        GameRegistry.registerItem(CTInits.TomatoSeeds, "tomatoSeed");
    }

    public static void CTItems()
    {
        CTInits.BarleyCrop = new CTItem().setUnlocalizedName(CTStrings.Item_Barley);
        GameRegistry.registerItem(CTInits.BarleyCrop, CTStrings.Item_Barley);
        CTInits.Strawberry = new ItemStrawberry();
        GameRegistry.registerItem(CTInits.Strawberry, "strawberry");
        CTInits.Grape = new ItemGrape();
        GameRegistry.registerItem(CTInits.Grape, "grape");
        CTInits.Blueberry = new ItemBlueberry();
        GameRegistry.registerItem(CTInits.Blueberry, "blueberry");
        CTInits.Tomato = new ItemTomato();
        GameRegistry.registerItem(CTInits.Tomato, "tomato");
        CTInits.Corn = new ItemCorn();
        GameRegistry.registerItem(CTInits.Corn, "corn");
        CTInits.Cranberry = new ItemCranberry();
        GameRegistry.registerItem(CTInits.Cranberry, "cranberry");
        CTInits.Raspberry = new ItemRaspberry();
        GameRegistry.registerItem(CTInits.Raspberry, "raspberry");

        CTInits.FarmerBoots = new ItemFarmerBoots();
        GameRegistry.registerItem(CTInits.FarmerBoots, "farmer_boots");

        CTInits.FoodBook = new CTFoodBook();
        GameRegistry.registerItem(CTInits.FoodBook, "food_book");

        // CTInits.ItemBucketChoco = new CTItemBucket(CTInits.ChocolateMilk,
        // CTInits.ItemBucketChoco,
        // "bucket_chocolate").setUnlocalizedName(CTStrings.Item_Bucket);
        // GameRegistry.registerItem(CTInits.ItemBucketChoco,
        // CTStrings.Item_Bucket);
    }

    public static void CTDrinks()
    {

    }

    public static void CTTileEntities()
    {
        GameRegistry.registerTileEntity(TEFermenter.class, "TEFermenter");
        GameRegistry.registerTileEntity(TEJuicer.class, "TEJuicer");
        GameRegistry.registerTileEntity(TEIceCreamMaker.class, "TEIceCreamMaker");
    }

}
