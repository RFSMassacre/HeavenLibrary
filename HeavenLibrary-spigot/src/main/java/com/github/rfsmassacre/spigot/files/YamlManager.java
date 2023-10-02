package com.github.rfsmassacre.spigot.files;

import com.github.rfsmassacre.heavenlibrary.interfaces.FileData;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Spigot sided manager for YML files.
 */
public abstract class YamlManager implements FileData<YamlConfiguration>
{
    protected JavaPlugin plugin;
    protected File folder;
    protected String folderName;
    protected YamlConfiguration yaml;
    protected YamlConfiguration defaultYaml;

    /**
     * Constructor for YamlManager.
     *
     * @param plugin Plugin where files will be for.
     * @param folderName Name of folder.
     * @param fileName Name of file.
     */
    public YamlManager(JavaPlugin plugin, String folderName, String fileName)
    {
        this.plugin = plugin;
        this.folderName = folderName;
        this.folder = new File(plugin.getDataFolder().getPath() + "/" + folderName);
        this.yaml = read(fileName);

        InputStream stream = plugin.getResource(fileName);
        if (stream != null)
        {
            InputStreamReader reader = new InputStreamReader(stream);
            this.defaultYaml = YamlConfiguration.loadConfiguration(reader);
        }

        if (!getFile(fileName).exists())
        {
            write(fileName, defaultYaml);
        }

        this.yaml = read(fileName);
    }

    /**
     * Read from file and convert into whatever data or object needed.
     *
     * @param fileName Name of file.
     * @return Data or object read from the file.
     */
    @Override
    public YamlConfiguration read(String fileName)
    {
        return YamlConfiguration.loadConfiguration(getFile(fileName));
    }

    /**
     * Read object from file asynchronously.
     *
     * @param fileName Name of file.
     * @param callback Runnable that accepts object.
     */
    public void readAsync(String fileName, Consumer<YamlConfiguration> callback)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> callback.accept(read(fileName)));
    }

    /**
     * Copy a new file with format.
     *
     * @param fileName Name of file.
     * @param overwrite Make new file over already existing file.
     */
    @Override
    public void copy(String fileName, boolean overwrite)
    {
        InputStream stream = plugin.getResource(fileName);
        if (stream != null)
        {
            InputStreamReader reader = new InputStreamReader(stream);
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(reader);
            File file = getFile(fileName);
            if (overwrite)
            {
                write(fileName, configuration);
            }
            else if (!file.exists())
            {
                write(fileName, configuration);
            }
        }
    }

    /**
     * Write new file with internal file contents asynchronously.
     *
     * @param fileName Name of file.
     * @param overwrite Make new file over already existing file.
     */
    public void copyAsync(String fileName, boolean overwrite)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> copy(fileName, overwrite));
    }

    /**
     * Write data of object into the file.
     *
     * @param fileName Name of file.
     * @param configuration Configuration file.
     */
    @Override
    public void write(String fileName, YamlConfiguration configuration)
    {
        write(fileName, configuration, StandardCharsets.UTF_8);
    }

    /**
     * Write data of object into the file.
     *
     * @param fileName Name of file.
     * @param configuration Configuration file.
     * @param charset Character set for file write mode.
     */
    public void write(String fileName, YamlConfiguration configuration, Charset charset)
    {
        try
        {
            OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(getFile(fileName)), charset);
            BufferedWriter writer = new BufferedWriter(streamWriter);
            writer.write(configuration.saveToString());
            writer.close();
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
     * @param configuration Configuration file.
     */
    public void writeAsync(String fileName, YamlConfiguration configuration)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> write(fileName, configuration));
    }

    /**
     * Delete specified file.
     *
     * @param fileName Name of file.
     */
    @Override
    public void delete(String fileName)
    {
        File file = getFile(fileName);
        if (file.exists())
        {
            file.delete();
        }
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

    /**
     * Retrieve file object from file name.
     *
     * @param fileName Name of file.
     * @return File object.
     */
    @Override
    public File getFile(String fileName)
    {
        return new File(folder.getPath() + "/" + fileName + (fileName.endsWith(".yml") ? "" : ".yml"));
    }

    /**
     * Return all objects.
     *
     * @return All objects.
     */
    public Set<YamlConfiguration> all()
    {
        Set<YamlConfiguration> all = new HashSet<>();

        try
        {
            for (File file : folder.listFiles())
            {
                all.add(read(file.getName()));
            }
        }
        catch (NullPointerException exception)
        {
            //Do nothing
        }

        return all;
    }

    /**
     * Return all objects asynchronously.
     *
     * @param callback Runnable that accepts set of objects.
     */
    public void allAsync(Consumer<Set<YamlConfiguration>> callback)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> callback.accept(all()));
    }
}
