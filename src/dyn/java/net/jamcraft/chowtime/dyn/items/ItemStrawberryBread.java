package net.jamcraft.chowtime.dyn.items;

import net.jamcraft.chowtime.ChowTime;
import net.jamcraft.chowtime.core.ModConstants;
import net.jamcraft.chowtime.dyn.DynItems;
import net.jamcraft.chowtime.dyn.common.IDynItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

/**
 * Created by James Hollowell on 5/17/2014.
 */
public class ItemStrawberryBread extends ItemFood implements IDynItem
{
    public ItemStrawberryBread()
    {
        super(12, false);
        setCreativeTab(ChowTime.creativeTab);
        setTextureName(ModConstants.MODID + ":strawberryBread");
        setUnlocalizedName("strawberryBread");
    }

    @Override public String getRegistrationName()
    {
        return "strawberryBread";
    }

    @Override public void registerRecipe()
    {
        CraftingManager.getInstance().addShapelessRecipe(new ItemStack(this), new ItemStack(Items.bread), new ItemStack(DynItems.items.get("strawberryJam")));
    }
}
