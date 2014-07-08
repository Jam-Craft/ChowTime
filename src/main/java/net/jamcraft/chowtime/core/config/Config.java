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

package net.jamcraft.chowtime.core.config;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.jamcraft.chowtime.core.ModConstants;
import net.minecraftforge.common.config.Configuration;

/**
 * Created by James Hollowell on 5/14/2014.
 */
public class Config
{
    public static final String DYN_CATEGORY="dynamic";
    public static final String STATIC_CATEGORY="static";

    //and public static vars here
    public static String remoteLoc;
    public static boolean forceLocal;
    public static boolean useDev;
    public static boolean shouldRenderXP;

    public static Configuration config;

    public static void init(Configuration conf)
    {
        config = conf;
        load();
    }

    public static void save()
    {
        //        config.get(DYN_CATEGORY, "RemoteLocation", remoteLoc).set(remoteLoc);
        //        config.get(DYN_CATEGORY, "ForceLocal", forceLocal).set(forceLocal);
        //        config.get(DYN_CATEGORY, "useDev", useDev).set(useDev);
        config.get(STATIC_CATEGORY, "RenderChowTimeInfo", shouldRenderXP).set(shouldRenderXP);
        config.save();
    }

    public static void load()
    {
        remoteLoc = config.get(DYN_CATEGORY, "RemoteLocation", "http://jam-craft.github.io/ChowTime/").getString();
        forceLocal = config.get(DYN_CATEGORY, "ForceLocal", false).getBoolean(false);
        useDev = config.get(DYN_CATEGORY, "UseDevVersions", false).getBoolean(false);
        shouldRenderXP = config.get(STATIC_CATEGORY, "RenderChowTimeInfo", false).getBoolean(false);
        remoteLoc = remoteLoc + (useDev ? "dev/" : "");
        if (config.hasChanged())
            config.save();
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.modID.equals(ModConstants.MODID))
        {
            load();
        }
    }
}
