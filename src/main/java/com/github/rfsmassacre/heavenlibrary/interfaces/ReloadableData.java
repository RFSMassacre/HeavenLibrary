package com.github.rfsmassacre.heavenlibrary.interfaces;

public interface ReloadableData
{
    /**
     * Provide easy function to reload configuration without needing parameters.
     */
    void reload(boolean update);

    default void reload()
    {
        reload(false);
    }

    /**
     * Provide an easy function to transfer new values from the default file to the new one without
     * overwriting valid values.
     */
    void update();
}
