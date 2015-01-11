// Copyright (c) 2014 by Daria Stepanova.
package com.dstapen.portfolio.todo.leveldb.planning;

import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Snapshot;

import java.io.IOException;
import java.util.Iterator;

import static com.dstapen.portfolio.todo.boundary.Dependencies.checkDependency;
import static java.util.Map.Entry;

/**
 * @author Daria Stepanova
 */
public class View implements Iterator<Entry<byte[], byte[]>>, AutoCloseable {

    final Snapshot snapshot;
    final DBIterator iterator;

    View(final Snapshot snapshot, final DBIterator iterator) {
        this.snapshot = checkDependency(snapshot);
        this.iterator = checkDependency(iterator);
    }

    @Override
    public void close() throws IOException {
        iterator.close();
        snapshot.close();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Entry<byte[], byte[]> next() {
        return iterator.next();
    }
}
