package com.github.rfsmassacre.heavenlibrary.managers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings({"unused", "ResultOfMethodCallIgnored", "CallToPrintStackTrace"})
public class TextManager
{
    protected final HashMap<String, List<String>> textCache;
    private final File folder;

    public TextManager(File dataFolder, String folderName)
    {
        this.textCache = new HashMap<>();
        this.folder = new File(dataFolder + File.separator + folderName);
        if (!folder.exists())
        {
            folder.mkdirs();
        }
    }

    public List<String> loadTextFile(String fileName)
    {
        if (!textCache.containsKey(fileName))
        {
            cacheTextFile(fileName);
        }

        return textCache.get(fileName);
    }

    public void clearCacheFiles()
    {
        textCache.clear();
    }

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
            //Do nothing
            exception.printStackTrace();
        }
    }

    public void cacheTextFile(String fileName)
    {
        try
        {
            textCache.put(fileName, Files.readAllLines(getFile(fileName).toPath()));
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    public File getFile(String fileName)
    {
        return new File(folder + File.separator + fileName);
    }
}
