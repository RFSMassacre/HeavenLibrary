package com.github.rfsmassacre.heavenlibrary.databases;

import com.github.rfsmassacre.heavenlibrary.interfaces.SQLData;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public abstract class SQLiteDatabase<T> implements SQLData<T>
{
    /**
     * Check for database driver only once.
     */
    static
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
        }
        catch (ClassNotFoundException exception)
        {
            exception.printStackTrace();
        }
    }

    //Database information.
    private String absolutePath;
    private String database;
    protected String tableName;
    private Connection connection;
    protected String mainKey;
    protected String[] columns; //This is assumed they are properly formatted.

    /**
     * Save database while instantiating.
     * @param absolutePath Path for databases file location.
     * @param database Name of database.
     * @param tableName Name of table.
     */
    public SQLiteDatabase(String absolutePath, String database, String tableName, String mainKey, String... columns)
    {
        this.absolutePath = absolutePath;
        this.database = database;
        this.tableName = tableName;
        this.mainKey = mainKey;
        this.columns = columns;

        try
        {
            connect();
            createTable();
        }
        catch (SQLException | ClassNotFoundException exception)
        {
            exception.printStackTrace();
        }
    }


    /**
     * Connect to database.
     * @throws SQLException Expected to throw if wrong parameters were entered or host is not up.
     * @throws ClassNotFoundException Not expected to throw.
     */
    @Override
    public void connect() throws SQLException, ClassNotFoundException
    {
        String path = absolutePath.isEmpty() ? "" : absolutePath + File.separator;
        File file = new File(path + database + ".db");
        if (!file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException exception)
            {
                exception.printStackTrace();
            }
        }

        this.connection = DriverManager.getConnection("jdbc:sqlite:" + path + database + ".db");
    }

    /**
     * Disconnect from database.
     * @throws SQLException Expected to throw if host is no longer up.
     */
    @Override
    public void close() throws SQLException
    {
        if (connection != null && !connection.isClosed())
        {
            connection.close();
        }
    }

    /**
     * Update database with series of statements.
     * @param sqls Series of queries to update in database.
     */
    @Override
    public void update(String... sqls)
    {
        try
        {
            Statement statement = connection.createStatement();
            for (String sql : sqls)
            {
                statement.executeUpdate(sql);
            }
            statement.close();
        }
        catch (SQLException exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * Retrieve object from database.
     * @param id Identifier to retrieve from database.
     * @return Object from database.
     */
    @Override
    public T query(String id)
    {
        T t = null;

        try
        {
            String sql = "SELECT * FROM " + tableName + " WHERE " + mainKey + " = '" + id + "'";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            t = load(result);
            result.close();
            statement.close();
        }
        catch (SQLException exception)
        {
            exception.printStackTrace();
        }

        return t;
    }

    /**
     * Create table for the first time.
     */
    @Override
    public void createTable()
    {
        String labels = String.join(", ", columns);
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + labels + ")";

        try
        {
            update(sql);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }
}
