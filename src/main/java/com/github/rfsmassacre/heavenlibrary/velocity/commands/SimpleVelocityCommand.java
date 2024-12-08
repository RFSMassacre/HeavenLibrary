package com.github.rfsmassacre.heavenlibrary.velocity.commands;

import com.github.rfsmassacre.heavenlibrary.commands.HeavenCommand;
import com.github.rfsmassacre.heavenlibrary.velocity.HeavenVelocityPlugin;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class SimpleVelocityCommand extends HeavenCommand<CommandSource> implements SimpleCommand
{
    protected SimpleVelocityCommand(HeavenVelocityPlugin plugin, String commandName)
    {
        super(plugin.getConfiguration(), plugin.getLocale(), plugin.getId(), commandName);
    }

    protected abstract void onRun(CommandSource sender, String ... args);

    @Override
    public void execute(Invocation invocation)
    {
        if (hasPermission(invocation))
        {
            onRun(invocation.source(), invocation.arguments());
        }
        else
        {
            onFail(invocation.source());
        }
    }

    public List<String> suggest(SimpleCommand.Invocation invocation)
    {
        return this.onTabComplete(invocation.source(), invocation.arguments());
    }

    public CompletableFuture<List<String>> suggestAsync(SimpleCommand.Invocation invocation)
    {
        return CompletableFuture.completedFuture(suggest(invocation));
    }

    @Override
    protected boolean hasPermission(CommandSource sender, String permission)
    {
        return sender.hasPermission(permission);
    }
}
