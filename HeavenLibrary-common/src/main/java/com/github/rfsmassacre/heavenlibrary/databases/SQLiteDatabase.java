package com.github.rfsmassacre.heavenlibrary.databases;


import java.io.File;
import java.io.IOException;
import java.sql.*;

@SuppressWarnings({"unused"})
public abstract class SQLiteDatabase<T, R> extends SQLDatabase<T, R>
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
    private final String absolutePath;
    private final String database;

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
}
