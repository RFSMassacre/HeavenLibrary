package com.github.rfsmassacre.heavenlibrary.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implement for manipulation of any kind of database.
 */
public interface SQLData
{
    /**
     * Connect to database.
     * @throws SQLException Exception.
     */
    void connect() throws SQLException;

    /**
     * Disconnect from database.
     * @throws SQLException Exception.
     */
    void close() throws SQLException;

    /**
     * Add object into database.
     * @param t T.
     * @throws SQLException Exception
     */
    <T> void add(String mainKey, T t) throws SQLException;

    /**
     * Update object into database.
     * @param t T.
     * @throws SQLException Exception.
     */
    <T> void update(String mainKey, T t) throws SQLException;

    /**
     * Query database to retrieve object.
     * @return Object type from database.
     * @throws SQLException Exception.
     */
    <T> T query(String mainKey, Class<T> clazz) throws SQLException;

    /**
     * Delete object from the database.
     */
    <T> void delete(String mainKey, Class<T> clazz) throws SQLException;

    ResultSet executeQuery(String sql) throws SQLException;

    int executeUpdate(String sql) throws SQLException;
}
