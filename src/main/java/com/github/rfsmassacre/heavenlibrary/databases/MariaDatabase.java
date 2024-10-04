package com.github.rfsmassacre.heavenlibrary.databases;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Handle MySQL databases.
 */
@SuppressWarnings({"unused", "CallToPrintStackTrace"})
public abstract class MariaDatabase extends SQLDatabase
{
    public static final String DRIVER_URL = "https://repo1.maven.org/maven2/org/mariadb/jdbc/mariadb-java-client/3.4.1/mariadb-java-client-3.4.1.jar";
    public static final String DRIVER = "mariadb-java-client-3.4.1.jar";
    public static final String CLASS_NAME = "org.mariadb.jdbc.Driver";

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
    public MariaDatabase(String hostName, String database, String username, String password, int port, boolean ssl)
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
            String sql = "USE " + database;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
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
        this.connection = DriverManager.getConnection("jdbc:mariadb://" + hostname + ":" + port + "/" + database,
                username, password);
    }
}
