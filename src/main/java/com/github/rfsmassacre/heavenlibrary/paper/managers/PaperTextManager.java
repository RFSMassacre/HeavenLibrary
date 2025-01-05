package com.github.rfsmassacre.heavenlibrary.paper.managers;

import com.github.rfsmassacre.heavenlibrary.managers.TextManager;
import com.github.rfsmassacre.heavenlibrary.paper.HeavenPaperPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class PaperTextManager extends TextManager
{
    private final HeavenPaperPlugin plugin;

    public PaperTextManager(HeavenPaperPlugin plugin, String folderName)
    {
        super(plugin.getDataFolder(), folderName);

        this.plugin = plugin;
        loadDefaultTexts();
        for (String fileName : defaultCache.keySet())
        {
            File file = getFile(fileName);
            if (!file.exists())
            {
                List<String> lines = defaultCache.get(fileName);
                if (lines != null && !lines.isEmpty())
                {
                    try
                    {
                        file.createNewFile();
                        write(fileName, lines);
                    }
                    catch (IOException exception)
                    {
                        exception.printStackTrace();
                    }
                }
            }
        }

        loadTexts();
    }

    @Override
    public void readAsync(String fileName, Consumer<List<String>> callback)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> callback.accept(read(fileName)));
    }

    @Override
    public void writeAsync(String fileName, List<String> strings)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> write(fileName, strings));
    }

    @Override
    public void deleteAsync(String fileName)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> delete(fileName));
    }

    @Override
    public void allAsync(Consumer<Set<List<String>>> callback)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> callback.accept(all()));
    }

    @Override
    public void loadDefaultTexts()
    {
        defaultCache.clear();
        String directory = (folderName == null || folderName.isBlank() ? File.separator : folderName + File.separator);
        File jarFile = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        if (!jarFile.exists())
        {
            return;
        }

        try (JarFile jar = new JarFile(jarFile))
        {
            jar.stream()
                    .filter((entry) -> entry.getName().startsWith(directory) && entry.getName().endsWith(".txt"))
                    .forEach((entry) ->
                    {
                        String fileName = entry.getName().replace(directory, "");
                        try (InputStream stream = plugin.getResource(entry.getName()))
                        {
                            if (stream != null)
                            {
                                List<String> lines = new BufferedReader(new InputStreamReader(stream)).lines().toList();
                                defaultCache.put(fileName, lines);
                            }
                        }
                        catch (IOException exception)
                        {
                            exception.printStackTrace();
                        }
                    });
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }
}
