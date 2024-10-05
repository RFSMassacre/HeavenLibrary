package com.github.rfsmassacre.heavenlibrary.paper.managers;

import com.github.rfsmassacre.heavenlibrary.managers.TextManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class PaperTextManager extends TextManager
{
    private final JavaPlugin plugin;

    public PaperTextManager(JavaPlugin plugin, String folderName)
    {
        super(plugin.getDataFolder(), folderName);

        this.plugin = plugin;
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
}
