package net.jamcraft.chowtime.dyn.items;

import net.jamcraft.chowtime.ChowTime;
import net.jamcraft.chowtime.core.ModConstants;
import net.jamcraft.chowtime.dyn.DynItems;
import net.jamcraft.chowtime.dyn.common.IDynItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;

/**
 * Created by James Hollowell on 5/18/2014.
 */
public class ItemBarleyDough extends Item implements IDynItem
{

    public ItemBarleyDough()
    {
        super();
        setCreativeTab(ChowTime.creativeTab);
        setTextureName(ModConstants.MODID + ":barleyDough");
        setUnlocalizedName("barleyDough");
    }

    @Override public String getRegistrationName()
    {
        return "barleyDough";
    }

    @Override public void registerRecipe()
    {
        CraftingManager.getInstance().addShapelessRecipe(new ItemStack(this), new ItemStack(DynItems.items.get("barleyFlour")), new ItemStack(Items.water_bucket));
        FurnaceRecipes.smelting().func_151394_a(new ItemStack(this),new ItemStack(Items.bread),.175F);
    }
}
