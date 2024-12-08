package com.github.rfsmassacre.heavenlibrary.paper.configs;

import com.github.rfsmassacre.heavenlibrary.interfaces.ConfigurationData;
import com.github.rfsmassacre.heavenlibrary.paper.HeavenLibraryPaper;
import com.github.rfsmassacre.heavenlibrary.paper.managers.PaperYamlManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handles retrieving all the values from a configuration file.
 */
public class PaperConfiguration extends PaperYamlManager implements ConfigurationData<ConfigurationSection>
{
    /**
     * JavaPlugin and name of file will give back a fully updated YamlConfiguration.
     *
     * @param plugin   JavaPlugin handling the configuration.
     * @param fileName Name of file to handle.
     */
    public PaperConfiguration(JavaPlugin plugin, String folderName, String fileName, boolean update)
    {
        super(plugin, folderName, fileName, update);
    }

    public PaperConfiguration(JavaPlugin plugin, String folderName, String fileName)
    {
        super(plugin, folderName, fileName);
    }

    @Override
    public <T> T get(String key, Class<T> clazz)
    {
        if (plugin instanceof HeavenLibraryPaper || this.hasKey(key))
        {
            return yaml.getObject(key, clazz, defaultYaml.getObject(key, clazz));
        }

        PaperConfiguration library = getLibraryYaml(PaperConfiguration.class);
        if (library != null)
        {
            return library.get(key, clazz);
        }

        return null;
    }

    @Override
    public <T> List<T> getList(String key, Class<T> clazz)
    {
        List<T> list = new ArrayList<>();
        List<?> rawList = null;
        if (plugin instanceof HeavenLibraryPaper || hasList(key))
        {
            rawList = yaml.getList(key, defaultYaml.getList(key));
        }
        else
        {
            PaperConfiguration library = this.getLibraryYaml(PaperConfiguration.class);
            if (library != null)
            {
                rawList = library.getList(key, clazz);
            }
        }

        if (rawList != null)
        {
            for (Object object : rawList)
            {
                if (clazz.isInstance(object))
                {
                    list.add(clazz.cast(object));
                }
            }
        }

        return list;
    }

    @Override
    public String getString(String key)
    {
        if (this.plugin instanceof HeavenLibraryPaper || this.hasKey(key))
        {
            return this.yaml.getString(key,defaultYaml.getString(key));
        }

        PaperConfiguration library = this.getLibraryYaml(PaperConfiguration.class);
        if (library != null)
        {
            return library.getString(key);
        }

        return null;
    }

    @Override
    public int getInt(String key)
    {
        Integer value = this.get(key, Integer.class);
        if (value == null)
        {
            return 0;
        }

        return value;
    }

    @Override
    public boolean getBoolean(String key)
    {
        Boolean value = this.get(key, Boolean.class);
        if (value == null)
        {
            return false;
        }

        return value;
    }

    @Override
    public double getDouble(String key)
    {
        Double value = this.get(key, Double.class);
        if (value == null)
        {
            return 0.0;
        }

        return value;
    }

    @Override
    public long getLong(String key)
    {
        Long value = this.get(key, Long.class);
        if (value == null)
        {
            return 0L;
        }

        return value;
    }

    @Override
    public List<String> getStringList(String key)
    {
        return yaml.getStringList(key);
    }

    @Override
    public List<Integer> getIntegerList(String key)
    {
        return getList(key, Integer.class);
    }

    @Override
    public List<Double> getDoubleList(String key)
    {
        return getList(key, Double.class);
    }

    @Override
    public List<Long> getLongList(String key)
    {
        return getList(key, Long.class);
    }

    @Override
    public Set<String> getKeys(boolean deep)
    {
        Set<String> keys = this.yaml.getKeys(deep);
        if (keys.isEmpty())
        {
            keys = this.defaultYaml.getKeys(deep);
        }

        return keys;
    }

    @Override
    public ConfigurationSection getSection(String key)
    {
        ConfigurationSection section = yaml.getConfigurationSection(key);
        if (section == null)
        {
            section = defaultYaml.getConfigurationSection(key);
        }

        return section;
    }
}
