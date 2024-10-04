package com.github.rfsmassacre.heavenlibrary.paper.managers;


import com.github.rfsmassacre.heavenlibrary.managers.YamlStorage;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@SuppressWarnings({"unused", "CallToPrintStackTrace"})
public abstract class PaperYamlStorage<T> extends YamlStorage<T, YamlConfiguration>
{
    protected final JavaPlugin plugin;

    /**
     * Constructor for YamlManager.
     * @param plugin Plugin where files will be for.
     * @param folderName Name of folder.
     */
    public PaperYamlStorage(JavaPlugin plugin, String folderName)
    {
        super(plugin.getDataFolder(), folderName);

        this.plugin = plugin;
    }

    /**
     * Read from file and convert into whatever data or object needed.
     * @param fileName Name of file.
     * @return Data or object read from the file.
     */
    @Override
    public T read(String fileName)
    {
        File file = getFile(fileName);
        if (file.exists())
        {
            return load(YamlConfiguration.loadConfiguration(file));
        }

        return null;
    }

    /**
     * Read object from file asynchronously.
     *
     * @param fileName Name of file.
     * @param callback Runnable that accepts object.
     */
    public void readAsync(String fileName, Consumer<T> callback)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> callback.accept(read(fileName)));
    }

    /**
     * Write data of object into the file.
     * @param fileName Name of file.
     * @param t Generic type.
     */
    @Override
    public void write(String fileName, T t)
    {
        try
        {
            save(t).save(getFile(fileName));
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * Write object to file asynchronously.
     *
     * @param fileName Name of file.
     * @param t Generic type.
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
    public void deleteAsync(String fileName)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> delete(fileName));
    }

    @Override
    public Set<T> all()
    {
        Set<T> all = new HashSet<>();
        File[] files = folder.listFiles();
        if (files == null)
        {
            return all;
        }

        for (File file : files)
        {
            all.add(read(file.getName()));
        }

        return all;
    }

    @Override
    public void allAsync(Consumer<Set<T>> callback)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> callback.accept(all()));
    }
}
