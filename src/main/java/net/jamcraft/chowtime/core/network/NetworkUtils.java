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

import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import net.jamcraft.chowtime.ChowTime;
import net.minecraft.entity.player.EntityPlayer;

public class NetworkUtils
{
    public static void writeString(ByteBuf buff, String string)
    {
        if (string != null)
        {
            int size = string.length();
            buff.writeInt(size);
            for (int i = 0; i < size; i++)
            {
                buff.writeChar(string.toCharArray()[i]);
            }
        }
        else
        {
            ChowTime.logger.error("Trying to write a null string", new Throwable());
        }
    }

    public static String readString(ByteBuf buff)
    {
        int size = buff.readInt();
        String s = "";
        for (int i = 0; i < size; i++)
        {
            s += buff.readChar();
        }
        return s;
    }

//    @SideOnly(Side.SERVER)
    public static void SendPacketToPlayer(EntityPlayer player, IPacket packet)
    {
        ChowTime.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        ChowTime.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        ChowTime.channels.get(Side.SERVER).writeAndFlush(packet).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

//    @SideOnly(Side.CLIENT)
    public static void SendPacktToServer(IPacket packet)
    {
        ChowTime.channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        ChowTime.channels.get(Side.CLIENT).writeAndFlush(packet).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }
}
