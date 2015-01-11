// Copyright (c) 2014 by Daria Stepanova.
package com.dstapen.portfolio.todo.jdbc.planning;

import com.dstapen.portfolio.todo.planning.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Daria Stepanova
 */
@Service
public class DefaultTaskDAO implements TaskDAO {

    static final String SQL_INSERT_ONE_TODO = "insert into TODO_LIST (TITLE, COMPLETED, PRINCIPAL) VALUES (?,?,?)";
    static final String SQL_SELECT_ALL_TODOS = "select T.TITLE, T.COMPLETED from TODO_LIST T WHERE PRINCIPAL=?";
    static final String SQL_UPDATE_ONE = "update TODO_LIST set COMPLETED=? where TITLE=? AND PRINCIPAL=?";
    static final String SQL_DELETE_ONE = "delete from TODO_LIST where TITLE = ? AND PRINCIPAL=?";
    static final String SQL_DELETE_COMPLETED = "delete from TODO_LIST where COMPLETED = true AND PRINCIPAL=?";
    static final String SQL_DELETE_ALL_TASKS = "delete from TODO_LIST where PRINCIPAL=?";

    final JdbcTemplateFactory factory;

    @Autowired
    public DefaultTaskDAO(final JdbcTemplateFactory factory) {
        this.factory = factory;
    }

    @Override
    public List<Task> select(String principal) {
        try {
            return factory.jdbcTemplate().query(SQL_SELECT_ALL_TODOS, new RowMapper<Task>() {

                @Override
                public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return Task.builder()
                            .title(rs.getString(1))
                            .completed(rs.getBoolean(2))
                            .build();
                }
            }, principal);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void insert(Task task, String principal) {
        try {
            factory.jdbcTemplate().update(SQL_INSERT_ONE_TODO, task.title, task.completed, principal);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Task task, String principal) {
        try {
            factory.jdbcTemplate().update(SQL_UPDATE_ONE, task.completed, task.title, principal);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
     public void delete(Task task, String principal) {
        try {
            factory.jdbcTemplate().update(SQL_DELETE_ONE, task.title, principal);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllTasks(String principal) {
        try {
            factory.jdbcTemplate().update(SQL_DELETE_ALL_TASKS, principal);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCompleted(String principal)  {
        try {
            factory.jdbcTemplate().update(SQL_DELETE_COMPLETED, principal);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
