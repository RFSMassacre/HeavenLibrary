package com.github.rfsmassacre.heavenlibrary.velocity.managers;

import com.github.rfsmassacre.heavenlibrary.managers.GsonManager;
import com.github.rfsmassacre.heavenlibrary.velocity.HeavenVelocityPlugin;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Handles storing and reading data via Gson.
 *
 * @param <T> Class type of object to be saved or read.
 */
public abstract class VelocityGsonManager<T> extends GsonManager<T>
{
    private final HeavenVelocityPlugin plugin;

    /**
     * Constructor.
     *
     * @param folderName Name of folder where everything will be held.
     * @param clazz Class type of the object.
     */
    public VelocityGsonManager(HeavenVelocityPlugin plugin, String folderName, Class<T> clazz)
    {
        super(plugin.getDataDirectory().toFile(), folderName, clazz);

        this.plugin = plugin;
    }

    /**
     * Read object from file asynchronously.
     *
     * @param fileName Name of file.
     * @param callback Runnable that accepts object.
     */
    public void readAsync(String fileName, Consumer<T> callback)
    {
        plugin.getServer().getScheduler().buildTask(plugin, () -> callback.accept(read(fileName))).delay(1L,
                        TimeUnit.SECONDS).schedule();
    }

    /**
     * Write new file with internal file contents asynchronously.
     *
     * @param fileName Name of file.
     * @param overwrite Make new file over already existing file.
     */
    public void copyAsync(String fileName, boolean overwrite)
    {
        plugin.getServer().getScheduler().buildTask(plugin, () -> copy(fileName, overwrite)).delay(1L,
                TimeUnit.SECONDS).schedule();
    }

    /**
     * Write object to file asynchronously.
     *
     * @param fileName Name of file.
     * @param t Data or object to be updated into file.
     */
    public void writeAsync(String fileName, T t)
    {
        plugin.getServer().getScheduler().buildTask(plugin, () -> write(fileName, t)).delay(Duration.ZERO).delay(1L,
                TimeUnit.SECONDS).schedule();
    }

    /**
     * Delete specified file asynchronously.
     *
     * @param fileName Name of file.
     */
    public void deleteAsync(String fileName)
    {
        plugin.getServer().getScheduler().buildTask(plugin, () -> delete(fileName)).delay(Duration.ZERO).delay(1L,
                TimeUnit.SECONDS).schedule();
    }

    /**
     * Return all objects asynchronously.
     *
     * @param callback Runnable that accepts set of objects.
     */
    public void allAsync(Consumer<Set<T>> callback)
    {
        plugin.getServer().getScheduler().buildTask(plugin, () -> callback.accept(all())).delay(0L,
            TimeUnit.SECONDS).schedule();
    }
}
