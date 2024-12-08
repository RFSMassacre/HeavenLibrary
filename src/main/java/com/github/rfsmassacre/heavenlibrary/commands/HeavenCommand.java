package com.github.rfsmassacre.heavenlibrary.commands;

import com.github.rfsmassacre.heavenlibrary.interfaces.ConfigurationData;
import com.github.rfsmassacre.heavenlibrary.interfaces.LocaleData;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unused", "rawuse"})
@Getter
public abstract class HeavenCommand<T>
{
    protected final String pluginName;
    protected final String commandName;
    protected final String permission;
    protected final ConfigurationData<?> config;
    protected final LocaleData<T, ?> locale;

    protected HeavenCommand(ConfigurationData<?> config, LocaleData<T, ?> locale, String pluginName, String commandName)
    {
        this.pluginName = pluginName;
        this.commandName = commandName;
        this.permission = pluginName + "." + commandName;
        this.config = config;
        this.locale = locale;
    }

    protected boolean hasPermission(T sender)
    {
        return hasPermission(sender, permission);
    }

    protected abstract boolean hasPermission(T sender, String permission);

    protected abstract void onRun(T sender, String... args);

    protected void onFail(T sender)
    {
        this.locale.sendLocale(sender, "invalid.no-perm");
    }

    protected void onConsole(T sender)
    {
        this.locale.sendLocale(sender, "invalid.console");
    }

    protected void onInvalidArgs(T sender, String ... args)
    {
        this.locale.sendLocale(sender, "invalid.args", "{command}", commandName +
                (args.length > 0 ? " " + String.join(" ", args) : ""));
    }

    protected List<String> onTabComplete(T sender, String[] args)
    {
        return Collections.emptyList();
    }

    @Getter
    protected abstract class HeavenSubCommand
    {
        protected final String name;
        protected final String permission;

        public HeavenSubCommand(String name, String permission) {
            this.name = name;
            this.permission = permission;
        }

        public HeavenSubCommand(String name)
        {
            this(name, HeavenCommand.this.permission + (name == null || name.isEmpty() ? "" : "." + name));
        }

        protected void execute(T sender, String[] args)
        {
            if (hasPermission(sender, permission))
            {
                this.onRun(sender, args);
            }
            else
            {
                HeavenCommand.this.onFail(sender);
            }
        }

        protected abstract void onRun(T sender, String ... args);

        protected void onInvalidArgs(T sender, String ... args)
        {
            List<String> allArgs = new ArrayList<>(List.of(args));
            allArgs.addFirst(this.name);
            HeavenCommand.this.onInvalidArgs(sender, allArgs.toArray(new String[0]));
        }

        public List<String> onTabComplete(T sender, String[] args)
        {
            return Collections.emptyList();
        }
    }
}
