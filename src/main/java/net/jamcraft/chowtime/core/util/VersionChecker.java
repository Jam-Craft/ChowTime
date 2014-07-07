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

package net.jamcraft.chowtime.core.util;

import com.google.gson.Gson;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.jamcraft.chowtime.ChowTime;
import net.jamcraft.chowtime.core.config.Config;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by James Hollowell on 6/2/2014.
 */
public class VersionChecker implements Runnable
{
    private static final int CHECK_COUNT = 3;
    private boolean needsUpdate = false;
    private String changelog = "";
    private Version local = new Version(0, 0, 0, 0);
    private Version remote = null;
    private static boolean HasBeenNotified = false;

    class RemoteVersion
    {
        public int[] version = new int[] { 0, 0, 0, 0 };
        public String changelog = "";
    }

    public static final VersionChecker instance = new VersionChecker();

    //https://github.com/pahimar/Equivalent-Exchange-3/blob/1.6.4/src/main/java/com/pahimar/ee3/helper/VersionHelper.java
    @SubscribeEvent
    public void onClientConnectToServer(EntityJoinWorldEvent event)
    {
        if (event.entity instanceof EntityPlayer && event.world.isRemote)
        {
            if (!HasBeenNotified && needsUpdate)
            {
                EntityPlayer player = (EntityPlayer) event.entity;
                player.addChatComponentMessage(new ChatComponentText("ChowTime has updated! Latest version " + remote.toString()));
                player.addChatComponentMessage(new ChatComponentText("Changelog: " + changelog));
            }
        }
    }

    public void CheckVersion()
    {
        local.readFromString(ChowTime.version);
        for (int i = 0; i < CHECK_COUNT; i++)
        {
            try
            {
                URL url = new URL(Config.remoteLoc + "version.json");
                URLConnection con = url.openConnection();
                InputStreamReader isr = new InputStreamReader(con.getInputStream());
                Gson gson = new Gson();
                RemoteVersion v = gson.fromJson(isr, RemoteVersion.class);
                remote = new Version(v.version[0], v.version[1], v.version[2], v.version[3]);
                int comp = remote.compareTo(local);
                if (comp == 1)
                {
                    needsUpdate = true;
                    changelog = v.changelog;
                    return;
                }
                else
                {
                    return;
                }
            }
            catch (IOException e)
            {
                ChowTime.logger.error("Error reading remote version (try {}/{})", i + 1, CHECK_COUNT);
                try
                {
                    Thread.sleep(5000);
                }
                catch (InterruptedException ie)
                {
                    e.printStackTrace();
                }
            }
        }
        ChowTime.logger.error("ChowTime Remote version check failed after {} attempts.", CHECK_COUNT);
    }

    private void LogResult()
    {
        if (needsUpdate)
        {
            ChowTime.logger.error("Needs update: local version-{}; remote version-{}", local.toString(), remote.toString());
        }
    }

    @Override
    public void run()
    {
        CheckVersion();
        LogResult();
    }

    public static void execute()
    {
        new Thread(instance).start();
    }
}
