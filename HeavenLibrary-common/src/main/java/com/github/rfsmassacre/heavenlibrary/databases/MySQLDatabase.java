package com.github.rfsmassacre.heavenlibrary.databases;

import com.github.rfsmassacre.heavenlibrary.interfaces.SQLData;

import java.sql.*;

/**
 * Handle MySQL databases.
 * @param <T> Object type to store or query.
 */
public abstract class MySQLDatabase<T> implements SQLData<T>
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
    protected String tableName;
    private String username;
    private String password;
    private int port;
    private boolean ssl;
    private Connection connection;
    protected String mainKey;
    protected String[] columns; //This is assumed they are properly formatted.

    /**
     * Save database while instantiating.
     * @param hostName Address where database is hosted.
     * @param database Name of database.
     * @param tableName Name of table.
     * @param username Username to access database.
     * @param password Password to access database.
     * @param port Port number of database.
     * @param ssl Use a secured connection.
     */
    public MySQLDatabase(String hostName, String database, String tableName, String username, String password,
                         int port, boolean ssl, String mainKey, String... columns)
    {
        this.hostname = hostName;
        this.database = database;
        this.tableName = tableName;
        this.username = username;
        this.password = password;
        this.port = port;
        this.ssl = ssl;
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
        this.connection = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database +
                "?autoReconnect=true&useSSL=" + Boolean.toString(ssl).toLowerCase(), username, password);
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

    public void preparedUpdate(PreparedStatement statement)
    {
        try
        {
            statement.executeUpdate();
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
