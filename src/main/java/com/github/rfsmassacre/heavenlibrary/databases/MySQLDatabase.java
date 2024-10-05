package com.github.rfsmassacre.heavenlibrary.databases;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;
import java.util.function.Consumer;


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

    public abstract <T> void queryAllAsync(Class<T> clazz, Consumer<Set<T>> callback);
}
