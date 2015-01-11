// Copyright (c) 2014 by Daria Stepanova.
package com.dstapen.portfolio.todo.jdbc.planning;


import com.dstapen.portfolio.todo.planning.Task;

import java.util.List;

/**
 * @author Daria Stepanova
 */
public interface TaskDAO {
    void insert(Task task, String principal);
    void update(Task task, String principal);
    void delete(Task task, String principal);
    void deleteAllTasks(String principal);
    void deleteCompleted(String principal);
    List<Task> select(String principal);
}
