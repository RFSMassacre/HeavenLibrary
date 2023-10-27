package com.github.rfsmassacre.spigot.files;

import com.github.rfsmassacre.spigot.utils.RuntimeTypeAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.github.rfsmassacre.heavenlibrary.interfaces.FileData;
import com.google.gson.JsonObject;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Consumer;

/**
 * Handles storing and reading data via Gson.
 *
 * @param <T> Class type of object to be saved or read.
 */
public abstract class GsonManager<T> implements FileData<T>
{
    protected final JavaPlugin plugin;
    private final File folder;
    protected final Class<T> clazz;
    private final Set<RuntimeTypeAdapterFactory<T>> adapters;
    private Gson gson;

    /**
     * Constructor.
     *
     * @param plugin JavaPlugin handling this manager.
     * @param folderName Name of folder where everything will be held.
     * @param clazz Class type of the object.
     * @param childClasses Class type of object's children.
     */
    @SafeVarargs
    public GsonManager(JavaPlugin plugin, String folderName, Class<T> clazz, Class<? extends T>... childClasses)
    {
        this.plugin = plugin;
        this.folder = new File(plugin.getDataFolder() + "/" + folderName);
        folder.mkdirs();
        this.clazz = clazz;
        this.adapters = new HashSet<>();
        rebuildGson();
    }

    /**
     * Register child class.
     * @param childCLass Child class.
     */
    public void registerType(Class<? extends T> childCLass)
    {
        RuntimeTypeAdapterFactory<T> adapter = RuntimeTypeAdapterFactory
                .of(clazz, "type")
                .registerSubtype(childCLass, childCLass.getSimpleName());
        this.adapters.add(adapter);
        rebuildGson();
    }

    /**
     * Rebuild Gson.
     */
    private void rebuildGson()
    {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        for (RuntimeTypeAdapterFactory<T> factory : adapters)
        {
            builder.registerTypeAdapterFactory(factory);
        }

        builder.setPrettyPrinting();
        this.gson = builder.create();
    }

    /**
     * Read object from file.
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
                return gson.fromJson(reader, clazz);
            }
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }

        return null;
    }

    /**
     * Read object from file asynchronously.
     *
     * @param fileName Name of file.
     * @param callback Runnable that accepts object.
     */
    public void readAsync(String fileName, Consumer<T> callback)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> callback.accept(read(fileName)));
    }

    /**
     * Write new file with internal file contents.
     *
     * @param fileName Name of file.
     * @param overwrite Make new file over already existing file.
     */
    @Override
    public void copy(String fileName, boolean overwrite)
    {
        InputStream stream = plugin.getResource(fileName);
        InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(stream));
        T t = gson.fromJson(reader, clazz);
        try
        {
            File file = getFile(fileName);
            if (overwrite)
            {
                file.delete();
            }

            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            gson.toJson(t, writer);
            writer.flush();
            writer.close();
        }
        catch (IOException exception)
        {
            exception.printStackTrace();;
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
     * Write object to file.
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
            JsonObject json = (JsonObject) gson.toJsonTree(t);
            json.addProperty("type", t.getClass().getSimpleName());
            gson.toJson(json, writer);
            writer.flush();
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
     * @param t Data or object to be updated into file.
     */
    public void writeAsync(String fileName, T t)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> write(fileName, t));
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

    /**
     * Return all objects asynchronously.
     *
     * @param callback Runnable that accepts set of objects.
     */
    public void allAsync(Consumer<Set<T>> callback)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> callback.accept(all()));
    }
}
