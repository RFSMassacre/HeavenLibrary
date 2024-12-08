package com.github.rfsmassacre.heavenlibrary.interfaces;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public interface FileData<T>
{
    class ResourceNotFoundException extends Exception
    {
        public ResourceNotFoundException(String pluginName, String fileName)
        {
            super("Cannot find " + fileName + " in resource folder of plugin " + pluginName + "!");
        }
    }

    /**
     * Read from file and convert into whatever data or object needed.
     *
     * @return Data or object read from the file.
     */
    T read();

    /**
     * Read from inside the jar and convert into whatever data or object needed.
     *
     * @return Data or object read from inside the jar.
     */
    T readDefault() throws ResourceNotFoundException;

    void readAsync(Consumer<T> callback);

    /**
     * Copy a new file with format.
     *
     * @param overwrite Make new file over already existing file.
     */
    void copy(boolean overwrite);

    void copyAsync(boolean overwrite);

    /**
     * Write data of object into the file.
     *
     * @param t Data or object to be updated into file.
     */
    void write(T t);

    void writeAsync(T t);

    /**
     * Delete specified file.
     */
    void delete();

    void deleteAsync();

    /**
     * Retrieve file object from file name.
     *
     * @return File object.
     */
    File getFile();
}
