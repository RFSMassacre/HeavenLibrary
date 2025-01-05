package com.github.rfsmassacre.heavenlibrary.velocity.managers;

import com.github.rfsmassacre.heavenlibrary.managers.TextManager;
import com.github.rfsmassacre.heavenlibrary.velocity.HeavenVelocityPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class VelocityTextManager extends TextManager
{
    private final HeavenVelocityPlugin plugin;

    public VelocityTextManager(HeavenVelocityPlugin plugin, String folderName)
    {
        super(plugin.getDataDirectory().toFile(), folderName);

        this.plugin = plugin;
        loadDefaultTexts();
        for (String fileName : defaultCache.keySet())
        {
            File file = getFile(fileName);
            if (!file.exists())
            {
                write(fileName, defaultCache.get(fileName));
            }
        }

        loadTexts();
    }

    @Override
    public void readAsync(String fileName, Consumer<List<String>> callback)
    {
        plugin.getServer().getScheduler().buildTask(plugin, () -> callback.accept(read(fileName))).delay(0L,
                TimeUnit.SECONDS).schedule();
    }

    @Override
    public void writeAsync(String fileName, List<String> strings)
    {
        plugin.getServer().getScheduler().buildTask(plugin, () -> write(fileName, strings)).delay(0L,
                TimeUnit.SECONDS).schedule();
    }

    @Override
    public void deleteAsync(String fileName)
    {
        plugin.getServer().getScheduler().buildTask(plugin, () -> delete(fileName)).delay(0L,
                TimeUnit.SECONDS).schedule();
    }

    @Override
    public void allAsync(Consumer<Set<List<String>>> callback)
    {
        plugin.getServer().getScheduler().buildTask(plugin, () -> callback.accept(all())).delay(0L,
                TimeUnit.SECONDS).schedule();
    }

    @Override
    public void loadDefaultTexts()
    {
        defaultCache.clear();
        String directory = File.separator + "resources" + (folderName == null || folderName.isBlank() ? "" : File.separator +
                folderName);
        URL url = plugin.getClass().getClassLoader().getResource(directory);
        if (url == null)
        {
            return;
        }

        try (Stream<Path> stream = Files.walk(Paths.get(url.toURI())))
        {
            List<File> files = stream
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .toList();
            for (File file : files)
            {
                defaultCache.put(file.getName(), Files.readAllLines(file.toPath()));
            }
        }
        catch (IOException | URISyntaxException | NullPointerException exception)
        {
            //Do nothing
        }
    }
}
