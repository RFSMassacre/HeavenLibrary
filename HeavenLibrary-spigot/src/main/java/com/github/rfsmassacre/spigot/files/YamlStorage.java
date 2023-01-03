package com.github.rfsmassacre.spigot.files;


import com.github.rfsmassacre.heavenlibrary.interfaces.FileData;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public abstract class YamlStorage<T> implements FileData<T>
{
    protected JavaPlugin plugin;
    protected File folder;
    protected String folderName;

    /**
     * Constructor for YamlManager.
     * @param plugin Plugin where files will be for.
     * @param folderName Name of folder.
     */
    public YamlStorage(JavaPlugin plugin, String folderName)
    {
        this.plugin = plugin;
        this.folderName = folderName;
        this.folder = new File(plugin.getDataFolder().getPath() + "/" + folderName);
    }

    /**
     * Read from file and convert into whatever data or object needed.
     * @param fileName Name of file.
     * @return Data or object read from the file.
     */
    @Override
    public T read(String fileName)
    {
        return load(YamlConfiguration.loadConfiguration(getFile(fileName)));
    }

    public T read(File file)
    {
        return load(YamlConfiguration.loadConfiguration(file));
    }

    /**
     * Copy a new file with format.
     * @param fileName Name of file.
     * @param overwrite Make new file over already existing file.
     */
    @Override
    public void copy(String fileName, boolean overwrite)
    {
        //Do nothing. There is no default storage.
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

    public File[] getFiles()
    {
        return folder.listFiles();
    }

    public abstract T load(YamlConfiguration configuration);

    public abstract YamlConfiguration save(T t);
}
