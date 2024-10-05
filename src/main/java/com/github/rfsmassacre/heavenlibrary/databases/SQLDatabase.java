package com.github.rfsmassacre.heavenlibrary.databases;

import com.github.rfsmassacre.heavenlibrary.interfaces.SQLData;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.regex.Pattern;

@SuppressWarnings({"unused", "CallToPrintStackTrace", "ResultOfMethodCallIgnored"})
public abstract class SQLDatabase implements SQLData
{
    public enum SQLPreset
    {
        CREATE_TABLE("CREATE TABLE IF NOT EXISTS tableName (id VARCHAR(32) PRIMARY KEY, data LONGTEXT)"),
        INSERT("INSERT INTO tableName (id, data) VALUES (?, ?)"),
        SELECT("SELECT data FROM tableName WHERE id = ?"),
        SELECT_ALL("SELECT data FROM tableName"),
        DELETE("DELETE FROM tableName WHERE id = ?");

        private final String sql;

        SQLPreset(String sql)
        {
            this.sql = sql;
        }

        public String parseSQL(Class<?> clazz)
        {
            return sql.replaceAll(Pattern.quote("tableName"), clazz.getSimpleName());
        }
    }

    public static void setupDrivers(File folder, String driverName, String url, String className)
    {
        try
        {
            File driverFile = new File(folder, driverName);
            if (!driverFile.exists())
            {
                driverFile.createNewFile();
                InputStream in = URI.create(url).toURL().openStream();
                Files.copy(in, driverFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            URLClassLoader classLoader = new URLClassLoader(new URL[] { driverFile.toURI().toURL() });
            Class.forName(className, true, classLoader);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
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
}
