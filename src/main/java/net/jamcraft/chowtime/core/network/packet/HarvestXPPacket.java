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

package net.jamcraft.chowtime.core.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.jamcraft.chowtime.core.harvestxp.HarvestXPClient;

/**
 * Created by James Hollowell on 6/26/2014.
 */
public class HarvestXPPacket
        implements IMessage, IMessageHandler<HarvestXPPacket, IMessage>
{
    public int xp = 0;

    public HarvestXPPacket()
    {
    }

    public HarvestXPPacket(int xp)
    {
        this.xp = xp;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.xp = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(xp);
    }

    @Override
    public IMessage onMessage(HarvestXPPacket message, MessageContext ctx)
    {
        HarvestXPClient.INSTANCE.SyncServer(message.xp);
        return null;
    }
}
