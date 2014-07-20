package net.jamcraft.chowtime.dyn.items;

import net.jamcraft.chowtime.ChowTime;
import net.jamcraft.chowtime.core.ModConstants;
import net.jamcraft.chowtime.core.recipies.IceCreamRecipies;
import net.jamcraft.chowtime.dyn.common.IDynItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;

/**
 * Created by James Hollowell on 5/17/2014.
 */
public class ItemChocoIceCream extends ItemSnowball implements IDynItem
{
    public ItemChocoIceCream()
    {
        super();
        setCreativeTab(ChowTime.creativeTab);
        setTextureName(ModConstants.MODID + ":chocolateIceCream");
        setUnlocalizedName("chocolateIceCream");
    }

    @Override public String getRegistrationName()
    {
        return "chocoicecream";
    }

    @Override public void registerRecipe()
    {
        IceCreamRecipies.AddRecipe(new ItemStack(Items.dye, 1, 3), new ItemStack(Items.milk_bucket), new ItemStack(this), 60, -21000);
    }
}
