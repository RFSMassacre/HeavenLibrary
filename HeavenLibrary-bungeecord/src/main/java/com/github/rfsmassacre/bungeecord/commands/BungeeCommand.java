package com.github.rfsmassacre.bungeecord.commands;

import com.github.rfsmassacre.bungeecord.files.configs.Locale;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Easier way to set up commands for Spigot Plugins.
 */
public abstract class BungeeCommand extends Command implements TabExecutor
{
    protected Locale locale;
    protected String commandName;
    private LinkedHashMap<String, SubCommand> subCommands;

    /**
     * Setup locale and command name when instantiating.
     * @param locale Locale.
     * @param commandName Command name.
     */
    public BungeeCommand(Locale locale, String commandName)
    {
        super(commandName);
        this.locale = locale;
        this.commandName = commandName;
        this.subCommands = new LinkedHashMap<>();
    }

    /**
     * When the sender runs a command.
     * @param sender CommandSender.
     * @param args Arguments given by sender.
     */
    @Override
    public void execute(CommandSender sender, String[] args)
    {
        if (subCommands.isEmpty())
        {
            //All commands MUST have a main sub-command.
            //If you want a command with no arguments, set the name to a blank string.
            return;
        }
        else if (args.length == 0)
        {
            //If no arguments are given, always run the first sub-command.
            subCommands.values().iterator().next().execute(sender, args);
            return;
        }
        else
        {
            //If arguments are given, cycle through the right one.
            //If none found, it'll give an error defined.
            String argument = args[0].toLowerCase();
            if (subCommands.containsKey(argument))
            {
                subCommands.get(argument).execute(sender, args);
                return;
            }
        }

        onInvalidArgs(sender);
    }

    /**
     * When the sender enters arguments.
     * @param sender CommandSender.
     * @param args Arguments given by sender.
     * @return List of suggestions.
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args)
    {
        if (args.length == 0)
        {
            return Collections.emptyList();
        }
        else if (args.length == 1)
        {
            //All subcommand names should be showed when typing the command in.
            List<String> suggestions = new ArrayList<>();
            for (String subCommand : subCommands.keySet())
            {
                if (subCommand.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                {
                    suggestions.add(subCommand);
                }
            }
            return suggestions;
        }
        else
        {
            //This section should show the argument of each subcommand
            List<String> suggestions = new ArrayList<>();
            for (SubCommand subCommand : subCommands.values())
            {
                if (subCommand.name.equalsIgnoreCase(args[0]))
                {
                    List<String> tab = subCommand.onTabComplete(sender, args);
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
    protected void addSubCommand(SubCommand subCommand)
    {
        subCommands.put(subCommand.name, subCommand);
    }

    /**
     * Removes subCommand from the map.
     * @param subCommand SubCommand.
     */
    protected void removeSubCommand(SubCommand subCommand)
    {
        subCommands.remove(subCommand.name);
    }

    /**
     * Broken down commands within a larger command in order to make running commands easier.
     * Simply implement each subCommand, add them to the map, and it will run for you.
     */
    protected abstract class SubCommand
    {
        private String name;
        private String permission;

        public SubCommand(String name, String permission)
        {
            this.name = name;
            this.permission = permission;
        }

        /**
         * Check if sender is a player. (This assumes the opposite is console.)
         * @param sender CommandSender.
         * @return Boolean: Whether it is the opposite of a player.
         */
        public boolean isConsole(CommandSender sender)
        {
            return !(sender instanceof ProxiedPlayer);
        }

        /**
         * Execute onRun() or onFail() on permission.
         * @param sender CommandSender.
         * @param args Array of command arguments.
         */
        public void execute(CommandSender sender, String[] args)
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

        /**
         * When the command is run.
         * @param sender CommandSender.
         * @param args Array of arguments.
         */
        protected abstract void onRun(CommandSender sender, String[] args);

        /**
         * List of suggestions
         * @param sender CommandSender.
         * @param args Array of arguments.
         * @return List of suggestions.
         */
        protected abstract List<String> onTabComplete(CommandSender sender, String[] args);
    }

    /**
     * When the command fails.
     * @param sender CommandSender.
     */
    protected abstract void onFail(CommandSender sender);

    /**
     * When the command does not meet the argument requirements.
     * @param sender CommandSender.
     */
    protected abstract void onInvalidArgs(CommandSender sender);
}
