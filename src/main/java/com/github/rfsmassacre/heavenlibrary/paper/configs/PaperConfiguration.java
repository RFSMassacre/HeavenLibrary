package com.github.rfsmassacre.heavenlibrary.paper.configs;

import com.github.rfsmassacre.heavenlibrary.interfaces.ConfigurationData;
import com.github.rfsmassacre.heavenlibrary.paper.managers.PaperYamlManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

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
    public PaperConfiguration(JavaPlugin plugin, String folderName, String fileName)
    {
        super(plugin, folderName, fileName);
    }

    /**
     * Retrieves String value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    public String getString(String key)
    {
        return yaml.getString(key, defaultYaml.getString(key));
    }

    /**
     * Retrieves int value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    public int getInt(String key)
    {
        return yaml.getInt(key, defaultYaml.getInt(key));
    }

    /**
     * Retrieves boolean value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    public boolean getBoolean(String key)
    {
        return yaml.getBoolean(key, defaultYaml.getBoolean(key));
    }

    /**
     * Retrieves double value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    public double getDouble(String key)
    {
        return yaml.getDouble(key, defaultYaml.getDouble(key));
    }

    /**
     * Retrieves long value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    public long getLong(String key)
    {
        return yaml.getLong(key, defaultYaml.getLong(key));
    }

    /**
     * Retrieves String list value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    public List<String> getStringList(String key)
    {
        List<String> option = yaml.getStringList(key);
        if (option.isEmpty())
        {
            option = defaultYaml.getStringList(key);
        }

        return option;
    }

    /**
     * Retrieves Integer list value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    public List<Integer> getIntegerList(String key)
    {
        List<Integer> option = yaml.getIntegerList(key);
        if (option.isEmpty())
        {
            option = defaultYaml.getIntegerList(key);
        }

        return option;
    }

    /**
     * Retrieves Double list value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    public List<Double> getDoubleList(String key)
    {
        List<Double> option = yaml.getDoubleList(key);
        if (option.isEmpty())
        {
            option = defaultYaml.getDoubleList(key);
        }

        return option;
    }

    /**
     * Retrieves Long list value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    public List<Long> getLongList(String key)
    {
        List<Long> option = yaml.getLongList(key);
        if (option.isEmpty())
        {
            option = defaultYaml.getLongList(key);
        }

        return option;
    }

    public Set<String> getKeys(boolean deep)
    {
        Set<String> keys = yaml.getKeys(deep);
        if (keys.isEmpty())
        {
            keys = defaultYaml.getKeys(deep);
        }

        return keys;
    }

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
