package com.github.rfsmassacre.heavenlibrary.managers;

import com.github.rfsmassacre.heavenlibrary.interfaces.MultiFileData;

import java.io.File;

@SuppressWarnings("ResultOfMethodCallIgnored")
public abstract class YamlStorage<T, C> implements MultiFileData<T>
{
    protected File folder;

    /**
     * Constructor for YamlManager.
     * @param dataFolder Main plugin folder.
     * @param folderName Name of folder.
     */
    public YamlStorage(File dataFolder, String folderName)
    {
        this.folder = new File(dataFolder + File.separator + folderName);
        if (!folder.exists())
        {
            folder.mkdirs();
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
        return new File(folder.getPath() + File.separator + fileName + (fileName.endsWith(".yml") ? "" :
                ".yml"));
    }

    public abstract T load(C configuration);

    public abstract C save(T t);
}
