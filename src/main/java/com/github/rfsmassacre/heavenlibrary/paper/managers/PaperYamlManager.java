package com.github.rfsmassacre.heavenlibrary.paper.managers;

import com.github.rfsmassacre.heavenlibrary.managers.YamlManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

/**
 * Spigot sided manager for YML files.
 */
@SuppressWarnings({"unused", "CallToPrintStackTrace"})
public abstract class PaperYamlManager extends YamlManager<FileConfiguration>
{
    protected final JavaPlugin plugin;

    /**
     * Constructor for YamlManager.
     *
     * @param folderName Name of folder.
     * @param fileName Name of file.
     */
    public PaperYamlManager(JavaPlugin plugin, String folderName, String fileName)
    {
        super(plugin.getDataFolder(), folderName, fileName);

        this.plugin = plugin;
        InputStream stream = plugin.getResource(fileName);
        if (stream != null)
        {
            InputStreamReader reader = new InputStreamReader(stream);
            this.defaultYaml = YamlConfiguration.loadConfiguration(reader);
        }

        if (!getFile().exists())
        {
            write(defaultYaml);
        }

        reload();
    }

    /**
     * Read from file and convert into whatever data or object needed.
     *
     * @return Data or object read from the file.
     */
    @Override
    public FileConfiguration read()
    {
        return YamlConfiguration.loadConfiguration(getFile());
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
     * Copy a new file with format.
     * @param overwrite Make new file over already existing file.
     */
    @Override
    public void copy(boolean overwrite)
    {
        InputStream stream = plugin.getResource(fileName);
        if (stream != null)
        {
            InputStreamReader reader = new InputStreamReader(stream);
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(reader);
            File file = getFile();
            if (overwrite)
            {
                write(configuration);
            }
            else if (!file.exists())
            {
                write(configuration);
            }
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
