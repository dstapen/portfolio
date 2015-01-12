// Copyright (c) 2014 by Daria Stepanova.
package com.dstapen.portfolio.todo.todo;

import com.dstapen.portfolio.todo.planning.Task;
import com.dstapen.portfolio.todo.planning.TaskService;
import com.google.common.base.MoreObjects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.dstapen.portfolio.todo.boundary.Dependencies.checkDependency;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author Daria Stepanova
 */
@Controller
@RequestMapping("todos")
public class DefaultTodoController {

    final TaskService service;

    @Autowired
    public DefaultTodoController(final TaskService service) {
        checkDependency(this.service = service);
    }

    @ResponseBody
    @RequestMapping(method = GET)
    @ResponseStatus(OK)
    public List<Task> retrieveAllTasks(Principal principal) throws Exception {
        return service.retrieveAllTasks(principal.getName());
    }

    @ResponseBody
    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    public List<Task> addTask(@RequestBody Task task, Principal principal) throws Exception {
        return service.addTask(task, principal.getName());
    }

    @ResponseBody
    @RequestMapping(method = PUT)
    @ResponseStatus(OK)
    public List<Task> updateTask(@RequestBody Task task, Principal principal) throws Exception {
        return service.updateTask(task, principal.getName());
    }

    @ResponseBody
    @RequestMapping(method = DELETE)
    @ResponseStatus(OK)
    public List<Task> deleteTask(@RequestBody Task task, Principal principal) throws Exception {
        return service.deleteTask(task, principal.getName());
    }

    @ResponseBody
    @RequestMapping(method = DELETE, value = "all")
    @ResponseStatus(OK)
    public void deleteAllTasks(Principal principal) throws Exception {
        service.deleteAllTasks(principal.getName());
    }

    @ResponseBody
    @RequestMapping(method = DELETE, value = "completed")
    @ResponseStatus(OK)
    public List<Task> clearCompleted(Principal principal) throws Exception {
        return service.clearCompleted(principal.getName());
    }
}
