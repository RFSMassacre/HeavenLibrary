package com.github.rfsmassacre.heavenlibrary.databases;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Handle MySQL databases.
 */
@SuppressWarnings({"unused", "CallToPrintStackTrace"})
public abstract class MySQLDatabase extends SQLDatabase
{
    public static final String CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    public static final String VERSION = "9.0.0";
    public static final String DRIVER = "mysql-connector-j-" + VERSION + ".jar";
    public static final String DRIVER_URL = "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/" + VERSION +
            "/" + DRIVER;

    //Database information.
    protected final String hostname;
    protected final String database;
    protected final String username;
    protected final String password;
    protected final int port;
    protected final boolean ssl;

    /**
     * Save database while instantiating.
     * @param hostName Address where database is hosted.
     * @param database Name of database.
     * @param username Username to access database.
     * @param password Password to access database.
     * @param port Port number of database.
     * @param ssl Use a secured connection.
     */
    public MySQLDatabase(String hostName, String database, String username, String password, int port, boolean ssl)
    {
        this.hostname = hostName;
        this.database = database;
        this.username = username;
        this.password = password;
        this.port = port;
        this.ssl = ssl;

        try
        {
            connect();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * Connect to database.
     * @throws SQLException Expected to throw if wrong parameters were entered or host is not up.
     */
    @Override
    public void connect() throws SQLException
    {
        this.connection = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database,
                username, password);
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
    public <T> void delete(String mainKey, Class<T> clazz) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement(SQLPreset.DELETE.parseSQL(clazz));
        statement.setString(1, mainKey);
        statement.executeUpdate();
    }
}
