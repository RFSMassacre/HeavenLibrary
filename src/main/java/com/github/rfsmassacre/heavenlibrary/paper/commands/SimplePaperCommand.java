package com.github.rfsmassacre.heavenlibrary.paper.commands;

import com.github.rfsmassacre.heavenlibrary.commands.HeavenCommand;
import com.github.rfsmassacre.heavenlibrary.paper.HeavenPaperPlugin;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public abstract class SimplePaperCommand extends HeavenCommand<CommandSender> implements TabExecutor
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

    public SimplePaperCommand(HeavenPaperPlugin plugin, String commandName)
    {
        super(plugin.getConfiguration(), plugin.getLocale(), plugin.getName().toLowerCase(), commandName);
    }

    @Override
    protected boolean hasPermission(CommandSender sender, String permission)
    {
        return sender.hasPermission(permission);
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
    protected void onInvalidArgs(CommandSender sender, String... args)
    {
        String fullArgs = args.length == 0 ? "" : " " + String.join(" ", args);
        locale.sendLocale(sender, "invalid.command", "{command}", commandName + fullArgs);
        playSound(sender, SoundKey.INVALID_ARGS);
    }

    /**
     * When the command is run by the console.
     */
    @Override
    protected void onConsole(CommandSender sender)
    {
        locale.sendLocale(sender, "invalid.console");
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

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args)
    {
        if (hasPermission(sender))
        {
            onRun(sender, args);
        }
        else
        {
            onFail(sender);
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String label, @NotNull String[] strings)
    {
        return Collections.emptyList();
    }
}
