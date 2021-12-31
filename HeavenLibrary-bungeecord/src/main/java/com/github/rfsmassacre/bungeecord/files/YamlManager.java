package com.github.rfsmassacre.bungeecord.files;

import com.github.rfsmassacre.heavenlibrary.interfaces.FileData;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * BungeeCord sided manager for YML files.
 */
public abstract class YamlManager implements FileData<Configuration>
{
    protected Plugin plugin;
    protected File folder;
    protected String folderName;
    protected Configuration yaml;
    protected Configuration defaultYaml;

    /**
     * Constructor for YamlManager.
     * @param plugin Plugin where files will be for.
     * @param folderName Name of folder.
     * @param fileName Name of file.
     */
    public YamlManager(Plugin plugin, String folderName, String fileName)
    {
        this.plugin = plugin;
        this.folderName = folderName;
        this.folder = new File(plugin.getDataFolder().getPath() + "/" + folderName);
        this.yaml = read(fileName);

        ConfigurationProvider provider = ConfigurationProvider.getProvider(YamlConfiguration.class);
        this.defaultYaml = provider.load(plugin.getResourceAsStream(fileName));
    }

    /**
     * Read from file and convert into whatever data or object needed.
     * @param fileName Name of file.
     * @return Data or object read from the file.
     */
    @Override
    public Configuration read(String fileName)
    {
        ConfigurationProvider provider = ConfigurationProvider.getProvider(YamlConfiguration.class);
        try
        {
            return provider.load(getFile(fileName));
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Copy a new file with format.
     * @param fileName Name of file.
     * @param overwrite Make new file over already existing file.
     */
    @Override
    public void copy(String fileName, boolean overwrite)
    {
        ConfigurationProvider provider = ConfigurationProvider.getProvider(YamlConfiguration.class);
        Configuration configuration = provider.load(plugin.getResourceAsStream(fileName));
        try
        {
            provider.save(configuration, getFile(fileName));
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * Write data of object into the file.
     * @param fileName Name of file.
     * @param configuration Configuration file.
     */
    @Override
    public void write(String fileName, Configuration configuration)
    {
        ConfigurationProvider provider = ConfigurationProvider.getProvider(YamlConfiguration.class);
        try
        {
            provider.save(configuration, getFile(fileName));
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * Delete specified file.
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
     * Retrieve file object from file name.
     * @param fileName Name of file.
     * @return File object.
     */
    @Override
    public File getFile(String fileName)
    {
        return new File(folder.getPath() + "/" + fileName + (fileName.endsWith(".yml") ? "" : ".yml"));
    }
}
