package com.github.rfsmassacre.heavenlibrary.databases;

import com.github.rfsmassacre.heavenlibrary.interfaces.SQLData;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

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
    private Connection connection;

    /**
     * Save database while instantiating.
     * @param absolutePath Path for databases file location.
     * @param database Name of database.
     */
    public SQLiteDatabase(String absolutePath, String database)
    {
        this.absolutePath = absolutePath;
        this.database = database;

        try
        {
            connect();
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
     * @param sql SQL statement.
     * @return Object from database.
     */
    @Override
    public List<T> query(String sql)
    {
        List<T> t = null;

        try
        {
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

    @Override
    public void createTable(String tableName, String... columns)
    {
        if (columns.length > 0)
        {
            String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + String.join(", ",
                    Arrays.asList(columns)) + ")";
            update(sql);
        }
    }
}
