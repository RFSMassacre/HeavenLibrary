package com.github.rfsmassacre.heavenlibrary.velocity.managers;

import com.github.rfsmassacre.heavenlibrary.managers.YamlManager;
import com.github.rfsmassacre.heavenlibrary.velocity.HeavenLibraryVelocity;
import com.github.rfsmassacre.heavenlibrary.velocity.HeavenVelocityPlugin;
import com.github.rfsmassacre.heavenlibrary.velocity.configs.VelocityConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * Spigot sided manager for YML files.
 */
@SuppressWarnings("all")
public abstract class VelocityYamlManager extends YamlManager<CommentedConfigurationNode, ConfigurationNode>
{
    protected static String[] splitKeys(String key)
    {
        return key.split(Pattern.quote("."));
    }

    protected final HeavenVelocityPlugin plugin;
    private final YamlConfigurationLoader defaultLoader;
    private final YamlConfigurationLoader loader;

    /**
     * Constructor for YamlManager.
     *
     * @param folderName Name of folder.
     * @param fileName Name of file.
     */
    public VelocityYamlManager(HeavenVelocityPlugin plugin, String folderName, String fileName)
    {
        this(plugin, folderName, fileName, false);
    }

    public VelocityYamlManager(HeavenVelocityPlugin plugin, String folderName, String fileName, boolean update)
    {
        super(plugin.getDataDirectory().toFile(), folderName, fileName);

        this.plugin = plugin;
        URL url = plugin.getClass().getResource(File.separator + fileName);
        this.defaultLoader = YamlConfigurationLoader.builder()
                .nodeStyle(NodeStyle.BLOCK)
                .defaultOptions(ConfigurationOptions.defaults())
                .url(url)
                .build();
        this.loader = YamlConfigurationLoader.builder()
                .nodeStyle(NodeStyle.BLOCK)
                .defaultOptions(ConfigurationOptions.defaults())
                .file(getFile())
                .build();
        reload(update);
    }

    @Override
    protected <F extends YamlManager<CommentedConfigurationNode, ConfigurationNode>> F getLibraryYaml(Class<F> clazz)
    {
        return HeavenLibraryVelocity.getInstance().getYamlManager(fileName, clazz);
    }

    @Override
    protected boolean hasKey(String key)
    {
        return !yaml.node(splitKeys(key)).virtual() || !defaultYaml.node(splitKeys(key)).virtual();
    }

    @Override
    protected boolean hasList(String key)
    {
        return yaml.node(splitKeys(key)).isList() || defaultYaml.node(splitKeys(key)).isList();
    }

    @Override
    protected <T> T get(String key, Class<T> clazz)
    {
        if (plugin instanceof HeavenLibraryVelocity || hasKey(key))
        {
            try
            {
                T t = yaml.node(splitKeys(key)).get(clazz);
                if (t == null)
                {
                    this.defaultYaml.node(splitKeys(key)).get(clazz);
                }

                return t;
            }
            catch (Exception exception)
            {
                return null;
            }
        }

        VelocityYamlManager library = this.getLibraryYaml(VelocityYamlManager.class);
        if (library != null)
        {
            try
            {
                return library.get(key, clazz);
            }
            catch (Exception exception)
            {
                return null;
            }
        }

        return null;
    }

    @Override
    protected <T> List<T> getList(String key, Class<T> clazz)
    {
        if (plugin instanceof HeavenLibraryVelocity || this.hasKey(key))
        {
            try
            {
                List<T> list = this.yaml.node(splitKeys(key)).getList(clazz);
                if (list == null)
                {
                    list = this.defaultYaml.node(splitKeys(key)).getList(clazz);
                }

                return list;
            }
            catch (Exception exception)
            {
                return null;
            }
        }

        VelocityConfiguration library = this.getLibraryYaml(VelocityConfiguration.class);
        if (library != null)
        {
            try
            {
                return library.getList(key, clazz);
            }
            catch (Exception exception)
            {
                return null;
            }
        }

        return null;
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
                ConfigurationNode destinationChild = destination.node(splitKeys(key));
                copySection(child, destinationChild, null);
                continue;
            }

            if (!destination.node(splitKeys(key)).virtual())
            {
                continue;
            }

            try
            {
                destination.node(new Object[]{key}).set(child.raw());
                if (!(child instanceof CommentedConfigurationNode commentedSource))
                {
                    continue;
                }

                if (!(destination instanceof CommentedConfigurationNode commentedDestination))
                {
                    continue;
                }

                String comment = commentedSource.comment();
                if (comment == null)
                {
                    continue;
                }

                commentedDestination.node(commentedDestination).comment(comment);
            }
            catch (SerializationException exception)
            {
                //Do nothing
            }
        }
    }
}