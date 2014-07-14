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

package net.jamcraft.chowtime.remote;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import cpw.mods.fml.common.FMLCommonHandler;
import net.jamcraft.chowtime.ChowTime;
import net.jamcraft.chowtime.core.ModConstants;
import net.jamcraft.chowtime.core.config.Config;
import net.jamcraft.chowtime.core.network.PacketHandler;
import net.jamcraft.chowtime.core.network.packet.RequestFallbackCheckPacket;
import net.jamcraft.chowtime.core.util.ObfHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by James Hollowell on 5/14/2014.
 */
public class RemoteMain
{
    public static LooseObjList local = new LooseObjList();
    private static LooseObjList remote = new LooseObjList();

    public static boolean hasUpdated = false;
    public static boolean isSyncedWithServer = false;
    public static String localHash = "";
    public static EntityPlayer player;

    public static void init()
    {
        ChowTime.logger.info("Starting remote checking...");
        File dyndir = new File(ModConstants.DYN_LOC);
        if (!dyndir.exists()) dyndir.mkdir();
        LoadLocal();
        if (Config.forceLocal) return;
        LoadRemote();
        if (!local.equals(remote) && remote.descriptions.size() > 0)
        {
            //Remove old files
            List<DynDescription> old = local.difference(remote);
            for (DynDescription desc : old)
            {
                File f = new File("");
                if (desc instanceof DynClassDescription)
                    f = new File(ModConstants.DYN_LOC + "/" + ((DynClassDescription) desc).classname.replace('.', '/') + ".class");
                else if (desc instanceof DynResourceDescription)
                    f = new File(ModConstants.DYN_LOC + "/assets/chowtime/" + ((DynResourceDescription) desc).path);
                f.delete();
            }

            //Download the classes that need to be updated
            List<DynDescription> list = remote.difference(local);
            for (DynDescription desc : list)
            {
                if (desc instanceof DynClassDescription)
                    DownloadFile("/" + ((DynClassDescription) desc).classname.replace('.', '/') + ".class", "/" + (ObfHelper.isObf ? "obf/" : "deobf/") + ((DynClassDescription) desc).classname.replace('.', '/') + ".class");
                else if (desc instanceof DynResourceDescription)
                    DownloadFile("/assets/chowtime/" + ((DynResourceDescription) desc).path, null);
            }

            hasUpdated = true;

            //Update local file
            try
            {
                remote.writeToFile(new File(ModConstants.DYN_LOC + "/local.ctd"));
            }
            catch (IOException ioe)
            {
                ChowTime.logger.error("Could not update the local file from the remote location");
            }
            //Reload local
            LoadLocal();
        }
    }

    public static boolean LoadLocal()
    {
        ChowTime.logger.info("Loading local..");
        local.getObjects().clear();
        File f = new File(ModConstants.DYN_LOC + "/local.ctd");
        local.readFromFile(f);
        ChowTime.logger.info("Done loading local..");
        HashCTD();
        return true;
    }

    public static boolean LoadRemote()
    {
        try
        {
            ChowTime.logger.info("Loading remote...");
            ChowTime.logger.info("Downloading remote...");
            URL url = new URL(Config.remoteLoc + "dyn/current.ctd");

            File dyn = new File(ModConstants.DYN_LOC + "/remote.ctd");
            if (!dyn.exists()) dyn.createNewFile();

            org.apache.commons.io.FileUtils.copyURLToFile(url, dyn);

            ChowTime.logger.info("Done downloading remote ctd...");
            ChowTime.logger.info("Loading remote ctd...");

            remote.readFromFile(dyn);
            ChowTime.logger.info("Done loading remote...");
            return true;
        }
        catch (IOException e)
        {
            ChowTime.logger.error("Error reading remote CT file; falling back to local only");
        }

        return false;
    }

    private static void DownloadFile(String localpath, String remotepath)
    {
        try
        {
            ChowTime.logger.info("Downloading remote " + remotepath + " to local " + localpath);
            if (remotepath == null) remotepath = localpath;
            URL url = new URL(Config.remoteLoc + "dyn/current" + remotepath);

            File f = new File(ModConstants.DYN_LOC + localpath);
            if (!f.exists())
            {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }

            org.apache.commons.io.FileUtils.copyURLToFile(url, f);

            ChowTime.logger.info("Download complete...");
        }
        catch (IOException e)
        {
            ChowTime.logger.error("Error downloading file " + remotepath);
        }
    }

    public static void HashCTD()
    {
        try
        {
            HashFunction hasher = Hashing.md5();

            FileReader fis = new FileReader(ModConstants.DYN_LOC + "/local.ctd");
            BufferedReader br = new BufferedReader(fis);

            String line = br.readLine();
            String file = "";
            while (line != null)
            {
                file += line;
                line = br.readLine();
            }

            HashCode hash = hasher.hashString(file, Charset.defaultCharset());

            ChowTime.logger.info("Local.ctd hash digest(in hex format):: " + hash.toString());
            localHash = hash.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static boolean IsSyncedWithServer(String serverHash)
    {
        //TODO: Fallback method of actually sending the whole file.
        isSyncedWithServer = localHash.equals(serverHash);
        if (!isSyncedWithServer)
        {
            ChowTime.logger.error("Error connecting to server: Different ctd hashes. Falling back to full file transfer.");
            ChowTime.logger.error("Local ctd hash: " + localHash + " Remote server's ctd hash: " + serverHash);
            FallbackSyncStart();
        }
        return isSyncedWithServer;
    }

    public static void FallbackSyncStart()
    {
        PacketHandler.INSTANCE.sendToServer(new RequestFallbackCheckPacket());
    }

    public static void FallbackSyncEnd()
    {
        LooseObjList serverlist = new LooseObjList();
        File f = new File(ModConstants.DYN_LOC + "/sync.ctd");
        serverlist.readFromFile(f);
        if (!serverlist.equals(local))
        {
            ChowTime.logger.error("Error connecting to server with fallback. Leaving server.");
            failSync();
        }
        f.delete();
    }

    private static void failSync()
    {
        if (player != null)
            player.addChatComponentMessage(new ChatComponentTranslation("string.nosync"));
        ChowTime.logger.error("Error connecting to server: Different ctd versions.");
        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
        {
            Minecraft.getMinecraft().theWorld.sendQuittingDisconnectingPacket();
        }
    }
}
