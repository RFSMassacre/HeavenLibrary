package com.github.rfsmassacre.heavenlibrary.interfaces;

import java.io.File;

public interface FileData<T>
{
    /**
     * Read from file and convert into whatever data or object needed.
     * @param fileName Name of file.
     * @return Data or object read from the file.
     */
    T read(String fileName);

    /**
     * Copy a new file with format.
     * @param fileName Name of file.
     * @param overwrite Make new file over already existing file.
     */
    void copy(String fileName, boolean overwrite);

    /**
     * Write data of object into the file.
     * @param fileName Name of file.
     * @param t Data or object to be updated into file.
     */
    void write(String fileName, T t);

    /**
     * Delete specified file.
     * @param fileName Name of file.
     */
    void delete(String fileName);

    /**
     * Retrieve file object from file name.
     * @param fileName Name of file.
     * @return File object.
     */
    File getFile(String fileName);
}
