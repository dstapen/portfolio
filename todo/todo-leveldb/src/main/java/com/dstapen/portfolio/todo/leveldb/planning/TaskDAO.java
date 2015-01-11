// Copyright (c) 2014 by Daria Stepanova.
package com.dstapen.portfolio.todo.leveldb.planning;

/**
 * @author Daria Stepanova
 */
public interface TaskDAO {
    String SPLITTER = "|";
    byte[] FALSE_PRESENTATION = {(byte) 0};
    byte[] TRUE_PRESENTATION = {(byte) 1};

    View seek(String principal);

    void put(String principal, String title, byte[] completed);

    void delete(String principal, String title);


}
