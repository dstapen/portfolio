// Copyright (c) 2014 by Daria Stepanova.
package com.dstapen.portfolio.todo.leveldb.planning;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.ReadOptions;
import org.iq80.leveldb.Snapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;

import static com.dstapen.portfolio.todo.boundary.Dependencies.checkDependency;

/**
 * @author Daria Stepanova
 */
@Repository
public class DefaultTaskDAO implements TaskDAO {

    final DB connection;

    @Inject
    public DefaultTaskDAO(final DB connection) {
        this.connection = checkDependency(connection);
    }

    @Override
    public View seek(String principal) {
        Snapshot snapshot = connection.getSnapshot();
        DBIterator iterator = connection.iterator(readOptions(snapshot));
        iterator.seek(principal.getBytes(StandardCharsets.UTF_8));
        return new View(snapshot, iterator);
    }

    @Override
    public void put(String principal, String title, byte[] completed) {
        connection.put(key(principal, title), completed);
    }

    @Override
    public void delete(String principal, String title) {
        connection.delete(key(principal, title));
    }

    private static ReadOptions readOptions(Snapshot snapshot) {
        ReadOptions rv = new ReadOptions();
        rv.snapshot(snapshot);
        return rv;
    }

    private static byte[] key(String principal, String title)
    {
        return (principal + SPLITTER + title).getBytes(StandardCharsets.UTF_8);
    }
}
