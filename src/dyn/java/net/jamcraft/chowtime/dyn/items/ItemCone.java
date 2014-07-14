package net.jamcraft.chowtime.dyn.items;

import cpw.mods.fml.common.registry.GameRegistry;
import net.jamcraft.chowtime.ChowTime;
import net.jamcraft.chowtime.core.ModConstants;
import net.jamcraft.chowtime.core.items.CTItem;
import net.jamcraft.chowtime.core.lib.CTStrings;
import net.jamcraft.chowtime.dyn.DynItems;
import net.jamcraft.chowtime.dyn.common.IDynItem;
import net.minecraft.item.ItemStack;

/**
 * Created by Kayla Marie on 5/15/14.
 */
public class ItemCone extends CTItem implements IDynItem
{

    public ItemCone()
    {
        super();
        this.setCreativeTab(ChowTime.creativeTab);
        this.setTextureName(ModConstants.MODID + ":cone");
        this.setUnlocalizedName(CTStrings.Item_Cone);
    }

    @Override public String getRegistrationName()
    {
        return "cone";
    }

    @Override public void registerRecipe()
    {
        GameRegistry.addRecipe(new ItemStack(this, 3),"   ","B B"," B ",'B', DynItems.items.get("barleyDough"));
    }
}
