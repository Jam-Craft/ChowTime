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

package net.jamcraft.chowtime.core.items.produce;

import net.jamcraft.chowtime.ChowTime;
import net.jamcraft.chowtime.core.ModConstants;
import net.minecraft.item.ItemFood;

/**
 * Created by DarkKnight on 5/18/14.
 */
public class ItemRaspberry extends ItemFood
{
    //Tier 2
    public ItemRaspberry()
    {
        super(3,false);
        this.setCreativeTab(ChowTime.creativeTab);
        setUnlocalizedName("raspberry");
        this.setTextureName(ModConstants.MODID + ":raspberry");
    }
}
