package com.github.rfsmassacre.heavenlibrary.utils;

import java.util.HashSet;
import java.util.Set;

public abstract class HeavenTaskUtil<T, X, R>
{
    protected final T plugin;
    protected final Set<X> tasks;

    public HeavenTaskUtil(T plugin)
    {
        this.plugin = plugin;
        this.tasks = new HashSet<>();
    }

    public abstract void runAsync(Runnable var1);

    public abstract void startTimer(R runnable, long delay, long interval);

    public abstract void startTimerAsync(R runnable, long delay, long interval);

    protected abstract long getLong(String key);

    public abstract void startTimers();

    public abstract void stopTimers();
}
