package com.github.rfsmassacre.spigot.files;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

public class TextManager
{
    private JavaPlugin instance;
    private HashMap<String, List<String>> textCache;
    private String folderName;

    public TextManager(JavaPlugin instance, String folderName)
    {
        this.instance = instance;
        this.textCache = new HashMap<>();
        this.folderName = folderName;

        //Create folder if needed
        File folder = new File(instance.getDataFolder() + "/" + folderName);
        if (!folder.exists())
        {
            folder.mkdirs();
        }
    }

    public List<String> loadTextFile(String fileName)
    {
        if (!textCache.containsKey(folderName + "/" + fileName))
        {
            cacheTextFile(fileName);
        }

        return textCache.get(folderName + "/" + fileName);
    }
    public void clearCacheFiles()
    {
        textCache.clear();
    }
    public void cacheTextFile(String fileName)
    {
        try
        {
            File file = new File(instance.getDataFolder() + "/" + folderName + "/" + fileName);
            if (!file.exists())
            {
                file.createNewFile();
                instance.saveResource(folderName + "/" + fileName, true);
            }
            textCache.put(folderName + "/" + fileName, Files.readAllLines(file.toPath()));
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }
}
