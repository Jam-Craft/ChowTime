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
import net.jamcraft.chowtime.ChowTime;
import net.jamcraft.chowtime.core.ModConstants;
import net.jamcraft.chowtime.remote.RemoteMain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by James Hollowell on 7/10/2014.
 */
public class FallbackFileResponsePacket
        implements IMessage, IMessageHandler<FallbackFileResponsePacket, IMessage>
{

    @Override
    public void fromBytes(ByteBuf byteBuf)
    {
        try
        {
            FileWriter fileWriter = new FileWriter(ModConstants.DYN_LOC + "/sync.ctd");
            fileWriter.write(ByteBufUtils.readUTF8String(byteBuf));
            fileWriter.close();
        }
        catch (IOException e)
        {
            ChowTime.logger.error("Error receiving the fallback dynamic sync file.", e);
        }
    }

    @Override
    public void toBytes(ByteBuf byteBuf)
    {
        try
        {
            FileReader fis = new FileReader(ModConstants.DYN_LOC + "/local.ctd");
            BufferedReader br = new BufferedReader(fis);

            String line = br.readLine();
            String file = "";
            while (line != null)
            {
                file += line + "\n";
                line = br.readLine();
            }
            br.close();

            ByteBufUtils.writeUTF8String(byteBuf, file);
        }
        catch (IOException e)
        {
            ChowTime.logger.error("Error sending the fallback dynamic sync file.", e);
        }
    }

    @Override
    public IMessage onMessage(FallbackFileResponsePacket message, MessageContext ctx)
    {
        RemoteMain.FallbackSyncEnd();
        return null;
    }
}
