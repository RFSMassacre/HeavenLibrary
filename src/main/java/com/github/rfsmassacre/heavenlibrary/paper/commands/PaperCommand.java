package com.github.rfsmassacre.heavenlibrary.paper.commands;

import com.github.rfsmassacre.heavenlibrary.commands.HeavenCommand;
import com.github.rfsmassacre.heavenlibrary.paper.HeavenPaperPlugin;
import com.github.rfsmassacre.heavenlibrary.paper.configs.PaperConfiguration;
import com.github.rfsmassacre.heavenlibrary.paper.configs.PaperLocale;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Easier way to set up commands for Spigot Plugins.
 */
@SuppressWarnings("unused")
public abstract class PaperCommand extends HeavenCommand<CommandSender> implements TabExecutor
{
    protected enum SoundKey
    {
        NO_PERM,
        INVALID_SUB_ARGS,
        INVALID_ARGS,
        SUCCESS,
        INCOMPLETE;

        public String getKey()
        {
            return toString().toLowerCase().replaceAll(Pattern.quote("_"), "-");
        }
    }

    public PaperCommand(HeavenPaperPlugin plugin, String commandName)
    {
        super(plugin.getConfiguration(), plugin.getLocale(), plugin.getName().toLowerCase(), commandName);
    }

    /**
     * When the sender runs a command.
     *
     * @param sender CommandSender.
     * @param command Command given by plugin.yml file.
     * @param label Label given by plugin.
     * @param args Arguments given by sender.
     * @return Boolean: Whether it succeeded or failed.
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             String[] args)
    {
        if (subCommands.isEmpty())
        {
            //All commands MUST have a main sub-command.
            //If you want a command with no arguments, set the name to a blank string.
            return false;
        }
        else if (args.length == 0)
        {
            //If no arguments are given, always run the first sub-command.
            subCommands.values().iterator().next().execute(sender, args);
            return true;
        }
        else
        {
            //If arguments are given, cycle through the right one.
            //If none found, it'll give an error defined.
            String argument = args[0].toLowerCase();
            if (subCommands.containsKey(argument))
            {
                subCommands.get(argument).execute(sender, args);
                return true;
            }
        }

        onInvalidArgs(sender);
        return true;
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
            for (String subName : subCommands.keySet())
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
            for (HeavenSubCommand subCommand : subCommands.values())
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
        subCommands.put(subCommand.getName(), subCommand);
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
        public abstract List<String> onTabComplete(CommandSender sender, String[] args);
    }

    /**
     * When the command fails.
     * @param sender CommandSender.
     */
    @Override
    protected void onFail(CommandSender sender)
    {
        locale.sendLocale(sender, "invalid.no-perm");
        playSound(sender, SoundKey.NO_PERM);
    }

    /**
     * When the command does not meet the argument requirements.
     * @param sender CommandSender.
     */
    @Override
    protected void onInvalidArgs(CommandSender sender)
    {
        locale.sendLocale(sender, "invalid.command", "{command}", commandName);
        playSound(sender, SoundKey.INVALID_ARGS);
    }

    /**
     * Play sound during a command input.
     * @param sender CommandSender.
     * @param key SoundKey.
     */
    protected void playSound(CommandSender sender, SoundKey key)
    {
        if (!(sender instanceof Player player))
        {
            return;
        }

        Sound sound = Sound.valueOf(config.getString("command-sound." + key.getKey() + ".sound"));
        float volume = (float) config.getDouble("command-sound." + key.getKey() + ".volume");
        float pitch = (float) config.getDouble("command-sound." + key.getKey() + ".pitch");
        player.playSound(player, sound, volume, pitch);
    }
}
