package com.github.rfsmassacre.heavenlibrary.velocity.managers;

import com.github.rfsmassacre.heavenlibrary.managers.YamlStorage;
import com.github.rfsmassacre.heavenlibrary.velocity.HeavenVelocityPlugin;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@SuppressWarnings({"unused", "CallToPrintStackTrace"})
public abstract class VelocityYamlStorage<T> extends YamlStorage<T, CommentedConfigurationNode>
{
    protected final HeavenVelocityPlugin plugin;

    /**
     * Constructor for YamlManager.
     * @param folderName Name of folder.
     */
    public VelocityYamlStorage(HeavenVelocityPlugin plugin, String folderName)
    {
        super(plugin.getDataDirectory().toFile(), folderName);

        this.plugin = plugin;
    }

    private YamlConfigurationLoader getLoader(File file)
    {
        return YamlConfigurationLoader.builder().file(file).build();
    }

    /**
     * Read from file and convert into whatever data or object needed.
     * @param fileName Name of file.
     * @return Data or object read from the file.
     */
    @Override
    public T read(String fileName)
    {
        try
        {
            YamlConfigurationLoader loader = getLoader(getFile(fileName));
            return load(loader.load());
        }
        catch (ConfigurateException exception)
        {
            return null;
        }
    }

    /**
     * Read object from file asynchronously.
     *
     * @param fileName Name of file.
     * @param callback Runnable that accepts object.
     */
    public void readAsync(String fileName, Consumer<T> callback)
    {
        plugin.getServer().getScheduler().buildTask(plugin, () -> callback.accept(read(fileName))).delay(0L,
                TimeUnit.SECONDS).schedule();
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
            getLoader(getFile(fileName)).save(save(t));
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
    public void writeAsync(String fileName, T t)
    {
        plugin.getServer().getScheduler().buildTask(plugin, () -> write(fileName, t)).delay(0L, TimeUnit.SECONDS)
                .schedule();
    }

    /**
     * Delete specified file asynchronously.
     *
     * @param fileName Name of file.
     */
    public void deleteAsync(String fileName)
    {
        plugin.getServer().getScheduler().buildTask(plugin, () -> delete(fileName)).delay(0L, TimeUnit.SECONDS)
                .schedule();
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
        plugin.getServer().getScheduler().buildTask(plugin, () -> callback.accept(all())).delay(0L,
                TimeUnit.SECONDS);
    }
}
