package com.github.rfsmassacre.heavenlibrary.databases;

import java.io.File;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

/**
 * Handle H2 databases.
 * @param <T> Object type to store or query.
 */
@SuppressWarnings({"unused"})
public abstract class H2Database<T, R> extends SQLDatabase<T, R>
{
    static
    {
        try
        {
            Class.forName ("org.h2.Driver");
        }
        catch (ClassNotFoundException exception)
        {
            exception.printStackTrace();
        }
    }

    //Database information.
    private final String absolutePath;
    private final String database;
    protected String mainKey;
    protected List<String> columns; //This is assumed they are properly formatted.

    /**
     * Save database while instantiating.
     * @param absolutePath Path for databases file location.
     * @param database Name of database.
     */
    public H2Database(String absolutePath, String database, String mainKey, String... columns)
    {
        this.absolutePath = absolutePath;
        this.database = database;
        this.mainKey = mainKey;
        this.columns = Arrays.asList(columns);

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

        this.connection = DriverManager.getConnection("jdbc:h2:" + path + database);
    }
}
