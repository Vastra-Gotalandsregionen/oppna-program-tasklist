/**
 * Copyright 2010 Västra Götalandsregionen
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
 *
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
    private static final String SQL_INSERT_TASK = "INSERT INTO vgr_task "
            + "(task_id, user_id, description, due_date, priority, status) values (?, ?, ?, ?, ?, ?)";
    private static final String SQL_GET_TASK = "SELECT task_id, user_id, description, due_date, priority, status "
            + "FROM vgr_task WHERE user_id = ?";
    private static final String SQL_DELETE_TASK = "DELETE FROM vgr_task WHERE task_id = ?";
    private static final String SQL_UPDATE_TASK = "UPDATE vgr_task SET "
            + "user_id = ?, description = ?, due_date = ?, priority = ?, status = ? WHERE task_id = ?";
    private SimpleJdbcTemplate simpleJdbcTemplate = null;
    private DataFieldMaxValueIncrementer dataFieldMaxValueIncrementer = null;
    private TaskRowMapper taskRowMapper = null;

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
        List<Task> tasks = simpleJdbcTemplate.query(SQL_GET_TASK, getTaskRowMapper(), userId);
        return tasks;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addTask(Task task) {
        long nextTaskId = dataFieldMaxValueIncrementer.nextLongValue();
        int updatedRows = simpleJdbcTemplate.update(SQL_INSERT_TASK, nextTaskId, task.getUserId(), task
                .getDescription(), task.getDueDate(), task.getPriority().toString(), task.getStatus().toString());
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

    /**
     *{@inheritDoc}
     */
    @Override
    public boolean deleteTask(long taskId) {
        int affectedRows = simpleJdbcTemplate.update(SQL_DELETE_TASK, taskId);
        return (affectedRows > 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateTask(Task task) {
        int affectedRows = simpleJdbcTemplate.update(SQL_UPDATE_TASK, task.getUserId(), task.getDescription(),
                task.getDueDate(), task.getPriority().toString(), task.getStatus().toString(), task.getTaskId());
        return (affectedRows > 0);
    }
}
