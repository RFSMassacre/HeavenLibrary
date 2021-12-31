package com.github.rfsmassacre.bungeecord.files;

import com.github.rfsmassacre.heavenlibrary.interfaces.FileData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.*;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

/**
 * Handles storing and reading data via Gson.
 *
 * @param <T> Class type of object to be saved or read.
 */
public abstract class GsonManager<T> implements FileData<T>
{
    private Plugin plugin;
    private File folder;
    private final Class<T> clazz;

    /**
     * Constructor.
     *
     * @param plugin JavaPlugin handling this manager.
     * @param folderName Name of folder where everything will be held.
     * @param clazz Class type of the object being handled.
     */
    public GsonManager(Plugin plugin, String folderName, Class<T> clazz)
    {
        this.plugin = plugin;
        this.folder = new File(plugin.getDataFolder() + "/" + folderName);
        folder.mkdir();
        this.clazz = clazz;
    }

    /**
     * Read object from file.
     *
     * Please note that all objects inside objects have to be serializable or else you will get an exception on reading.
     *
     * @param fileName Name of file.
     * @return Object from file.
     */
    @Override
    public T read(String fileName)
    {
        File file = getFile(fileName);

        try
        {
            if (file.exists())
            {
                BufferedReader reader = Files.newBufferedReader(file.toPath());
                return new Gson().fromJson(reader, clazz);
            }
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }

        return null;
    }

    /**
     * Write brand new blank file.
     *
     * @param fileName Name of file.
     * @param overwrite Make new file over already existing file.
     */
    @Override
    public void copy(String fileName, boolean overwrite)
    {
        //Do nothing
    }

    /**
     * Write object to file.
     *
     * Please note that all objects inside objects have to be serializable or else you will get an exception on writing.
     *
     * @param fileName Name of file.
     * @param t Data or object to be updated into file.
     */
    @Override
    public void write(String fileName, T t)
    {
        File file = getFile(fileName);

        try
        {
            if (!file.exists())
            {
                file.createNewFile();
            }

            FileWriter writer = new FileWriter(file);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(t, writer);
            writer.flush();
            writer.close();
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
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
     * Retrieve file object from file name.
     *
     * @param fileName Name of file.
     * @return File object.
     */
    @Override
    public File getFile(String fileName)
    {
        return new File(folder.getPath() + "/" + fileName + (fileName.endsWith(".json") ? "" : ".json"));
    }

    /**
     * Return all objects.
     *
     * @return All objects.
     */
    public Set<T> all()
    {
        Set<T> all = new HashSet<>();

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
}
