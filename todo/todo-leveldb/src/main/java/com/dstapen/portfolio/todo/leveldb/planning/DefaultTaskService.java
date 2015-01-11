// Copyright (c) 2014 by Daria Stepanova.
package com.dstapen.portfolio.todo.leveldb.planning;

import com.dstapen.portfolio.todo.planning.Task;
import com.dstapen.portfolio.todo.planning.TaskService;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Snapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.base.Strings.repeat;
import static java.util.Map.Entry;

import static com.dstapen.portfolio.todo.boundary.Dependencies.checkDependency;

/**
 * @author Daria Stepanova
 */
@Service
public class DefaultTaskService implements TaskService {

    static boolean decode(byte[] in)
    {
        return Arrays.equals(TaskDAO.TRUE_PRESENTATION, in);
    }

    static byte[] encode(boolean in)
    {
        return in ? TaskDAO.TRUE_PRESENTATION : TaskDAO.FALSE_PRESENTATION;
    }

    final TaskDAO dao;

    @Autowired
    public DefaultTaskService(final TaskDAO dao) {
        this.dao = checkDependency(dao);
    }

    @Override
    public List<Task> retrieveAllTasks(String principal) {
        checkArgument(!isNullOrEmpty(principal));
        try (View view = dao.seek(principal))
        {
            List<Task> list = new ArrayList<>();
            Task.Builder builder = Task.builder();
            stream(view).map(new Function<Entry<byte[], byte[]>, Optional<Task>>() {

                @Override
                public Optional<Task> apply(Entry<byte[], byte[]> entry) {
                    String key = new String(entry.getKey(), StandardCharsets.UTF_8);
                    String[] result = key.split(TaskDAO.SPLITTER, 2);
                    return (principal.equals(result[0])) ? Optional.of(builder.completed(decode(entry.getValue())).title(result[1]).build()) : Optional.empty();
                }
            }).filter(task -> task.isPresent())
            .map(optional -> optional.get()).forEach(list::add);
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Stream<Entry<byte[],byte[]>> stream(View view)
    {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(view, Spliterator.ORDERED), false);
    }

    @Override
    public List<Task> addTask(Task task, String principal) {
        dao.put(principal, task.title, encode(task.completed));
        return retrieveAllTasks(principal);
    }

    @Override
    public List<Task> updateTask(Task task, String principal) {
        dao.put(principal, task.title, encode(task.completed));
        return retrieveAllTasks(principal);
    }

    @Override
    public List<Task> deleteTask(Task task, String principal) {
        dao.delete(principal, task.title);
        return retrieveAllTasks(principal);
    }

    @Override
    public void deleteAllTasks(String principal) {
        retrieveAllTasks(principal).stream().forEach(new Consumer<Task>() {
            @Override
            public void accept(Task task) {
                dao.delete(principal, task.title);
            }
        });
    }

    @Override
    public List<Task> clearCompleted(String principal) {
        retrieveAllTasks(principal).stream()
                .filter(task -> !task.completed)
                .forEach(new Consumer<Task>() {
                    @Override
                    public void accept(Task task) {
                        dao.delete(principal, task.title);
                    }
                });
        return retrieveAllTasks(principal);
    }
}
