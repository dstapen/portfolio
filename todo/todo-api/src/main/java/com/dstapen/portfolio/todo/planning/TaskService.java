// Copyright (c) 2014 by Daria Stepanova.
package com.dstapen.portfolio.todo.planning;


import java.util.List;

/**
 * @author Daria Stepanova
 */
public interface TaskService {
    List<Task> retrieveAllTasks(String principal);

    List<Task> addTask(Task task, String principal);

    List<Task> updateTask(Task task, String principal);

    List<Task> deleteTask(Task task, String principal);

    void deleteAllTasks(String principal);

    List<Task> clearCompleted(String principal);
}
