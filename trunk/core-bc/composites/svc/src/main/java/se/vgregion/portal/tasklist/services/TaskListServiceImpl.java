/**
 * Copyright 2009 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 */
package se.vgregion.portal.tasklist.services;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;

import se.vgregion.portal.tasklist.domain.Task;

/**
 * Implementation of task list service.
 * 
 * @author jonas
 * @author david
 * 
 */
public class TaskListServiceImpl implements TaskListService {
    private static final String SQL = "SELECT task_id, user_id, description, due_date, priority "
            + "FROM task WHERE user_id = ?";
    private SimpleJdbcTemplate simpleJdbcTemplate;
    private DataFieldMaxValueIncrementer dataFieldMaxValueIncrementer;
    private TaskRowMapper taskRowMapper;

    /**
     * @param simpleJdbcTemplate
     *            the simpleJdbcTemplate to set
     */
    public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
        this.simpleJdbcTemplate = simpleJdbcTemplate;
    }

    /**
     * @param dataFieldMaxValueIncrementer
     *            the dataFieldMaxValueIncrementer to set
     */
    public void setDataFieldMaxValueIncrementer(DataFieldMaxValueIncrementer dataFieldMaxValueIncrementer) {
        this.dataFieldMaxValueIncrementer = dataFieldMaxValueIncrementer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> getTaskList(String userId) {
        List<Task> tasks = simpleJdbcTemplate.query(SQL, getTaskRowMapper(), userId);
        return tasks;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addTask(Task task) {
        long nextTaskId = dataFieldMaxValueIncrementer.nextLongValue();
        int updatedRows = simpleJdbcTemplate
                .update("INSERT INTO task (task_id, user_id, description, due_date, priority) "
                        + "values (?, ?, ?, ?, ?)", nextTaskId, task.getUserId(), task.getDescription(), task
                        .getDueDate(), task.getPriority().toString());
        boolean isTaskAdded = false;
        if (updatedRows == 1) {
            isTaskAdded = true;
        }
        return isTaskAdded;
    }

    private TaskRowMapper getTaskRowMapper() {
        if (taskRowMapper == null) {
            taskRowMapper = new TaskRowMapper();
        }
        return taskRowMapper;
    }
}
