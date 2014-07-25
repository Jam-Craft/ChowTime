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

package net.jamcraft.chowtime.core.commands;

import net.jamcraft.chowtime.core.harvestxp.HarvestXPServer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by James Hollowell on 5/24/2014.
 */
public class ChowTimeCommand extends CommandBase
{
    @SuppressWarnings("rawtypes")
    private List aliases;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ChowTimeCommand()
    {
        this.aliases = new ArrayList();
        this.aliases.add("chowtime");
    }

    @Override
    public String getCommandName()
    {
        return "chowtime";
    }

    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        String use = "/chowtime getXP <user> | ";
        use += "/chowtime setXP <user> <xp>";
        return use;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List getCommandAliases()
    {
        return aliases;
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] astring)
    {
        if (astring.length == 0 || astring[0].equals("help"))
            throw new WrongUsageException(getCommandUsage(commandSender));
        if (astring[0].equals("getXP"))
        {
            if (astring.length != 2)
                throw new WrongUsageException(getCommandUsage(commandSender));
            commandSender.addChatMessage(new ChatComponentTranslation("chat.ctprefix").appendSibling(new ChatComponentTranslation("chat.getXP", astring[1], HarvestXPServer.INSTANCE.GetXPForUser(astring[1]))));
        }
        else if (astring[0].equals("setXP"))
        {
            if (astring.length != 3)
                throw new WrongUsageException(getCommandUsage(commandSender));
            int xp = CommandBase.parseIntWithMin(commandSender, astring[2], 0);
            HarvestXPServer.INSTANCE.SetXPForUser(MinecraftServer.getServer().getConfigurationManager().func_152612_a(astring[1]), xp);
            commandSender.addChatMessage(new ChatComponentTranslation("chat.ctprefix").appendSibling(new ChatComponentTranslation("chat.setXP", astring[1], xp)));
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring)
    {
        final List<String> MATCHES = new LinkedList<String>();
        final String ARG_LC = astring[astring.length - 1].toLowerCase();
        if (astring.length == 1)
        {
            if ("getXP".toLowerCase().startsWith(ARG_LC)) MATCHES.add("getXP");
            if ("setXP".toLowerCase().startsWith(ARG_LC)) MATCHES.add("setXP");
        }
        else if (astring.length == 2)
        {
            for (String un : MinecraftServer.getServer().getAllUsernames())
                if (un.toLowerCase().startsWith(ARG_LC)) MATCHES.add(un);
        }
        return MATCHES.isEmpty() ? null : MATCHES;
    }
}
