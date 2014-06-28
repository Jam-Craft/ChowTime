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

package net.jamcraft.chowtime.core.harvestxp;

import net.jamcraft.chowtime.ChowTime;
import net.jamcraft.chowtime.core.network.PacketHandler;
import net.jamcraft.chowtime.core.network.packet.HarvestXPPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.SaveHandler;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by James Hollowell on 6/26/2014.
 */
public class HarvestXPServer
{
    public static final HarvestXPServer INSTANCE = new HarvestXPServer();

    protected Map<String, Integer> userXP = new HashMap<String, Integer>();
    protected File saveFile;

    public void SetXPForUser(String user, int xp)
    {
        userXP.put(user, Integer.valueOf(xp));
        Save();
        SyncClient(MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(user));
    }

    public int GetXPForUser(String user)
    {
        if (!userXP.containsKey(user)) return -1;
        else return userXP.get(user);
    }

    public void SyncClient(EntityPlayer user)
    {
        String name = user.getCommandSenderName();
        if (!userXP.containsKey(name))
            SetXPForUser(name, 0);
        if (user != null)
        {
            PacketHandler.INSTANCE.sendTo(new HarvestXPPacket(GetXPForUser(name)), (net.minecraft.entity.player.EntityPlayerMP) user);
        }
    }

    public void Save()
    {
        //Assemble NBT Tags
        NBTTagCompound head = new NBTTagCompound();
        NBTTagList tagList = new NBTTagList();
        for (String user : userXP.keySet())
        {
            NBTTagCompound userTag = new NBTTagCompound();
            userTag.setString("user", user);
            userTag.setInteger("xp", userXP.get(user));
            tagList.appendTag(userTag);
        }
        head.setTag("list", tagList);

        //Create output stream and write it out
        try
        {
            OutputStream os = new FileOutputStream(saveFile);
            CompressedStreamTools.writeCompressed(head, os);
        }
        catch (IOException e)
        {
            ChowTime.logger.error("Error saving ChowTime XP to file: {}", e);
        }
    }

    public void Load()
    {
        if (saveFile == null) init();
        NBTTagCompound head;
        try
        {
            InputStream is = new FileInputStream(saveFile);
            head = CompressedStreamTools.readCompressed(is);
        }
        catch (IOException e)
        {
            ChowTime.logger.error("Error reading ChowTime XP from file: {}", e);
            return;
        }

        NBTTagList tagList = (NBTTagList) head.getTag("list");
        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound userTag = tagList.getCompoundTagAt(i);
            userXP.put(userTag.getString("user"), userTag.getInteger("xp"));
        }
    }

    public void init()
    {
        SaveHandler saveHandler = (SaveHandler) MinecraftServer.getServer().worldServerForDimension(0).getSaveHandler();
        saveFile = new File(saveHandler.getWorldDirectory().getAbsolutePath() + "/ChowTimeXP.dat");
        try
        {
            if (!saveFile.exists()) saveFile.createNewFile();
        }
        catch (IOException e)
        {
            ChowTime.logger.error("Error creating ChowTime XP save file: {}", e);
        }
    }
}
