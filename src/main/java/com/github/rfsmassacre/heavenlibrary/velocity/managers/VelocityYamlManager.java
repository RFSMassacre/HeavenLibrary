package com.github.rfsmassacre.heavenlibrary.velocity.managers;

import com.github.rfsmassacre.heavenlibrary.managers.YamlManager;
import com.github.rfsmassacre.heavenlibrary.velocity.HeavenVelocityPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
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
public abstract class VelocityYamlManager extends YamlManager<CommentedConfigurationNode, ConfigurationNode>
{
    protected final HeavenVelocityPlugin plugin;
    private final YamlConfigurationLoader loader;

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
        this.loader = YamlConfigurationLoader.builder().file(getFile()).build();
        copy(false);
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
     * Read from inside the jar and convert into whatever data or object needed.
     *
     * @return Data or object read from inside the jar.
     */
    @Override
    public CommentedConfigurationNode readDefault()
    {
        URL url = plugin.getClass().getResource(File.separator + fileName);
        if (url == null)
        {
            plugin.getLogger().info("URL is not defined for " + fileName);
            return null;
        }

        try
        {
            return YamlConfigurationLoader.builder().url(url).build().load();
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

    /**
     * Provide an easy function to transfer new values from the default file to the new one without
     * overwriting valid values.
     */
    @Override
    public void update()
    {
        copySection(defaultYaml, yaml, null);
    }

    /**
     * Copy section of default file to new file.
     */
    @Override
    public void copySection(ConfigurationNode source, ConfigurationNode destination, String path)
    {
        for (ConfigurationNode child : source.childrenMap().values())
        {
            Object object = child.key();
            if (object == null)
            {
                continue;
            }

            String key = object.toString();
            if (child.isMap())
            {
                // Handle nested sections
                ConfigurationNode destinationChild = destination.node(key);
                copySection(child, destinationChild, null);
            }
            else
            {
                // Only copy value if destination does not already contain it
                if (destination.node(key).virtual())
                {
                    try
                    {
                        destination.node(key).set(child.raw());
                        if (child instanceof CommentedConfigurationNode commentedSource &&
                                destination instanceof CommentedConfigurationNode commentedDestination)
                        {
                            // Copy comments if present
                            String comment = commentedSource.comment();
                            if (comment != null)
                            {
                                commentedDestination.node(key).comment(comment);
                            }
                        }
                    }
                    catch (SerializationException exception)
                    {
                        //Do nothing
                    }
                }
            }
        }
    }
}