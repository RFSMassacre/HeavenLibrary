package com.github.rfsmassacre.heavenlibrary.paper.commands;

import com.github.rfsmassacre.heavenlibrary.paper.HeavenPaperPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Easier way to set up commands for Spigot Plugins.
 */
@SuppressWarnings("unused")
public abstract class PaperCommand extends SimplePaperCommand
{
    protected final LinkedHashMap<String, PaperSubCommand> subCommands;

    public PaperCommand(HeavenPaperPlugin plugin, String commandName)
    {
        super(plugin, commandName);

        this.subCommands = new LinkedHashMap<>();
        registerSubCommands();
    }

    protected void registerSubCommands()
    {
        Stream.of(this.getClass().getDeclaredClasses())
                .filter(PaperSubCommand.class::isAssignableFrom)
                .forEachOrdered(clazz ->
                {
                    try
                    {
                        Constructor<?> constructor = clazz.getDeclaredConstructor(this.getClass());
                        constructor.setAccessible(true);
                        PaperSubCommand subCommand = (PaperSubCommand) constructor.newInstance(this);
                        addSubCommand(subCommand);
                    }
                    catch (Exception exception)
                    {
                        //Do nothing.
                    }
                });
    }

    /**
     * When the sender runs a command.
     *
     * @param sender CommandSender.
     * @param args Arguments given by sender.
     */
    @Override
    public void onRun(@NotNull CommandSender sender, String... args)
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
            subCommands.sequencedValues().getFirst().execute(sender, args);
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
     * @param command Command given by plugin.yml file.
     * @param alias Alias given by plugin.yml file.
     * @param args Arguments given by sender.
     * @return List of suggestions.
     */
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias,
                                      String[] args)
    {
        if (args.length == 0)
        {
            return Collections.emptyList();
        }
        else if (args.length == 1)
        {
            //All subcommand names should be showed when typing the command in.
            List<String> suggestions = new ArrayList<>();
            for (String subName : subCommands.sequencedKeySet())
            {
                HeavenSubCommand subCommand = subCommands.get(subName);
                if (subName.toLowerCase().startsWith(args[args.length - 1].toLowerCase()) &&
                        sender.hasPermission(subCommand.getPermission()))
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
            for (HeavenSubCommand subCommand : subCommands.sequencedValues())
            {
                if (subCommand.getName().equalsIgnoreCase(args[0]))
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
    protected void addSubCommand(PaperSubCommand subCommand)
    {
        subCommands.putFirst(subCommand.getName(), subCommand);
    }

    /**
     * Removes subCommand from the map.
     * @param subCommand SubCommand.
     */
    protected void removeSubCommand(PaperSubCommand subCommand)
    {
        subCommands.remove(subCommand.getName());
    }

    /**
     * Broken down commands within a larger command in order to make running commands easier.
     * Simply implement each subCommand, add them to the map, and it will run for you.
     */
    protected abstract class PaperSubCommand extends HeavenSubCommand
    {
        public PaperSubCommand(String name, String permission)
        {
            super(name, permission);
        }

        public PaperSubCommand(String name)
        {
            super(name);
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
    }
}
