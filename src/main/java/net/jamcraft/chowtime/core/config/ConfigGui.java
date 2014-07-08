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

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import net.jamcraft.chowtime.core.ModConstants;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James Hollowell on 7/7/2014.
 */
public class ConfigGui extends GuiConfig
{
    public ConfigGui(GuiScreen parent)
    {
        super(parent, getConfigElements(), ModConstants.MODID, false, false, GuiConfig.getAbridgedConfigPath(Config.config.toString()));
    }

    @SuppressWarnings("unchecked")
    private static List<IConfigElement> getConfigElements()
    {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        List<IConfigElement> temp = (new ConfigElement(Config.config.getCategory(Config.DYN_CATEGORY))).getChildElements();
        for (IConfigElement el : temp)
        {
            list.add(el);
        }
        temp = (new ConfigElement(Config.config.getCategory(Config.STATIC_CATEGORY))).getChildElements();
        for (IConfigElement el : temp)
        {
            list.add(el);
        }
        return list;
    }
}
