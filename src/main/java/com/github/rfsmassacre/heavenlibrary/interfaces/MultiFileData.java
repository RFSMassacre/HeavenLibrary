package com.github.rfsmassacre.heavenlibrary.interfaces;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public interface MultiFileData<T>
{
    /**
     * Read from file and convert into whatever data or object needed.
     * @param fileName Name of file.
     * @return Data or object read from the file.
     */
    T read(String fileName) throws FileNotFoundException;

    void readAsync(String fileName, Consumer<T> callback);

    /**
     * Write data of object into the file.
     * @param fileName Name of file.
     * @param t Data or object to be updated into file.
     */
    void write(String fileName, T t);

    void writeAsync(String fileName, T t);

    /**
     * Delete specified file.
     * @param fileName Name of file.
     */
    void delete(String fileName);

    void deleteAsync(String fileName);

    Set<T> all();

    void allAsync(Consumer<Set<T>> callback);

    /**
     * Retrieve file object from file name.
     * @param fileName Name of file.
     * @return File object.
     */
    File getFile(String fileName);
}
