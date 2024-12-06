package com.github.rfsmassacre.heavenlibrary.velocity.configs;

import com.github.rfsmassacre.heavenlibrary.interfaces.ConfigurationData;
import com.github.rfsmassacre.heavenlibrary.velocity.HeavenVelocityPlugin;
import com.github.rfsmassacre.heavenlibrary.velocity.managers.VelocityYamlManager;
import com.velocitypowered.api.plugin.Plugin;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Handles retrieving all the values from a configuration file.
 */
@SuppressWarnings({"unused"})
public class VelocityConfiguration extends VelocityYamlManager implements ConfigurationData<CommentedConfigurationNode>
{

    /**
     * JavaPlugin and name of file will give back a fully updated YamlConfiguration.
     *
     * @param fileName Name of file to handle.
     */
    public VelocityConfiguration(HeavenVelocityPlugin plugin, String folderName, String fileName)
    {
        super(plugin, folderName, fileName);
    }
    
    /**
     * Provide easy function to reload configuration without needing parameters.
     */
    @Override
    public void reload()
    {
        this.yaml = read();
    }

    /**
     * Retrieves String value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    @Override
    public String getString(String key)
    {
        return Objects.requireNonNullElse(yaml.node(splitKeys(key)).getString(),
                defaultYaml.node(splitKeys(key)).getString());
    }

    /**
     * Retrieves int value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    @Override
    public int getInt(String key)
    {
        return Objects.requireNonNullElse(yaml.node(splitKeys(key)).getInt(),
                defaultYaml.node(splitKeys(key)).getInt());
    }

    /**
     * Retrieves boolean value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    @Override
    public boolean getBoolean(String key)
    {
        return Objects.requireNonNullElse(yaml.node(splitKeys(key)).getBoolean(),
                defaultYaml.node(splitKeys(key)).getBoolean());
    }

    /**
     * Retrieves double value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    @Override
    public double getDouble(String key)
    {
        return Objects.requireNonNullElse(yaml.node(splitKeys(key)).getDouble(),
                defaultYaml.node(splitKeys(key)).getDouble());
    }

    /**
     * Retrieves long value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    @Override
    public long getLong(String key)
    {
        return Objects.requireNonNullElse(yaml.node(splitKeys(key)).getLong(),
                defaultYaml.node(splitKeys(key)).getLong());
    }

    /**
     * Retrieves String list value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    @Override
    public List<String> getStringList(String key)
    {
        try
        {
            if (!yaml.node(key).isNull())
            {
                return yaml.node(splitKeys(key)).getList(String.class);
            }

            return defaultYaml.node(splitKeys(key)).getList(String.class);
        }
        catch (SerializationException exception)
        {
            try
            {
                return defaultYaml.node(splitKeys(key)).getList(String.class);
            }
            catch (SerializationException defaultException)
            {
                return null;
            }
        }
    }

    /**
     * Retrieves Integer list value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    @Override
    public List<Integer> getIntegerList(String key)
    {
        try
        {
            if (!yaml.node(key).isNull())
            {
                return yaml.node(splitKeys(key)).getList(Integer.class);
            }

            return defaultYaml.node(splitKeys(key)).getList(Integer.class);
        }
        catch (SerializationException exception)
        {
            try
            {
                return defaultYaml.node(splitKeys(key)).getList(Integer.class);
            }
            catch (SerializationException defaultException)
            {
                return null;
            }
        }
    }

    /**
     * Retrieves Double list value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    @Override
    public List<Double> getDoubleList(String key)
    {
        try
        {
            if (!yaml.node(key).isNull())
            {
                return yaml.node(splitKeys(key)).getList(Double.class);
            }

            return defaultYaml.node(splitKeys(key)).getList(Double.class);
        }
        catch (SerializationException exception)
        {
            try
            {
                return defaultYaml.node(splitKeys(key)).getList(Double.class);
            }
            catch (SerializationException defaultException)
            {
                return null;
            }
        }
    }

    /**
     * Retrieves Long list value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    @Override
    public List<Long> getLongList(String key)
    {
        try
        {
            if (!yaml.node(key).isNull())
            {
                return yaml.node(splitKeys(key)).getList(Long.class);
            }

            return defaultYaml.node(splitKeys(key)).getList(Long.class);
        }
        catch (SerializationException exception)
        {
            try
            {
                return defaultYaml.node(splitKeys(key)).getList(Long.class);
            }
            catch (SerializationException defaultException)
            {
                return null;
            }
        }
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
}
