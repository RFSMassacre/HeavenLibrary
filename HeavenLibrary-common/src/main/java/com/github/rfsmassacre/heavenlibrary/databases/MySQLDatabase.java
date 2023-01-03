package com.github.rfsmassacre.heavenlibrary.databases;

import com.github.rfsmassacre.heavenlibrary.interfaces.SQLData;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Handle MySQL databases.
 * @param <T> Object type to store or query.
 */
public abstract class MySQLDatabase<T> extends SQLDatabase<T>
{
    /**
     * Check for database driver only once.
     */
    static
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException exception)
        {
            exception.printStackTrace();
        }
    }

    //Database information.
    private String hostname;
    private String database;
    private String username;
    private String password;
    private int port;
    private boolean ssl;

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
        this.connection = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database +
                "?autoReconnect=true&useSSL=" + Boolean.toString(ssl).toLowerCase(), username, password);
    }
}
