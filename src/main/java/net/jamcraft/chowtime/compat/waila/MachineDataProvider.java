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

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.jamcraft.chowtime.core.tileentities.TEFermenter;
import net.jamcraft.chowtime.core.tileentities.TEIceCreamMaker;
import net.jamcraft.chowtime.core.tileentities.TEJuicer;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by James Hollowell on 6/25/2014.
 */
public class MachineDataProvider implements IWailaDataProvider
{
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return null;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        if (accessor.getTileEntity() instanceof TEFermenter)
        {
            TEFermenter te = (TEFermenter) accessor.getTileEntity();
            currenttip.add("Completion: " + te.getScaledProgress(100) + "%");
        }
        else if (accessor.getTileEntity() instanceof TEJuicer)
        {
            TEJuicer te = (TEJuicer) accessor.getTileEntity();
            currenttip.add("Completion: " + te.getScaledProgress(100) + "%");
        }
        else if (accessor.getTileEntity() instanceof TEIceCreamMaker)
        {
            TEIceCreamMaker te = (TEIceCreamMaker) accessor.getTileEntity();
            currenttip.add("Completion: "+te.getScaledProgress(100)+"%");
            currenttip.add("Temperature: "+te.getTemp()+"\u00b0C");
            currenttip.add("Freezing Temperature: "+te.getFreezeTemp()+"\u00b0C");
        }
        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return null;
    }
}
