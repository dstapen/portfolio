// Copyright (c) 2014 by Daria Stepanova.
package com.dstapen.portfolio.todo.leveldb.planning;

import org.iq80.leveldb.DB;
import org.springframework.beans.factory.FactoryBean;

import org.iq80.leveldb.*;
import org.springframework.stereotype.Component;

import static org.iq80.leveldb.impl.Iq80DBFactory.*;
import java.io.*;

/**
 * @author Daria Stepanova
 */
@Component
public class ConnectionProvider implements FactoryBean<DB>, AutoCloseable {

    static volatile DB connection = null;

    @Override
    public DB getObject() throws Exception {
        if (connection == null)
        {
            synchronized (ConnectionProvider.this)
            {
                if (connection == null)
                {
                    Options options = new Options();
                    options.createIfMissing(true);
                    File location = new File("storage");
                    location.mkdirs();
                    connection = factory.open(location, options);
                }
            }
        }
        return connection;
    }

    @Override
    public Class<?> getObjectType() {
        return DB.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void close() throws Exception {
        getObject().close();
    }
}
