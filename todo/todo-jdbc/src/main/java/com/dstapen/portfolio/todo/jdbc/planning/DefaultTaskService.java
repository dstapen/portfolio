// Copyright (c) 2014 by Daria Stepanova.
package com.dstapen.portfolio.todo.jdbc.planning;

import com.dstapen.portfolio.todo.planning.Task;
import com.dstapen.portfolio.todo.planning.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import java.util.List;

/**
 * @author Daria Stepanova
 */
@Service
public class DefaultTaskService implements TaskService {

    final TaskDAO dao;
    final TxTemplateFactory txTemplateFactory;

    @Autowired
    public DefaultTaskService(final TaskDAO dao, final TxTemplateFactory txTemplateFactory ) {
        this.dao = dao;
        this.txTemplateFactory = txTemplateFactory;
    }

    @Override
    public List<Task> retrieveAllTasks(String principal) {
        return dao.select(principal);
    }

    @Override
    public List<Task> addTask(Task task, String principal) {
        return txTemplateFactory.txTemplate().execute(transactionStatus -> doTaskCreation(task, principal));
    }

    @Override
    public List<Task> updateTask(Task task, String principal) {
        return txTemplateFactory.txTemplate().execute(transactionStatus -> doTaskUpdate(task, principal));
    }

    @Override
    public List<Task> deleteTask(Task task, String principal) {
        return txTemplateFactory.txTemplate().execute(transactionStatus -> doTaskDelete(task, principal));
    }

    @Override
    public void deleteAllTasks(String principal) {
        txTemplateFactory.txTemplate().execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                try {
                    dao.deleteAllTasks(principal);
                } catch (Exception e) {
                    transactionStatus.setRollbackOnly();
                }
            }
        });
    }

    @Override
    public List<Task> clearCompleted(String principal) {
        return txTemplateFactory.txTemplate().execute(transactionStatus -> doTaskClearCompleted(principal));
    }

    private List<Task> doTaskCreation(Task task, String principal)
    {
        dao.insert(task, principal);
        return dao.select(principal);
    }

    private List<Task> doTaskUpdate(Task task, String principal)
    {
        dao.update(task, principal);
        return dao.select(principal);
    }

    private List<Task> doTaskDelete(Task task, String principal)
    {
        dao.delete(task, principal);
        return dao.select(principal);
    }

    private List<Task> doTaskClearCompleted(String principal){
        dao.deleteCompleted(principal);
        return dao.select(principal);
    }
}
