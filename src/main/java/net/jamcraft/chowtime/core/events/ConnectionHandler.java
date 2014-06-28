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

package net.jamcraft.chowtime.core.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.jamcraft.chowtime.core.harvestxp.HarvestXPServer;
import net.jamcraft.chowtime.core.network.PacketHandler;
import net.jamcraft.chowtime.core.network.packet.SHA1Packet;
import net.jamcraft.chowtime.remote.RemoteMain;

/**
 * Created by James Hollowell on 5/18/2014.
 */
public class ConnectionHandler
{
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        PacketHandler.INSTANCE.sendTo(new SHA1Packet(RemoteMain.localHash), (net.minecraft.entity.player.EntityPlayerMP) event.player);
        HarvestXPServer.INSTANCE.SyncClient(event.player);
    }

    //    @SubscribeEvent
    //    public void connectToServer(FMLNetworkEvent.ServerConnectionFromClientEvent e)
    //    {
    //        if (!e.isLocal)
    //        {
    //            if (!RemoteMain.isSyncedWithServer)
    //            {
    //                e.setCanceled(true);
    //            }
    //        }
    //    }
}
