package com.github.rfsmassacre.heavenlibrary.paper.databases;

import com.github.rfsmassacre.heavenlibrary.databases.MySQLDatabase;
import com.github.rfsmassacre.heavenlibrary.paper.HeavenPaperPlugin;

import java.sql.SQLException;
import java.util.Set;
import java.util.function.Consumer;

@SuppressWarnings({"unused", "CallToPrintStackTrace"})
public class PaperMySQLDatabase extends MySQLDatabase
{
    private final HeavenPaperPlugin plugin;

    /**
     * Save database while instantiating.
     *
     * @param hostName Address where database is hosted.
     * @param database Name of database.
     * @param username Username to access database.
     * @param password Password to access database.
     * @param port     Port number of database.
     * @param ssl      Use a secured connection.
     */
    public PaperMySQLDatabase(HeavenPaperPlugin plugin, String hostName, String database, String username, String password, int port, boolean ssl)
    {
        super(hostName, database, username, password, port, ssl);

        this.plugin = plugin;
    }

    @Override
    public <T> void insertAsync(String mainKey, T t)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () ->
        {
            try
            {
                insert(mainKey, t);
            }
            catch (SQLException exception)
            {
                exception.printStackTrace();
            }
        });
    }

    @Override
    public <T> void queryAsync(String mainKey, Class<T> clazz, Consumer<T> callback)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () ->
        {
            try
            {
                callback.accept(query(mainKey, clazz));
            }
            catch (SQLException exception)
            {
                exception.printStackTrace();
            }
        });
    }

    @Override
    public <T> void queryAllAsync(Class<T> clazz, Consumer<Set<T>> callback)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () ->
        {
            try
            {
                callback.accept(queryAll(clazz));
            }
            catch (SQLException exception)
            {
                exception.printStackTrace();
            }
        });
    }

    @Override
    public <T> void deleteAsync(String mainKey, Class<T> clazz)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () ->
        {
            try
            {
                delete(mainKey, clazz);
            }
            catch (SQLException exception)
            {
                exception.printStackTrace();
            }
        });
    }
}
