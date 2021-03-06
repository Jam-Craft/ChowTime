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

package net.jamcraft.chowtime.core.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.jamcraft.chowtime.core.ModConstants;
import net.jamcraft.chowtime.core.network.packet.FallbackFileResponsePacket;
import net.jamcraft.chowtime.core.network.packet.HarvestXPPacket;
import net.jamcraft.chowtime.core.network.packet.HashPacket;
import net.jamcraft.chowtime.core.network.packet.RequestFallbackCheckPacket;

/**
 * Created by James Hollowell on 5/18/2014.
 */
public class PacketHandler
{
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ModConstants.MODID);

    public static void init()
    {
        INSTANCE.registerMessage(HashPacket.class, HashPacket.class, 0, Side.CLIENT);
        INSTANCE.registerMessage(HarvestXPPacket.class, HarvestXPPacket.class, 1, Side.CLIENT);
        INSTANCE.registerMessage(RequestFallbackCheckPacket.class, RequestFallbackCheckPacket.class, 2, Side.SERVER);
        INSTANCE.registerMessage(FallbackFileResponsePacket.class, FallbackFileResponsePacket.class, 3, Side.CLIENT);
    }

}
