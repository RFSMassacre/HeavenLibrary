package com.github.rfsmassacre.heavenlibrary.commands;

import com.github.rfsmassacre.heavenlibrary.interfaces.ConfigurationData;
import com.github.rfsmassacre.heavenlibrary.interfaces.LocaleData;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;

@SuppressWarnings({"unused", "rawuse"})
public abstract class HeavenCommand<T>
{
    protected final String pluginName;
    protected final String commandName;
    protected final LinkedHashMap<String, HeavenSubCommand> subCommands;
    protected final ConfigurationData<?> config;
    protected final LocaleData<T, ?> locale;

    protected HeavenCommand(ConfigurationData<?> config, LocaleData<T, ?> locale, String pluginName, String commandName)
    {
        this.pluginName = pluginName;
        this.commandName = commandName;
        this.subCommands = new LinkedHashMap<>();
        this.config = config;
        this.locale = locale;
    }

    /**
     * Adds subCommand to the map.
     * @param subCommand SubCommand.
     */
    protected void addSubCommand(HeavenSubCommand subCommand)
    {
        subCommands.put(subCommand.name, subCommand);
    }

    /**
     * Removes subCommand from the map.
     * @param subCommand SubCommand.
     */
    protected void removeSubCommand(HeavenSubCommand subCommand)
    {
        subCommands.remove(subCommand.name);
    }

    /**
     * Broken down commands within a larger command in order to make running commands easier.
     * Simply implement each subCommand, add them to the map, and it will run for you.
     */
    @Getter
    protected abstract class HeavenSubCommand
    {
        protected final String name;
        protected final String permission;

        public HeavenSubCommand(String name, String permission)
        {
            this.name =  name;
            this.permission = permission;
        }

        public HeavenSubCommand(String name)
        {
            this.name = name;
            this.permission = (pluginName == null || pluginName.isEmpty() ? "" : pluginName + ".") + commandName +
                    (name == null || name.isEmpty() ? "" : "." + name);
        }

        /**
         * Execute onRun() or onFail() on permission.
         * @param sender CommandSender.
         * @param args Array of command arguments.
         */
        public abstract void execute(T sender, String[] args);

        /**
         * When the command is run.
         * @param sender CommandSender.
         * @param args Array of arguments.
         */
        protected abstract void onRun(T sender, String[] args);

        /**
         * List of suggestions
         * @param sender CommandSender.
         * @param args Array of arguments.
         * @return List of suggestions.
         */
        public abstract List<String> onTabComplete(T sender, String[] args);
    }

    /**
     * When the command fails.
     * @param sender CommandSender.
     */
    protected abstract void onFail(T sender);

    /**
     * When the command does not meet the argument requirements.
     * @param sender CommandSender.
     */
    protected abstract void onInvalidArgs(T sender);
}
