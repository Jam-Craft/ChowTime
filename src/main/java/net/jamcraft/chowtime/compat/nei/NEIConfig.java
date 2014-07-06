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

package net.jamcraft.chowtime.compat.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import cpw.mods.fml.common.Optional;

/**
 * Created by James Hollowell on 5/25/2014.
 */
public class NEIConfig implements IConfigureNEI
{
    @Override public void loadConfig()
    {
        API.registerRecipeHandler(new JuicerHandler());
        API.registerUsageHandler(new JuicerHandler());

        API.registerUsageHandler(new FermenterHandler());
        API.registerRecipeHandler(new FermenterHandler());

        API.registerRecipeHandler(new IceCreamMakerHandler());
        API.registerUsageHandler(new IceCreamMakerHandler());
    }

    @Override public String getName()
    {
        return "ChowTime";
    }

    @Override public String getVersion()
    {
        return "1.0";
    }
}
