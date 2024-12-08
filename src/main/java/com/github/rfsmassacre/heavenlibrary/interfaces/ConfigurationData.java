package com.github.rfsmassacre.heavenlibrary.interfaces;

import java.util.List;
import java.util.Set;

public interface ConfigurationData<T> extends ReloadableData
{
    public <X> X get(String key, Class<X> clazz);

    public <X> List<X> getList(String key, Class<X> clazz);

    /**
     * Retrieves String value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    String getString(String key);

    /**
     * Retrieves int value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    int getInt(String key);

    /**
     * Retrieves boolean value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    boolean getBoolean(String key);

    /**
     * Retrieves double value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    double getDouble(String key);

    /**
     * Retrieves long value from configuration.
     * @param key Key that the value is assigned to.
     * @return Value from config.
     */
    long getLong(String key);

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
