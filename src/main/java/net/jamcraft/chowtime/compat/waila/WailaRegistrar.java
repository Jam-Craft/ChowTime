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

package net.jamcraft.chowtime.compat.waila;

import mcp.mobius.waila.api.IWailaRegistrar;
import net.jamcraft.chowtime.core.blocks.machines.IMachineBlock;
import net.jamcraft.chowtime.core.crops.ICrop;
import net.jamcraft.chowtime.core.mobs.SeedMob.EntitySeedMob;

/**
 * Created by James Hollowell on 6/25/2014.
 */
public class WailaRegistrar
{
    @SuppressWarnings("unused")
    public static void registerCallbacks(IWailaRegistrar registrar)
    {
        registrar.registerBodyProvider(new MachineDataProvider(), IMachineBlock.class);
        registrar.registerBodyProvider(new CropDataProvider(), ICrop.class);

        registrar.registerBodyProvider(new EntityDataProvider(), EntitySeedMob.class);
    }
}
