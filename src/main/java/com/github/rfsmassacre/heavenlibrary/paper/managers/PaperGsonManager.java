package com.github.rfsmassacre.heavenlibrary.paper.managers;

import com.github.rfsmassacre.heavenlibrary.managers.GsonManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Handles storing and reading data via Gson.
 *
 * @param <T> Class type of object to be saved or read.
 */
@SuppressWarnings({"unused", "CallToPrintStackTrace"})
public class PaperGsonManager<T> extends GsonManager<T>
{
    protected final JavaPlugin plugin;

    /**
     * Constructor.
     *
     * @param plugin JavaPlugin handling this manager.
     * @param folderName Name of folder where everything will be held.
     * @param clazz Class type of the object.
     */
    public PaperGsonManager(JavaPlugin plugin, String folderName, Class<T> clazz)
    {
        super(plugin.getDataFolder(), folderName, clazz);

        this.plugin = plugin;
    }

    /**
     * Read object from file.
     * Please note that all objects inside objects have to be serializable or else you will get an exception on reading.
     *
     * @param fileName Name of file.
     * @return Object from file.
     */
    @Override
    public T read(String fileName)
    {
        File file = getFile(fileName);
        if (file.exists())
        {
            try (InputStream stream = new FileInputStream(file))
            {
                InputStreamReader reader = new InputStreamReader(stream);
                return gson.fromJson(gson.newJsonReader(reader), clazz);
            }
            catch (IOException exception)
            {
                exception.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Read object from file asynchronously.
     *
     * @param fileName Name of file.
     * @param callback Runnable that accepts object.
     */
    @Override
    public void readAsync(String fileName, Consumer<T> callback)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> callback.accept(read(fileName)));
    }

    @Override
    protected InputStream getResource(String fileName)
    {
        return plugin.getResource(fileName);
    }

    /**
     * Write new file with internal file contents asynchronously.
     *
     * @param fileName Name of file.
     * @param overwrite Make new file over already existing file.
     */
    @Override
    public void copyAsync(String fileName, boolean overwrite)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> copy(fileName, overwrite));
    }

    /**
     * Write object to file asynchronously.
     *
     * @param fileName Name of file.
     * @param t Data or object to be updated into file.
     */
    @Override
    public void writeAsync(String fileName, T t)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> write(fileName, t));
    }

    /**
     * Delete specified file asynchronously.
     *
     * @param fileName Name of file.
     */
    @Override
    public void deleteAsync(String fileName)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> delete(fileName));
    }

    /**
     * Return all objects asynchronously.
     *
     * @param callback Runnable that accepts set of objects.
     */
    @Override
    public void allAsync(Consumer<Set<T>> callback)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> callback.accept(all()));
    }
}
