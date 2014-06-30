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

import io.netty.buffer.ByteBuf;
import net.jamcraft.chowtime.ChowTime;

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
}
