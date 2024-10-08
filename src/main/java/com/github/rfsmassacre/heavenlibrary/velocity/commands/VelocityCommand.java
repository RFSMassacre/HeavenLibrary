package com.github.rfsmassacre.heavenlibrary.velocity.commands;

import com.github.rfsmassacre.heavenlibrary.velocity.HeavenVelocityPlugin;
import com.velocitypowered.api.command.CommandSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Easier way to set up commands for Spigot Plugins.
 */
@SuppressWarnings("unused")
public class VelocityCommand extends SimpleVelocityCommand
{
    private final LinkedHashMap<String, VelocitySubCommand> subCommands;

    public VelocityCommand(HeavenVelocityPlugin plugin, String commandName)
    {
        super(plugin, commandName);

        this.subCommands = new LinkedHashMap<>();
    }

    /**
     * When the sender runs a command.
     *
     * @param invocation Invocation.
     */
    @Override
    public void execute(Invocation invocation)
    {
        if (subCommands.isEmpty())
        {
            //All commands MUST have a main sub-command.
            //If you want a command with no arguments, set the name to a blank string.
            return;
        }
        else if (invocation.arguments().length == 0)
        {
            //If no arguments are given, always run the first sub-command.
            subCommands.values().iterator().next().execute(invocation.source(), invocation.arguments());
            return;
        }
        else
        {
            //If arguments are given, cycle through the right one.
            //If none found, it'll give an error defined.
            String argument = invocation.arguments()[0].toLowerCase();
            if (subCommands.containsKey(argument))
            {
                subCommands.get(argument).execute(invocation.source(), invocation.arguments());
                return;
            }
        }

        onInvalidArgs(invocation.source());
    }

    /**
     * When the sender enters arguments.
     *
     * @param invocation Invocation.
     */
    @Override
    public List<String> suggest(Invocation invocation)
    {
        String[] args = invocation.arguments();
        if (args.length == 0)
        {
            return Collections.emptyList();
        }
        else if (args.length == 1)
        {
            //All subcommand names should be showed when typing the command in.
            List<String> suggestions = new ArrayList<>();
            for (String subName : subCommands.keySet())
            {
                HeavenSubCommand subCommand = subCommands.get(subName);
                if (subName.toLowerCase().startsWith(args[args.length - 1].toLowerCase()) &&
                        invocation.source().hasPermission(subCommand.getPermission()))
                {
                    suggestions.add(subName);
                }
            }
            return suggestions;
        }
        else
        {
            //This section should show the argument of each subcommand
            List<String> suggestions = new ArrayList<>();
            for (HeavenSubCommand subCommand : subCommands.values())
            {
                if (subCommand.getName().equalsIgnoreCase(args[0]))
                {
                    List<String> tab = subCommand.onTabComplete(invocation.source(), args);
                    if (tab == null || tab.isEmpty())
                    {
                        return Collections.emptyList();
                    }

                    for (String suggestion : tab)
                    {
                        if (suggestion.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                        {
                            suggestions.add(suggestion);
                        }
                    }
                }
            }
            return suggestions;
        }
    }

    /**
     * Adds subCommand to the map.
     * @param subCommand SubCommand.
     */
    protected void addSubCommand(VelocitySubCommand subCommand)
    {
        subCommands.put(subCommand.getName(), subCommand);
    }

    /**
     * Removes subCommand from the map.
     * @param subCommand SubCommand.
     */
    protected void removeSubCommand(VelocitySubCommand subCommand)
    {
        subCommands.remove(subCommand.getName());
    }

    /**
     * Broken down commands within a larger command in order to make running commands easier.
     * Simply implement each subCommand, add them to the map, and it will run for you.
     */
    protected abstract class VelocitySubCommand extends HeavenSubCommand
    {
        public VelocitySubCommand(String name, String permission)
        {
            super(name, permission);
        }

        public VelocitySubCommand(String name)
        {
           super(name);
        }

        /**
         * Execute onRun() or onFail() on permission.
         * @param sender CommandSender.
         * @param args Array of command arguments.
         */
        @Override
        public void execute(CommandSource sender, String[] args)
        {
            if (sender.hasPermission(this.permission))
            {
                onRun(sender, args);
            }
            else
            {
                onFail(sender);
            }
        }
    }
}
