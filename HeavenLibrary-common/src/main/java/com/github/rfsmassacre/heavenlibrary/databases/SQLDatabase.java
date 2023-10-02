package com.github.rfsmassacre.heavenlibrary.databases;

import com.github.rfsmassacre.heavenlibrary.interfaces.SQLData;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract class SQLDatabase<T, R> implements SQLData<T, R>
{
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
