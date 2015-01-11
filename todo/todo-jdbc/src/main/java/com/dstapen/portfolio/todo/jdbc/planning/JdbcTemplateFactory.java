// Copyright (c) 2014 by Daria Stepanova.
package com.dstapen.portfolio.todo.jdbc.planning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static com.dstapen.portfolio.todo.boundary.Dependencies.checkDependency;
import static com.google.common.base.Preconditions.checkState;

/**
 * @author Daria Stepanova
 */
public class JdbcTemplateFactory {

    final DataSource dataSource;

    @Autowired
    public JdbcTemplateFactory(final DataSource dataSource) {
        checkDependency(this.dataSource = dataSource);
    }

    public JdbcTemplate jdbcTemplate() throws Exception {
        return new JdbcTemplate(dataSource);
    }
}
