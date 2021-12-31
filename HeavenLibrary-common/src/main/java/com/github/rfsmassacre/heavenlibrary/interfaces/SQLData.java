package com.github.rfsmassacre.heavenlibrary.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implement for manipulation of any kind of database.
 */
public interface SQLData<T>
{
    /**
     * Connect to database.
     * @throws Exception Generic database exception.
     */
    void connect() throws Exception;

    /**
     * Disconnect from database.
     * @throws Exception Generic database exception.
     */
    void close() throws Exception;

    /**
     * Update database with series of statements.
     * @param sqls Series of queries to update in database.
     * @throws Exception Generic database exception.
     */
    void update(String... sqls) throws Exception;

    /**
     * Query database to retrieve object .
     * @param id Identifier to retrieve object from database.
     * @return Object type from database.
     * @throws Exception Generic database exception.
     */
    T query(String id) throws Exception;

    /**
     * Create table to the database.
     */
    void createTable();

    /**
     * Deconstruct object to save to database.
     * @param t Object to save.
     */
    void save(T t);

    /**
     * Construct object from result set. (To be used exclusively on the query function.)
     * @param result Result from query.
     * @return Object from database.
     */
    T load(ResultSet result) throws SQLException;

    /**
     * Delete object from the database.
     * @param t Object to delete.
     */
    void delete(T t);
}
