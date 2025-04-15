package com.github.rfsmassacre.heavenlibrary.velocity.configs;

import com.github.rfsmassacre.heavenlibrary.interfaces.ConfigurationData;
import com.github.rfsmassacre.heavenlibrary.managers.YamlManager;
import com.github.rfsmassacre.heavenlibrary.velocity.HeavenLibraryVelocity;
import com.github.rfsmassacre.heavenlibrary.velocity.HeavenVelocityPlugin;
import com.github.rfsmassacre.heavenlibrary.velocity.managers.VelocityYamlManager;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Handles retrieving all the values from a configuration file.
 */
@SuppressWarnings("all")
public class VelocityConfiguration extends VelocityYamlManager implements ConfigurationData<CommentedConfigurationNode>
{
    /**
     * JavaPlugin and name of file will give back a fully updated YamlConfiguration.
     *
     * @param fileName Name of file to handle.
     */
    public VelocityConfiguration(HeavenVelocityPlugin plugin, String folderName, String fileName, boolean update)
    {
        super(plugin, folderName, fileName, update);
    }

    public VelocityConfiguration(HeavenVelocityPlugin plugin, String folderName, String fileName)
    {
        super(plugin, folderName, fileName);
    }

    /**
     * Retrieves String value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    @Override
    public String getString(String key, String defaultValue)
    {
        String value = get(key, String.class);
        if (value == null)
        {
            return defaultValue;
        }

        return value;
    }

    /**
     * Retrieves int value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    @Override
    public int getInt(String key, int defaultValue)
    {
        Integer value = get(key, Integer.class);
        if (value == null)
        {
            return defaultValue;
        }

        return value;
    }

    /**
     * Retrieves boolean value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    @Override
    public boolean getBoolean(String key, boolean defaultValue)
    {
        Boolean value = get(key, Boolean.class);
        if (value == null)
        {
            return defaultValue;
        }

        return value;
    }

    /**
     * Retrieves double value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    @Override
    public double getDouble(String key, double defaultValue)
    {
        Double value = get(key, Double.class);
        if (value == null)
        {
            return defaultValue;
        }

        return value;
    }

    /**
     * Retrieves long value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    @Override
    public long getLong(String key, long defaultValue)
    {
        Long value = get(key, Long.class);
        if (value == null)
        {
            return defaultValue;
        }

        return value;
    }

    /**
     * Retrieves String list value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    @Override
    public List<String> getStringList(String key)
    {
        return getList(key, String.class);
    }

    /**
     * Retrieves Integer list value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    @Override
    public List<Integer> getIntegerList(String key)
    {
        return getList(key, Integer.class);
    }

    /**
     * Retrieves Double list value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    @Override
    public List<Double> getDoubleList(String key)
    {
        return getList(key, Double.class);
    }

    /**
     * Retrieves Long list value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    @Override
    public List<Long> getLongList(String key)
    {
        return getList(key, Long.class);
    }

    @Override
    public Set<String> getKeys(boolean deep)
    {
        //Not entirely sure how to get this
        return null;
    }

    @Override
    public CommentedConfigurationNode getSection(String key)
    {
        return Objects.requireNonNullElse(yaml.node(key), defaultYaml.node(key));
    }

    @Override
    protected boolean hasKey(String key)
    {
        return !yaml.node(splitKeys(key)).virtual() || !defaultYaml.node(splitKeys(key)).virtual();
    }

    @Override
    protected boolean hasList(String key)
    {
        return yaml.node(splitKeys(key)).isList() || defaultYaml.node(splitKeys(key)).isList();
    }
}
