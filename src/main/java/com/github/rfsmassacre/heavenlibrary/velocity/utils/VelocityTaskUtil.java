package com.github.rfsmassacre.heavenlibrary.velocity.utils;

import com.github.rfsmassacre.heavenlibrary.utils.HeavenTaskUtil;
import com.github.rfsmassacre.heavenlibrary.velocity.HeavenVelocityPlugin;
import com.velocitypowered.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

public abstract class VelocityTaskUtil extends HeavenTaskUtil<HeavenVelocityPlugin, ScheduledTask, Runnable>
{
    public VelocityTaskUtil(HeavenVelocityPlugin plugin)
    {
        super(plugin);
    }

    @Override
    public void run(Runnable runnable)
    {
        runAsync(runnable);
    }

    @Override
    public void runAsync(Runnable runnable)
    {
        plugin.getServer().getScheduler().buildTask(this.plugin, runnable).schedule();
    }

    @Override
    public void startTimer(Runnable runnable, long delay, long interval)
    {
        tasks.add(plugin.getServer().getScheduler().buildTask(this.plugin, runnable)
                .delay(delay, TimeUnit.SECONDS)
                .repeat(interval, TimeUnit.SECONDS)
                .schedule());
    }

    @Override
    public void startTimerAsync(Runnable runnable, long delay, long interval)
    {
        startTimer(runnable, delay, interval);
    }

    @Override
    protected long getLong(String key)
    {
        return this.plugin.getConfiguration().getLong("threads." + key);
    }

    @Override
    public void stopTimers()
    {
        for (ScheduledTask task : this.tasks)
        {
            task.cancel();
        }

        this.tasks.clear();
    }
}
