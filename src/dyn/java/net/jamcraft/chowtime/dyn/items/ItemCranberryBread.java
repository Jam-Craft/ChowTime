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
public class ItemCranberryBread extends ItemFood implements IDynItem
{
    public ItemCranberryBread()
    {
        super(9, false);
        setCreativeTab(ChowTime.creativeTab);
        setTextureName(ModConstants.MODID + ":cranberryBread");
        setUnlocalizedName("cranberryBread");
    }

    @Override public String getRegistrationName()
    {
        return "cranberryBread";
    }

    @Override public void registerRecipe()
    {
        CraftingManager.getInstance().addShapelessRecipe(new ItemStack(this), new ItemStack(Items.bread), new ItemStack(DynItems.items.get("cranberryJam")));
    }
}
