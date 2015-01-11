// Copyright (c) 2014 by Daria Stepanova.
package com.dstapen.portfolio.todo.jdbc.planning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

import static com.dstapen.portfolio.todo.boundary.Dependencies.checkDependency;

/**
 * @author Daria Stepanova
 */
public class TxTemplateFactory {

    final PlatformTransactionManager transactionManager;


    @Autowired
    public TxTemplateFactory(final PlatformTransactionManager transactionManager) {
        checkDependency(this.transactionManager = transactionManager);
    }

    public TransactionTemplate txTemplate() {
        return new TransactionTemplate(transactionManager);
    }
}
