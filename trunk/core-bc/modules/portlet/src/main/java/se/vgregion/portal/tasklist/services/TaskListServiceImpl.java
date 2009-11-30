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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import se.vgregion.portal.tasklist.domain.Task;

/**
 * @author jonas
 * @author david
 * 
 */
public class TaskListServiceImpl implements TaskListService {
    private static final String SQL = "SELECT * FROM task WHERE userId = ?";
    @Autowired
    private SimpleJdbcTemplate simpleJdbcTemplate;
    private RowMapper<Task> taskRowMapper;

    /**
     * @param simpleJdbcTemplate
     *            the simpleJdbcTemplate to set
     */
    public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
        this.simpleJdbcTemplate = simpleJdbcTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> getTaskList(String userId) {
        List<Task> tasks = simpleJdbcTemplate.query(SQL, getTaskRowMapper(), userId);
        return tasks;
    }

    private RowMapper<Task> getTaskRowMapper() {
        if (taskRowMapper == null) {
            taskRowMapper = new TaskRowMapper();
        }
        return taskRowMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addTask(Task task) {
        // TODO Create our own sequence routine (max + 1)
        int updatedRows = simpleJdbcTemplate.update(
                "INSERT INTO task (task_id, user_id, description, due_date, priority) "
                        + "values (NEXT VALUE FOR TASK_SEQUENCE, ?, ?, ?, ?)", task.getUserId(), task
                        .getDescription(), task.getDueDate(), task.getPriority().getPriorityValue());
        boolean isTaskAdded = false;
        if (updatedRows == 1) {
            isTaskAdded = true;
        }
        return isTaskAdded;
    }
}
