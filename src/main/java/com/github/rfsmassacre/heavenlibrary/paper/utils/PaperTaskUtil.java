package com.github.rfsmassacre.heavenlibrary.paper.utils;

import com.github.rfsmassacre.heavenlibrary.paper.HeavenPaperPlugin;
import com.github.rfsmassacre.heavenlibrary.utils.HeavenTaskUtil;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class PaperTaskUtil extends HeavenTaskUtil<HeavenPaperPlugin, BukkitTask, BukkitRunnable>
{
    public PaperTaskUtil(HeavenPaperPlugin plugin)
    {
        super(plugin);
    }

    @Override
    public void run(Runnable runnable)
    {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, runnable);
    }

    @Override
    public void runAsync(Runnable runnable)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    @Override
    public void startTimer(BukkitRunnable runnable, long delay, long interval)
    {
        tasks.add(runnable.runTaskTimer(plugin, delay, interval));
    }

    @Override
    public void startTimerAsync(BukkitRunnable runnable, long delay, long interval)
    {
        tasks.add(runnable.runTaskTimerAsynchronously(plugin, delay, interval));
    }

    @Override
    protected long getLong(String key)
    {
        return plugin.getConfiguration().getInt("threads." + key);
    }

    @Override
    public void stopTimers()
    {
        for (BukkitTask task : tasks)
        {
            task.cancel();
        }

        tasks.clear();
    }
}
