package com.github.rfsmassacre.heavenlibrary.interfaces;

import java.io.File;

public interface MultiFileData
{
    /**
     * Read from file and convert into whatever data or object needed.
     * @param fileName Name of file.
     * @return Data or object read from the file.
     */
    <T> T read(String fileName, Class<T> clazz);

    /**
     * Copy a new file with format.
     * @param fileName Name of file.
     * @param overwrite Make new file over already existing file.
     */
    <T> void copy(String fileName, Class<T> clazz, boolean overwrite);

    /**
     * Write data of object into the file.
     * @param fileName Name of file.
     * @param t Data or object to be updated into file.
     */
    <T> void write(String fileName, T t);

    /**
     * Delete specified file.
     * @param fileName Name of file.
     */
    <T> void delete(String fileName, Class<T> clazz);

    /**
     * Retrieve file object from file name.
     * @param fileName Name of file.
     * @return File object.
     */
    <T> File getFile(String fileName, Class<T> clazz);
}
