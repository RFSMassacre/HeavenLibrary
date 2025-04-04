package com.github.rfsmassacre.heavenlibrary.managers;

import com.github.rfsmassacre.heavenlibrary.interfaces.MultiFileData;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

@SuppressWarnings({"unused", "ResultOfMethodCallIgnored", "CallToPrintStackTrace"})
public abstract class TextManager implements MultiFileData<List<String>>
{
    protected final String folderName;
    protected final File folder;
    protected final Map<String, List<String>> defaultCache;
    protected final Map<String, List<String>> cache;

    public TextManager(File dataFolder, String folderName)
    {
        this.folderName = folderName;
        this.folder = new File(dataFolder + File.separator + folderName);
        if (!folder.exists())
        {
            folder.mkdirs();
        }

        this.defaultCache = new HashMap<>();
        this.cache = new HashMap<>();
    }

    public List<String> getText(String fileName)
    {
        return cache.get(fileName);
    }

    public abstract void loadDefaultTexts();

    public void loadTexts()
    {
        cache.clear();
        for (File file : folder.listFiles())
        {
            List<String> text = read(file.getName());
            cache.put(file.getName(), text);
        }
    }

    /**
     * Read object from file.
     * Please note that all objects inside objects have to be serializable or else you will get an exception on reading.
     *
     * @param fileName Name of file.
     * @return Object from file.
     */
    @Override
    public List<String> read(String fileName)
    {
        File file = getFile(fileName);
        if (file.exists())
        {
            try
            {
                return Files.readAllLines(file.toPath());
            }
            catch (IOException exception)
            {
                exception.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public void write(String fileName, List<String> lines)
    {
        try
        {
            if (lines == null)
            {
                return;
            }

            File file = getFile(fileName);
            if (!file.exists())
            {
                file.createNewFile();
            }

            FileWriter writer = new FileWriter(file, true);
            for (String line : lines)
            {
                writer.write(line + System.lineSeparator());
            }

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

    @Override
    public Set<List<String>> all()
    {
        Set<List<String>> all = new HashSet<>();
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

    @Override
    public File getFile(String fileName)
    {
        return new File(folder, fileName + (fileName.endsWith(".txt") ? "" : ".txt"));
    }
}
