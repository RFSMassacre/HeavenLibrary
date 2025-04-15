package com.github.rfsmassacre.heavenlibrary.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface ConfigurationData<T> extends ReloadableData
{
    /**
     * Retrieves String value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    String getString(String key, String defaultValue);

    default String getString(String key)
    {
        return getString(key, null);
    }

    /**
     * Retrieves int value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    int getInt(String key, int defaultValue);

    default int getInt(String key)
    {
        return getInt(key, 0);
    }

    /**
     * Retrieves boolean value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    boolean getBoolean(String key, boolean defaultValue);

    default boolean getBoolean(String key)
    {
        return getBoolean(key, false);
    }

    /**
     * Retrieves double value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    double getDouble(String key, double defaultValue);

    default double getDouble(String key)
    {
        return getDouble(key, 0.0);
    }

    /**
     * Retrieves long value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    long getLong(String key, long defaultValue);

    default long getLong(String key)
    {
        return getLong(key, 0L);
    }

    /**
     * Retrieves String list value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    List<String> getStringList(String key);

    /**
     * Retrieves Integer list value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    List<Integer> getIntegerList(String key);

    /**
     * Retrieves Double list value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    List<Double> getDoubleList(String key);

    /**
     * Retrieves Long list value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    List<Long> getLongList(String key);

    Set<String> getKeys(boolean deep);

    T getSection(String key);
}
