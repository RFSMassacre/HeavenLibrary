package com.github.rfsmassacre.heavenlibrary.managers;

import com.github.rfsmassacre.heavenlibrary.interfaces.FileData;
import com.github.rfsmassacre.heavenlibrary.interfaces.ReloadableData;

import java.io.File;
import java.io.IOException;

@SuppressWarnings({"unused", "ResultOfMethodCallIgnored", "CallToPrintStackTrace"})
public abstract class YamlManager<T> implements FileData<T>, ReloadableData
{
    protected final File folder;
    protected final String folderName;
    protected final String fileName;
    protected T defaultYaml;
    protected T yaml;

    /**
     * Constructor for YamlManager.
     *
     * @param folderName Name of folder.
     */
    public YamlManager(File dataFolder, String folderName, String fileName)
    {
        this.folderName = folderName;
        this.folder = new File(dataFolder + File.separator + folderName);
        if (!folder.exists())
        {
            folder.mkdirs();
        }

        this.fileName = fileName;
    }

    /**
     * Provide easy function to reload configuration without needing parameters.
     */
    @Override
    public void reload()
    {
        try
        {
            this.yaml = read();
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * Delete specified file.
     */
    @Override
    public void delete()
    {
        File file = getFile();
        if (file.exists())
        {
            file.delete();
        }
    }

    /**
     * Retrieve file object from file name.
     *
     * @return File object.
     */
    @Override
    public File getFile()
    {
        return new File(folder, fileName + (fileName.endsWith(".yml") ? "" : ".yml"));
    }
}