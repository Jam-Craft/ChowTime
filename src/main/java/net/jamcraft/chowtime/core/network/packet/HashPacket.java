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

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.jamcraft.chowtime.remote.RemoteMain;

/**
 * Created by James Hollowell on 5/18/2014.
 */
public class HashPacket
        implements IMessage, IMessageHandler<HashPacket, IMessage>
{
    public String hash;

    @SuppressWarnings("unused")
    public HashPacket()
    {
    }

    public HashPacket(String hash)
    {
        this.hash = hash;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        hash = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, hash);
    }

    @Override
    public IMessage onMessage(HashPacket message, MessageContext ctx)
    {
        RemoteMain.IsSyncedWithServer(message.hash);
        return null;
    }
}
