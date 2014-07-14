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

package net.jamcraft.chowtime.core.commands.client;

import net.jamcraft.chowtime.core.config.Config;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentTranslation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by James Hollowell on 6/22/2014.
 */
public class ChowTimeClientCommand implements ICommand
{
    @SuppressWarnings("rawtypes")
    private List aliases;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ChowTimeClientCommand()
    {
        this.aliases = new ArrayList();
        this.aliases.add("chowtimeui");
    }

    @Override
    public String getCommandName()
    {
        return "chowtimeui";
    }

    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "/chowtimeui toggleXPBar";
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
        if (astring[0].equals("toggleXPBar"))
        {
            Config.shouldRenderXP = !Config.shouldRenderXP;
            Config.save();
            commandSender.addChatMessage(new ChatComponentTranslation("chat.ctprefix").appendSibling(new ChatComponentTranslation("chat.toggleXPBar")));
        }
        else
            throw new WrongUsageException(getCommandUsage(commandSender));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender var1)
    {
        return true;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring)
    {
        final List<String> MATCHES = new LinkedList<String>();
        final String ARG_LC = astring[astring.length - 1].toLowerCase();
        if (astring.length == 1)
        {
            if ("toggleXPBar".toLowerCase().startsWith(ARG_LC))
                MATCHES.add("toggleXPBar");
        }
        return MATCHES.isEmpty() ? null : MATCHES;
    }

    @Override
    public boolean isUsernameIndex(String[] var1, int var2)
    {
        return false;
    }

    @Override
    public int compareTo(Object o)
    {
        if(o instanceof ICommand)
        {
            return this.getCommandName().compareTo(((ICommand) o).getCommandName());
        }
        return 0;
    }
}

