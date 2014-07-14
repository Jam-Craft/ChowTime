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

package net.jamcraft.chowtime.core;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import net.jamcraft.chowtime.core.client.gui.GuiFermenter;
import net.jamcraft.chowtime.core.client.gui.GuiICMaker;
import net.jamcraft.chowtime.core.client.gui.GuiJuicer;
import net.jamcraft.chowtime.core.container.ContainerFermenter;
import net.jamcraft.chowtime.core.container.ContainerICMaker;
import net.jamcraft.chowtime.core.container.ContainerJuicer;
import net.jamcraft.chowtime.core.tileentities.TEFermenter;
import net.jamcraft.chowtime.core.tileentities.TEIceCreamMaker;
import net.jamcraft.chowtime.core.tileentities.TEJuicer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

/**
 * Created by James Hollowell on 5/14/2014.
 */
public class CommonProxy implements IGuiHandler
{
    public void registerRenderers()
    {
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == GuiIDS.Fermenter_Gui)
        {
            TEFermenter te = (TEFermenter) world.getTileEntity(x, y, z);
            return new ContainerFermenter(player.inventory, te);
        }
        if (ID == GuiIDS.Juicer_Gui)
        {
            TEJuicer te = (TEJuicer) world.getTileEntity(x, y, z);
            return new ContainerJuicer(player.inventory, te);
        }
        if (ID == GuiIDS.ICMaker_Gui)
        {
            TEIceCreamMaker te = (TEIceCreamMaker) world.getTileEntity(x, y, z);
            return new ContainerICMaker(player.inventory, te);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == GuiIDS.Fermenter_Gui)
        {
            TEFermenter te = (TEFermenter) world.getTileEntity(x, y, z);
            return new GuiFermenter(player.inventory, te);
        }
        if (ID == GuiIDS.Juicer_Gui)
        {
            TEJuicer te = (TEJuicer) world.getTileEntity(x, y, z);
            return new GuiJuicer(player.inventory, te);
        }
        if (ID == GuiIDS.ICMaker_Gui)
        {
            TEIceCreamMaker te = (TEIceCreamMaker) world.getTileEntity(x, y, z);
            return new GuiICMaker(player.inventory, te);
        }
        return null;
    }

    public void openBook()
    {
    }

    // From MachineMuse's PowerSuits mod
    public static boolean shouldAddAdditionalInfo()
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
        {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
            {
                return true;
            }
        }
        return false;
    }

    public static Object additionalInfoInstructions()
    {
        return EnumChatFormatting.ITALIC.toString() + "Press " + EnumChatFormatting.AQUA.toString() + EnumChatFormatting.ITALIC.toString() + "<SHIFT>" + EnumChatFormatting.GRAY.toString() + EnumChatFormatting.ITALIC.toString() + " for more information.";
    }
}
