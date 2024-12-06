package com.github.rfsmassacre.heavenlibrary.managers;

import com.github.rfsmassacre.heavenlibrary.interfaces.FileData;
import com.github.rfsmassacre.heavenlibrary.interfaces.ReloadableData;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

@SuppressWarnings({"unused", "ResultOfMethodCallIgnored", "CallToPrintStackTrace"})
public abstract class YamlManager<T extends X, X> implements FileData<T>, ReloadableData
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
        this.defaultYaml = readDefault();

        reload();
    }

    /**
     * Provide easy function to reload configuration without needing parameters.
     */
    @Override
    public void reload()
    {
        this.yaml = read();
    }

    @Override
    public void copy(boolean overwrite)
    {
        if (!getFile().exists())
        {
            write(defaultYaml);
        }
        else
        {
            if (overwrite)
            {
                write(defaultYaml);
            }
            else
            {
                reload();
                update();
                write(yaml);
            }
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
     * Copy section of default file to new file.
     *
     * @param source Original section.
     * @param destination Destination section.
     * @param path Path to section. (Can be null if not needed.)
     */
    public abstract void copySection(X source, X destination, @Nullable String path);

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