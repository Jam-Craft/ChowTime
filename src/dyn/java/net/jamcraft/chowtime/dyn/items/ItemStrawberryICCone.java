package net.jamcraft.chowtime.dyn.items;

import net.jamcraft.chowtime.ChowTime;
import net.jamcraft.chowtime.core.ModConstants;
import net.jamcraft.chowtime.dyn.DynItems;
import net.jamcraft.chowtime.dyn.common.IDynItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/**
 * Created by James Hollowell on 5/17/2014.
 */
public class ItemStrawberryICCone extends ItemFood implements IDynItem
{
    public ItemStrawberryICCone()
    {
        super(5, false);
        setCreativeTab(ChowTime.creativeTab);
        setTextureName(ModConstants.MODID + ":strawberryIceCreamCone");
        setUnlocalizedName("strawberryIceCreamCone");
    }

    protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player)
    {
        if (!world.isRemote)
        {
            player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id,300,0));
        }
    }

    @Override
    public String getRegistrationName()
    {
        return "strawberryiccone";
    }

    @Override
    public void registerRecipe()
    {
        CraftingManager.getInstance().addShapelessRecipe(new ItemStack(this), new ItemStack(DynItems.items.get("cone")), new ItemStack(DynItems.items.get("strawberryIceCream")));
    }
}
