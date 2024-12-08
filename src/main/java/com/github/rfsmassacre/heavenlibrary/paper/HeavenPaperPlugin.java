package com.github.rfsmassacre.heavenlibrary.paper;

import com.github.rfsmassacre.heavenlibrary.managers.YamlManager;
import com.github.rfsmassacre.heavenlibrary.paper.configs.PaperConfiguration;
import com.github.rfsmassacre.heavenlibrary.paper.configs.PaperLocale;
import com.github.rfsmassacre.heavenlibrary.paper.managers.PaperYamlManager;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

@Getter
@SuppressWarnings({"unused"})
public abstract class HeavenPaperPlugin extends JavaPlugin
{
    private final Map<String, PaperYamlManager> yamlManagers;

    protected HeavenPaperPlugin()
    {
        this.yamlManagers = new HashMap<>();
    }

    public void addYamlManager(PaperYamlManager yamlManager)
    {
        this.yamlManagers.put(yamlManager.getFileName(), yamlManager);
    }

    public <T extends YamlManager<FileConfiguration, ConfigurationSection>> T getYamlManager(String fileName,
                                                                                             Class<T> clazz)
    {
        PaperYamlManager yamlManager = yamlManagers.get(fileName);
        if (clazz.isInstance(yamlManager))
        {
            return clazz.cast(yamlManager);
        }

        return null;
    }

    public PaperConfiguration getConfiguration()
    {
        return this.getYamlManager("config.yml", PaperConfiguration.class);
    }

    public PaperLocale getLocale()
    {
        return this.getYamlManager("locale.yml", PaperLocale.class);
    }
}
