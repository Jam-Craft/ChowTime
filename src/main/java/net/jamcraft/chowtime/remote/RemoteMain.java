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

import cpw.mods.fml.common.FMLCommonHandler;
import net.jamcraft.chowtime.ChowTime;
import net.jamcraft.chowtime.core.Config;
import net.jamcraft.chowtime.core.ModConstants;
import net.jamcraft.chowtime.core.util.ObfHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import java.io.*;
import java.net.URL;
import java.security.MessageDigest;
import java.util.List;

/**
 * Created by James Hollowell on 5/14/2014.
 */
public class RemoteMain
{
    public static LooseObjList local = new LooseObjList();
    private static LooseObjList remote = new LooseObjList();

    public static boolean hasUpdated = false;
    public static boolean isSyncedWithServer=false;
    public static String localHash="";
    public static EntityPlayer  player;

    public static void init()
    {
        ChowTime.logger.error("Starting remote checking...");
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
        HashCTD();
    }

    public static boolean LoadLocal()
    {
        ChowTime.logger.error("Loading local..");
        local.getObjects().clear();
        File f = new File(ModConstants.DYN_LOC + "/local.ctd");
        local.readFromFile(f);
        ChowTime.logger.error("Done loading local..");
        return true;
    }

    public static boolean LoadRemote()
    {
        try
        {
            ChowTime.logger.error("Loading remote...");
            ChowTime.logger.error("Downloading remote...");
            URL url = new URL(Config.remoteLoc + "dyn/current.ctd");

            File dyn = new File(ModConstants.DYN_LOC + "/remote.ctd");
            if (!dyn.exists()) dyn.createNewFile();

            org.apache.commons.io.FileUtils.copyURLToFile(url,dyn);

//            URLConnection con = url.openConnection();
//            InputStreamReader isr = new InputStreamReader(con.getInputStream());
//            BufferedReader br = new BufferedReader(isr);
//
//            FileWriter fw = new FileWriter(dyn);
//            while (br.ready())
//            {
//                fw.write(br.readLine());
//                fw.write("\n");
//            }
//            fw.close();
//            br.close();

            ChowTime.logger.error("Done downloading remote ctd...");
            ChowTime.logger.error("Loading remote ctd...");

            remote.readFromFile(dyn);
            ChowTime.logger.error("Done loading remote...");
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
            ChowTime.logger.warn("Downloading remote " + remotepath + " to local " + localpath);
            if (remotepath == null) remotepath = localpath;
            URL url = new URL(Config.remoteLoc  + "dyn/current" + remotepath);

            File f = new File(ModConstants.DYN_LOC + localpath);
            if (!f.exists())
            {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }

            org.apache.commons.io.FileUtils.copyURLToFile(url, f);

//            URLConnection con = url.openConnection();
//            InputStream reader = url.openStream();
//
//            FileOutputStream writer = new FileOutputStream(ModConstants.DYN_LOC + localpath);
//            int total = con.getContentLength();
//            int size_dl = 0;
//            byte[] buffer = new byte[blk_size];
//            int bytesRead = 0;
//            while ((bytesRead = reader.read(buffer)) > 0)
//            {
//                size_dl += bytesRead;
//                writer.write(buffer, 0, bytesRead);
//                buffer = new byte[blk_size];
//            }
//            writer.close();
//            reader.close();
            ChowTime.logger.warn("Download complete...");
        }
        catch (IOException e)
        {
            ChowTime.logger.error("Error downloading file "+remotepath);
        }
    }

    @SuppressWarnings("resource")
    public static void HashCTD()
    {
        try
        {
            //TODO: Update to Guava
            MessageDigest md = MessageDigest.getInstance("SHA1");
            FileInputStream fis = new FileInputStream(ModConstants.DYN_LOC + "/local.ctd");
            byte[] dataBytes = new byte[1024];

            int nread = 0;

            while ((nread = fis.read(dataBytes)) != -1)
            {
                md.update(dataBytes, 0, nread);
            }

            byte[] mdbytes = md.digest();

            //convert the byte to hex format
            StringBuffer sb = new StringBuffer("");
            for (int i = 0; i < mdbytes.length; i++)
            {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }

//            System.out.println("Digest(in hex format):: " + sb.toString());
            localHash=sb.toString();
        }
        catch (Exception e)
        {

        }
    }

    public static boolean IsSyncedWithServer(String serverHash)
    {
        isSyncedWithServer = localHash.equals(serverHash);
        if(!isSyncedWithServer && player!=null)
        {
            player.addChatComponentMessage(new ChatComponentTranslation("string.nosync"));
            if(FMLCommonHandler.instance().getEffectiveSide().isClient())
            {
                Minecraft.getMinecraft().theWorld.sendQuittingDisconnectingPacket();
            }
        }
        return isSyncedWithServer;
    }
}
