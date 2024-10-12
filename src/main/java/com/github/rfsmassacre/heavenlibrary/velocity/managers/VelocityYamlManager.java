package com.github.rfsmassacre.heavenlibrary.velocity.managers;

import com.github.rfsmassacre.heavenlibrary.managers.YamlManager;
import com.github.rfsmassacre.heavenlibrary.velocity.HeavenVelocityPlugin;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Spigot sided manager for YML files.
 */
@SuppressWarnings({"unused", "CallToPrintStackTrace"})
public abstract class VelocityYamlManager extends YamlManager<CommentedConfigurationNode>
{
    protected final HeavenVelocityPlugin plugin;
    private YamlConfigurationLoader loader;

    /**
     * Constructor for YamlManager.
     *
     * @param folderName Name of folder.
     * @param fileName Name of file.
     */
    public VelocityYamlManager(HeavenVelocityPlugin plugin, String folderName, String fileName)
    {
        super(plugin.getDataDirectory().toFile(), folderName, fileName);

        this.plugin = plugin;

        URL url = plugin.getClass().getResource(File.separator + fileName);
        if (url == null)
        {
            plugin.getLogger().info("URL is not defined for " + fileName);
            return;
        }


        File file = getFile();
        loader = YamlConfigurationLoader.builder().file(file).build();
        try
        {
            this.defaultYaml = YamlConfigurationLoader.builder().url(url).build().load();
        }
        catch (ConfigurateException exception)
        {
            return;
        }

        if (!file.exists())
        {
            write(defaultYaml);
        }

        this.yaml = read();
    }

    protected String[] splitKeys(String key)
    {
        return key.split("\\.");
    }

    /**
     * Read from file and convert into whatever data or object needed.
     *
     * @return Data or object read from the file.
     */
    @Override
    public CommentedConfigurationNode read()
    {
        try
        {
            return loader.load();
        }
        catch (ConfigurateException exception)
        {
            return null;
        }
    }

    /**
     * Read object from file asynchronously.
     *
     * @param callback Runnable that accepts object.
     */
    @Override
    public void readAsync(Consumer<CommentedConfigurationNode> callback)
    {
        plugin.getServer().getScheduler().buildTask(plugin, () -> callback.accept(read())).delay(1L,
                TimeUnit.SECONDS).schedule();
    }

    /**
     * Copy a new file with format.
     *
     * @param overwrite Make new file over already existing file.
     */
    @Override
    public void copy(boolean overwrite)
    {
        if (overwrite)
        {
            write(defaultYaml);
        }
        else if (!getFile().exists())
        {
            write(defaultYaml);
        }
    }

    /**
     * Write new file with internal file contents asynchronously.
     *
     * @param overwrite Make new file over already existing file.
     */
    @Override
    public void copyAsync(boolean overwrite)
    {
        plugin.getServer().getScheduler().buildTask(plugin, () -> copy(overwrite)).delay(0L,
                TimeUnit.SECONDS).schedule();
    }

    /**
     * Write data of object into the file.
     *
     * @param node CommentedConfigurationNode file.
     */
    @Override
    public void write(CommentedConfigurationNode node)
    {
        try
        {
            this.loader.save(node);
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * Write object to file asynchronously.
     *
     * @param node CommentedConfigurationNode file.
     */
    @Override
    public void writeAsync(CommentedConfigurationNode node)
    {
        plugin.getServer().getScheduler().buildTask(plugin, () -> write(node)).delay(0L,
                TimeUnit.SECONDS).schedule();
    }

    /**
     * Delete specified file asynchronously.
     *
     */
    @Override
    public void deleteAsync()
    {
        plugin.getServer().getScheduler().buildTask(plugin, this::delete).delay(0L, TimeUnit.SECONDS).schedule();
    }
}