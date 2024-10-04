package com.github.rfsmassacre.heavenlibrary.managers;

import com.github.rfsmassacre.heavenlibrary.factories.RuntimeTypeAdapterFactory;
import com.github.rfsmassacre.heavenlibrary.interfaces.MultiFileData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

@SuppressWarnings({"unused", "CallToPrintStackTrace", "ResultOfMethodCallIgnored"})
public abstract class GsonManager<T> implements MultiFileData<T>
{
    private static final String CLASS_ID = "type";

    private final File folder;
    protected final Class<T> clazz;
    private final Set<RuntimeTypeAdapterFactory<? extends T>> adapters;
    protected Gson gson;

    /**
     * Constructor.
     *
     * @param dataFolder Main plugin folder.
     * @param folderName Name of folder where everything will be held.
     * @param clazz Class type of the object.
     */
    public GsonManager(File dataFolder, String folderName, Class<T> clazz)
    {
        this.folder = new File(dataFolder + "/" + folderName);
        folder.mkdirs();
        this.clazz = clazz;
        this.adapters = new HashSet<>();
        rebuildGson();
    }

    /**
     * Register child class.
     *
     * @param childClass Child class.
     */
    public void registerType(Class<? extends T> childClass)
    {
        RuntimeTypeAdapterFactory<T> adapter = RuntimeTypeAdapterFactory
                .of(clazz)
                .registerSubtype(childClass);
        this.adapters.add(adapter);
        rebuildGson();
    }

    /**
     * Register child class.
     *
     * @param childClasses Child classes.
     */
    @SafeVarargs
    public final void registerTypes(Class<? extends T>... childClasses)
    {
        if (childClasses.length == 0)
        {
            return;
        }

        RuntimeTypeAdapterFactory<T> adapter = RuntimeTypeAdapterFactory.of(clazz);
        for (Class<? extends T> childClass : childClasses)
        {
            adapter = adapter.registerSubtype(childClass);
            this.adapters.add(adapter);
        }

        rebuildGson();
    }

    /**
     * Rebuild Gson.
     */
    protected void rebuildGson()
    {
        GsonBuilder builder = new GsonBuilder();
        for (RuntimeTypeAdapterFactory<? extends T> factory : adapters)
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
        if (file.exists())
        {
            try (InputStream stream = new FileInputStream(file))
            {
                InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(stream));
                return gson.fromJson(gson.newJsonReader(reader), clazz);
            }
            catch (IOException exception)
            {
                exception.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Read object from file asynchronously.
     *
     * @param fileName Name of file.
     * @param callback Runnable that accepts object.
     */
    public abstract void readAsync(String fileName, Consumer<T> callback);

    /**
     * Write new file with internal file contents.
     *
     * @param fileName Name of file.
     * @param overwrite Make new file over already existing file.
     */
    @Override
    public void copy(String fileName, boolean overwrite)
    {
        InputStream stream = getResource(fileName);
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
            exception.printStackTrace();
        }
    }

    protected abstract InputStream getResource(String fileName);

    /**
     * Write new file with internal file contents asynchronously.
     *
     * @param fileName Name of file.
     * @param overwrite Make new file over already existing file.
     */
    public abstract void copyAsync(String fileName, boolean overwrite);

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
            JsonObject json = gson.toJsonTree(t).getAsJsonObject();
            json.addProperty(CLASS_ID, t.getClass().getSimpleName());
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
    public abstract void writeAsync(String fileName, T t);

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
    public abstract void deleteAsync(String fileName);

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
        File[] files = folder.listFiles();
        if (files == null)
        {
            return all;
        }

        for (File file : files)
        {
            all.add(read(file.getName()));
        }

        return all;
    }

    /**
     * Return all objects asynchronously.
     *
     * @param callback Runnable that accepts set of objects.
     */
    public abstract void allAsync(Consumer<Set<T>> callback);
}