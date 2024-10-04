package com.github.rfsmassacre.heavenlibrary.databases;

import com.github.rfsmassacre.heavenlibrary.interfaces.SQLData;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public abstract class SQLDatabase implements SQLData
{
    public enum SQLPreset
    {
        CREATE_TABLE("CREATE TABLE IF NOT EXISTS tableName (id VARCHAR(32) PRIMARY KEY, data JSON)"),
        INSERT("INSERT INTO tableName (name, data) VALUES (?, ?)"),
        SELECT("SELECT data FROM tableName WHERE id = ?"),
        SELECT_ALL("SELECT data FROM tableName"),
        DELETE("DELETE FROM tableName WHERE id = ?");

        private final String sql;

        SQLPreset(String sql)
        {
            this.sql = sql;
        }

        public String parseSQL(String database, Class<?> clazz)
        {
            return sql.replaceAll(Pattern.quote("database"), database)
                    .replaceAll(Pattern.quote("tableName"), clazz.getSimpleName());
        }
    }

    protected Connection connection;

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

    @Override
    public ResultSet executeQuery(String sql) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement.executeQuery();
    }

    @Override
    public int executeUpdate(String sql) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement.executeUpdate();
    }

    public static void downloadDrivers(File outputFile, String driverUrl) throws IOException
    {
        InputStream in = URI.create(driverUrl).toURL().openStream();
        Files.copy(in, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void loadDriver(File file, String className) throws Exception
    {
        try (URLClassLoader classLoader = new URLClassLoader(new URL[] { file.toURI().toURL() },
                System.class.getClassLoader()))
        {
            Driver driver = (Driver) Class.forName(className, true, classLoader).newInstance();
            DriverManager.registerDriver(driver);
        }
    }
}
