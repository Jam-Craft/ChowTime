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

package net.jamcraft.chowtime.core.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.jamcraft.chowtime.core.CommonProxy;
import net.jamcraft.chowtime.core.registrars.HarvestLevelRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by James Hollowell on 7/13/2014.
 */
public abstract class CTSeed extends ItemSeeds
{
    public CTSeed(Block crop, Block soilBlock)
    {
        super(crop, soilBlock);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /** Allows items to add custom lines of information to the mouseover description. */
    public void addInformation(ItemStack stack, EntityPlayer entityPlayer, List infoList, boolean par4)
    {
        // TODO See if this can be localized
//        if (CommonProxy.shouldAddAdditionalInfo())
//        {
            int tier = HarvestLevelRegistry.LevelForSeed(this);
            infoList.add(String.format("Crop tier: %d", tier));
//        }
//        else
//        {
//            infoList.add(CommonProxy.additionalInfoInstructions());
//        }
    }
}
