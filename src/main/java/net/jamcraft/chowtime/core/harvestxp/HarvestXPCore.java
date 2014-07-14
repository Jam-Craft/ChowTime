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

package net.jamcraft.chowtime.core.harvestxp;

/**
 * Created by James Hollowell on 6/26/2014.
 */
public abstract class HarvestXPCore
{
    public static int GetHarvestLevelFromXP(int xp)
    {
        if (xp < 20) return 1;
        if (xp < 100) return 2;
        if (xp < 300) return 4; //also includes 3 for some reason...
        return 5;
    }
}
