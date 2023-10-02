package com.github.rfsmassacre.heavenlibrary.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Implement for manipulation of any kind of database.
 */
public interface SQLData<T, R>
{
    /**
     * Connect to database.
     * @throws SQLException Exception.
     */
    void connect() throws SQLException, ClassNotFoundException;

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
    void add(T t) throws Exception;

    /**
     * Update object into database.
     * @param t T.
     * @throws SQLException Exception.
     */
    void update(T t) throws SQLException;

    /**
     * Query database to retrieve object.
     * @return Object type from database.
     * @throws SQLException Exception.
     */
    T query(R r, Class<T> clazz) throws SQLException;

    /**
     * Delete object from the database.
     * @param t Object to delete.
     */
    void delete(T t) throws SQLException;

    ResultSet executeQuery(String sql) throws SQLException;

    int executeUpdate(String sql) throws SQLException;
}
