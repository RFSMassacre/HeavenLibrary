package com.github.rfsmassacre.heavenlibrary.velocity.commands;

import com.github.rfsmassacre.heavenlibrary.commands.HeavenCommand;
import com.github.rfsmassacre.heavenlibrary.velocity.HeavenVelocityPlugin;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.plugin.Plugin;

public abstract class SimpleVelocityCommand extends HeavenCommand<CommandSource> implements SimpleCommand
{
    protected SimpleVelocityCommand(HeavenVelocityPlugin plugin, String commandName)
    {
        super(plugin.getConfiguration(), plugin.getLocale(), ((Plugin) plugin).id(), commandName);
    }

    /**
     * When the command fails.
     * @param sender CommandSender.
     */
    @Override
    protected void onFail(CommandSource sender)
    {
        locale.sendLocale(sender, "invalid.no-perm");
    }

    /**
     * When the command does not meet the argument requirements.
     * @param sender CommandSender.
     */
    @Override
    protected void onInvalidArgs(CommandSource sender)
    {
        locale.sendLocale(sender, "invalid.command", "{command}", commandName);
    }
}
