package com.github.rfsmassacre.heavenlibrary.interfaces;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public interface FileData<T>
{
    /**
     * Read from file and convert into whatever data or object needed.
     * @return Data or object read from the file.
     */
    T read() throws IOException;

    void readAsync(Consumer<T> callback);

    /**
     * Copy a new file with format.
     * @param overwrite Make new file over already existing file.
     */
    void copy(boolean overwrite);

    void copyAsync(boolean overwrite);

    /**
     * Write data of object into the file.
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
     * @return File object.
     */
    File getFile();
}
