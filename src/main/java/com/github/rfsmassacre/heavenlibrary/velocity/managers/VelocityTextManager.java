package com.github.rfsmassacre.heavenlibrary.velocity.managers;

import com.github.rfsmassacre.heavenlibrary.managers.TextManager;
import com.github.rfsmassacre.heavenlibrary.velocity.HeavenVelocityPlugin;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class VelocityTextManager extends TextManager
{
    private final HeavenVelocityPlugin plugin;

    public VelocityTextManager(HeavenVelocityPlugin plugin, String folderName)
    {
        super(plugin.getDataDirectory().toFile(), folderName);

        this.plugin = plugin;
    }

    @Override
    public void readAsync(String fileName, Consumer<List<String>> callback)
    {
        plugin.getServer().getScheduler().buildTask(plugin, () -> callback.accept(read(fileName))).delay(0L,
                TimeUnit.SECONDS).schedule();
    }

    @Override
    public void writeAsync(String fileName, List<String> strings)
    {
        plugin.getServer().getScheduler().buildTask(plugin, () -> write(fileName, strings)).delay(0L,
                TimeUnit.SECONDS).schedule();
    }

    @Override
    public void deleteAsync(String fileName)
    {
        plugin.getServer().getScheduler().buildTask(plugin, () -> delete(fileName)).delay(0L,
                TimeUnit.SECONDS).schedule();
    }

    @Override
    public void allAsync(Consumer<Set<List<String>>> callback)
    {
        plugin.getServer().getScheduler().buildTask(plugin, () -> callback.accept(all())).delay(0L,
                TimeUnit.SECONDS).schedule();
    }
}
