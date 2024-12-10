package com.github.rfsmassacre.heavenlibrary.paper.managers;

import com.github.rfsmassacre.heavenlibrary.managers.YamlManager;
import com.github.rfsmassacre.heavenlibrary.paper.HeavenLibraryPaper;
import com.github.rfsmassacre.heavenlibrary.paper.configs.PaperConfiguration;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Spigot sided manager for YML files.
 */
@SuppressWarnings({"unused", "CallToPrintStackTrace"})
public abstract class PaperYamlManager extends YamlManager<FileConfiguration, ConfigurationSection>
{
    protected final JavaPlugin plugin;

    /**
     * Constructor for YamlManager.
     *
     * @param folderName Name of folder.
     * @param fileName   Name of file.
     */
    public PaperYamlManager(JavaPlugin plugin, String folderName, String fileName)
    {
        this(plugin, folderName, fileName, false);
    }

    public PaperYamlManager(JavaPlugin plugin, String folderName, String fileName, boolean update)
    {
        super(plugin.getDataFolder(), folderName, fileName);

        this.plugin = plugin;
        reload(update);
    }

    @Override
    protected <F extends YamlManager<FileConfiguration, ConfigurationSection>> F getLibraryYaml(Class<F> clazz)
    {
        return HeavenLibraryPaper.getInstance().getYamlManager(fileName, clazz);
    }

    @Override
    protected boolean hasKey(String key)
    {
        if (!yaml.contains(key))
        {
            return defaultYaml.contains(key);
        }

        return true;
    }

    @Override
    protected boolean hasList(String key)
    {
        if (!yaml.isList(key))
        {
            return defaultYaml.isList(key);
        }

        return true;
    }

    @Override
    protected <T> T get(String key, Class<T> clazz)
    {
        if (plugin instanceof HeavenLibraryPaper || this.hasKey(key))
        {
            return yaml.getObject(key, clazz, defaultYaml.getObject(key, clazz));
        }

        PaperConfiguration library = getLibraryYaml(PaperConfiguration.class);
        if (library != null)
        {
            return library.get(key, clazz);
        }

        return null;
    }

    @Override
    protected <T> List<T> getList(String key, Class<T> clazz)
    {
        List<T> list = new ArrayList<>();
        List<?> rawList = null;
        if (plugin instanceof HeavenLibraryPaper || hasList(key))
        {
            rawList = yaml.getList(key, defaultYaml.getList(key));
        }
        else
        {
            PaperConfiguration library = this.getLibraryYaml(PaperConfiguration.class);
            if (library != null)
            {
                rawList = library.getList(key, clazz);
            }
        }

        if (rawList != null)
        {
            for (Object object : rawList)
            {
                if (clazz.isInstance(object))
                {
                    list.add(clazz.cast(object));
                }
            }
        }

        return list;
    }

    /**
     * Read from file and convert into whatever data or object needed.
     *
     * @return Data or object read from the file.
     */
    @Override
    public FileConfiguration read()
    {
        File file = getFile();
        if (file != null)
        {
            return YamlConfiguration.loadConfiguration(file);
        }

        return null;
    }

    /**
     * Read from inside the jar and convert into whatever data or object needed.
     *
     * @return Data or object read from inside the jar.
     */
    @Override
    public FileConfiguration readDefault()
    {
        InputStream stream = plugin.getResource(fileName);
        if (stream != null)
        {
            InputStreamReader reader = new InputStreamReader(stream);
            return YamlConfiguration.loadConfiguration(reader);
        }

        return null;
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

    @Override
    public void copySection(ConfigurationSection source, ConfigurationSection destination, String path)
    {
        for (String key : source.getKeys(false))
        {
            String fullPath = (path == null ? key : path + "." + key);

            if (source.isConfigurationSection(key))
            {
                // If the key is a section, recurse into it
                ConfigurationSection sourceSection = source.getConfigurationSection(key);
                if (sourceSection == null)
                {
                    continue;
                }

                ConfigurationSection destinationSection = destination.getConfigurationSection(key);
                if (destinationSection == null)
                {
                    destinationSection = destination.createSection(key);
                }

                copySection(sourceSection, destinationSection, fullPath);
            }
            else
            {
                // Copy only if the key does not exist or is null in the destination
                if (!destination.contains(fullPath) || destination.get(fullPath) == null)
                {
                    destination.set(fullPath, source.get(key));
                }
            }
        }
    }

    /**
     * Read object from file asynchronously.
     *
     * @param callback Runnable that accepts object.
     */
    @Override
    public void readAsync(Consumer<FileConfiguration> callback)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> callback.accept(read()));
    }

    /**
     * Write new file with internal file contents asynchronously.
     *
     * @param overwrite Make new file over already existing file.
     */
    @Override
    public void copyAsync(boolean overwrite)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> copy(overwrite));
    }

    /**
     * Write data of object into the file.
     *
     * @param configuration Configuration file.
     */
    @Override
    public void write(FileConfiguration configuration)
    {
        try
        {
            configuration.save(getFile());
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    @Override
    public void writeAsync(FileConfiguration configuration)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> write(configuration));
    }

    @Override
    public void deleteAsync()
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, this::delete);
    }

    /**
     * Write object to file asynchronously.
     *
     * @param fileName Name of file.
     * @param configuration Configuration file.
     */
    public void writeAsync(String fileName, FileConfiguration configuration)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> write(configuration));
    }

    /**
     * Delete specified file asynchronously.
     *
     * @param fileName Name of file.
     */
    public void deleteAsync(String fileName)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, this::delete);
    }
}
