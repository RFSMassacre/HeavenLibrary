package com.github.rfsmassacre.heavenlibrary.interfaces;

import java.sql.*;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Implement for manipulation of any kind of database.
 */
@SuppressWarnings({"unused"})
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
     * Insert object into database.
     * @param t T.
     * @throws SQLException Exception
     */
    <T> void insert(String mainKey, T t) throws SQLException;

    /**
     * Insert object into database asynchronously.
     * @param t T.
     */
    <T> void insertAsync(String mainKey, T t);

    /**
     * Update object into database.
     * @param t T.
     * @throws SQLException Exception.
     */
    <T> void update(String mainKey, T t) throws SQLException;

    /**
     * Update object into database asynchronously.
     * @param t T.
     */
    <T> void updateAsync(String mainKey, T t);

    /**
     * Query database to retrieve object.
     * @return Object type from database.
     * @throws SQLException Exception.
     */
    <T> T query(String mainKey, Class<T> clazz) throws SQLException;

    /**
     * Query database to retrieve object asynchronously through a callback.
     */
    <T> void queryAsync(String mainKey, Class<T> clazz, Consumer<T> callback);

    /**
     * Delete object from the database.
     */
    <T> void delete(String mainKey, Class<T> clazz) throws SQLException;

    /**
     * Delete object from the database asynchronously.
     */
    <T> void deleteAsync(String mainKey, Class<T> clazz);

    /**
     * Query database to retrieve all objects.
     * @return Set of object types from database.
     * @throws SQLException Exception.
     */
    <T> Set<T> queryAll(Class<T> clazz) throws SQLException;

    /**
     * Query database to retrieve all objects asynchronously through a callback.
     */
    <T> void queryAllAsync(Class<T> clazz, Consumer<Set<T>> callback);

    ResultSet executeQuery(String sql) throws SQLException;

    int executeUpdate(String sql) throws SQLException;
}
