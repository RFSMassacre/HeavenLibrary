package com.github.rfsmassacre.heavenlibrary.velocity.databases;

import com.github.rfsmassacre.heavenlibrary.databases.MySQLDatabase;
import com.github.rfsmassacre.heavenlibrary.velocity.HeavenVelocityPlugin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@SuppressWarnings({"unused", "CallToPrintStackTrace"})
public class VelocityMySQLDatabase extends MySQLDatabase
{
    private final HeavenVelocityPlugin plugin;

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
    public VelocityMySQLDatabase(HeavenVelocityPlugin plugin, String hostName, String database, String username, String password, int port, boolean ssl)
    {
        super(hostName, database, username, password, port, ssl);

        this.plugin = plugin;
    }

    @Override
    public <T> void insert(String mainKey, T t) throws SQLException
    {
        String sql = SQLPreset.INSERT.parseSQL(t.getClass());
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, mainKey);
        statement.setString(2, new Gson().toJson(t));
        statement.executeUpdate();
    }

    @Override
    public <T> void insertAsync(String mainKey, T t)
    {
        plugin.getServer().getScheduler().buildTask(plugin, () ->
        {
            try
            {
                insert(mainKey, t);
            }
            catch (SQLException exception)
            {
                exception.printStackTrace();
            }
        }).delay(0L, TimeUnit.SECONDS).schedule();
    }

    @Override
    public <T> void update(String mainKey, T t) throws SQLException
    {
        insert(mainKey, t);
    }

    @Override
    public <T> void updateAsync(String mainKey, T t)
    {
        insertAsync(mainKey, t);
    }

    @Override
    public <T> T query(String mainKey, Class<T> clazz) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement(SQLPreset.SELECT.parseSQL(clazz));
        statement.setString(1, mainKey);
        ResultSet result = statement.executeQuery();
        Gson gson = new Gson();
        if (result.next())
        {
            String data = result.getString("data");
            if (data == null)
            {
                throw new SQLException("Data of " + mainKey + " is null!");
            }

            return gson.fromJson(data, clazz);
        }
        else
        {
            throw new SQLException("Could not find with " + mainKey);
        }
    }

    @Override
    public <T> void queryAsync(String mainKey, Class<T> clazz, Consumer<T> callback)
    {
        plugin.getServer().getScheduler().buildTask(plugin, () ->
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
    public <T> Set<T> queryAll(Class<T> clazz) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement(SQLPreset.SELECT_ALL.parseSQL(clazz));
        ResultSet result = statement.executeQuery();
        Set<T> set = new HashSet<>();
        Gson gson = new GsonBuilder().create();
        while (result.next())
        {
            String data = result.getString("data");
            if (data != null)
            {
                set.add(gson.fromJson(data, clazz));
            }
        }

        return set;
    }

    @Override
    public <T> void queryAllAsync(Class<T> clazz, Consumer<Set<T>> callback)
    {
        plugin.getServer().getScheduler().buildTask(plugin, () ->
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
    public <T> void delete(String mainKey, Class<T> clazz) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement(SQLPreset.DELETE.parseSQL(clazz));
        statement.setString(1, mainKey);
        statement.executeUpdate();
    }

    @Override
    public <T> void deleteAsync(String mainKey, Class<T> clazz)
    {
        plugin.getServer().getScheduler().buildTask(plugin, () ->
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
