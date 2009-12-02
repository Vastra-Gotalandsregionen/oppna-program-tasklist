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
/**
 * 
 */
package se.vgregion.portal.tasklist.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;

import se.vgregion.portal.tasklist.domain.Priority;
import se.vgregion.portal.tasklist.domain.Task;

public class TaskListServiceImplTest {

    private static final String userId = "1";
    private TaskListServiceImpl taskListServiceImpl;
    private MockSimpleJDBCTemplate mockSimpleJdbcTemplate;
    private MockDataFieldMaxValueIncrementer mockDataFieldMaxValueIncrementer;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

        taskListServiceImpl = new TaskListServiceImpl();
        mockDataFieldMaxValueIncrementer = new TaskListServiceImplTest.MockDataFieldMaxValueIncrementer();
        mockSimpleJdbcTemplate = new TaskListServiceImplTest.MockSimpleJDBCTemplate();
        taskListServiceImpl.setSimpleJdbcTemplate(mockSimpleJdbcTemplate);
        taskListServiceImpl.setDataFieldMaxValueIncrementer(mockDataFieldMaxValueIncrementer);
    }

    /**
     * Test method for
     * {@link se.vgregion.portal.tasklist.services.TaskListServiceImpl#getTaskList(java.lang.String)}.
     */
    @Test
    public void testGetTaskList() {
        List<Task> taskList = taskListServiceImpl.getTaskList(userId);
        assertEquals("SELECT task_id, user_id, description, due_date, priority FROM task WHERE user_id = ?",
                mockSimpleJdbcTemplate.sql);
        assertTrue(mockSimpleJdbcTemplate.rowMapper instanceof TaskRowMapper);
        assertEquals("1", mockSimpleJdbcTemplate.queryParameters[0]);
        assertNotNull(taskList);
    }

    /**
     * Test method for {@link se.vgregion.portal.tasklist.services.TaskListServiceImpl#addTask()}.
     */
    @Test
    public void testAddTask() {
        Task task = new Task();
        task.setUserId("user-1");
        task.setDescription("task description");
        task.setDueDate(new Date(new java.util.Date().getTime()));
        task.setPriority(Priority.LOW);
        boolean isTaskAdded = taskListServiceImpl.addTask(task);
        assertTrue(isTaskAdded);
        assertEquals(mockSimpleJdbcTemplate.sql,
                "INSERT INTO task (task_id, user_id, description, due_date, priority) values (?, ?, ?, ?, ?)");
        assertEquals(mockSimpleJdbcTemplate.args[0], mockDataFieldMaxValueIncrementer.counter - 1);
        assertEquals(mockSimpleJdbcTemplate.args[1], task.getUserId());
        assertEquals(mockSimpleJdbcTemplate.args[2], task.getDescription());
        assertEquals(mockSimpleJdbcTemplate.args[3], task.getDueDate());
        assertEquals(mockSimpleJdbcTemplate.args[4], task.getPriority().toString());
    }

    @Test
    public void testAddTaskNoRowAdded() {
        mockSimpleJdbcTemplate.affectedRows = 0;
        boolean isTaskAdded = taskListServiceImpl.addTask(new Task());
        assertFalse(isTaskAdded);
        mockSimpleJdbcTemplate.affectedRows = 1;
    }

    @Test
    public void testUpdateTask() {
        Task task = new Task();
        task.setDescription("description");
        task.setDueDate(new java.sql.Date(new java.util.Date().getTime()));
        task.setPriority(Priority.HIGH);
        task.setUserId("user-1");

        mockSimpleJdbcTemplate.affectedRows = 1;
        boolean isTaskUpdated = taskListServiceImpl.updateTask(task);
        assertEquals("UPDATE task SET user_id = ?, description = ?, due_date = ?, priority = ? WHERE task_id = ?",
                mockSimpleJdbcTemplate.sql);
        assertEquals(task.getUserId(), mockSimpleJdbcTemplate.args[0]);
        assertEquals(task.getDescription(), mockSimpleJdbcTemplate.args[1]);
        assertEquals(task.getDueDate(), mockSimpleJdbcTemplate.args[2]);
        assertEquals(task.getPriority(), mockSimpleJdbcTemplate.args[3]);
        assertEquals(task.getTaskId(), mockSimpleJdbcTemplate.args[4]);
        assertTrue(isTaskUpdated);
        mockSimpleJdbcTemplate.affectedRows = 0;
    }

    @Test
    public void testUpdateTaskFailed() {
        mockSimpleJdbcTemplate.affectedRows = 0;
        boolean isTaskUpdated = taskListServiceImpl.updateTask(new Task());
        assertFalse(isTaskUpdated);
    }

    @Test
    public void testDeleteTask() {
        long taskId = 0;
        mockSimpleJdbcTemplate.affectedRows = 1;
        boolean isTaskDeleted = taskListServiceImpl.deleteTask(taskId);
        assertEquals("DELETE FROM task WHERE task_id = ?", mockSimpleJdbcTemplate.sql);
        assertEquals(taskId, mockSimpleJdbcTemplate.args[0]);
        assertTrue(isTaskDeleted);
        mockSimpleJdbcTemplate.affectedRows = 0;
    }

    @Test
    public void testDeleteTaskFailed() {
        long taskId = 0;
        mockSimpleJdbcTemplate.affectedRows = 0;
        boolean isTaskDeleted = taskListServiceImpl.deleteTask(taskId);
        assertFalse(isTaskDeleted);
    }

    static class MockSimpleJDBCTemplate extends SimpleJdbcTemplate {
        String sql;
        RowMapper<Task> rowMapper;
        Object[] queryParameters;
        List<Task> tasks = new ArrayList<Task>();
        private Object[] args;
        int affectedRows = 1;

        public MockSimpleJDBCTemplate() {
            super(new MockDataSource());
        }

        /**
         * @inheritDoc
         */
        @Override
        public <T> List<T> query(String sql, RowMapper<T> rm, Object... args) throws DataAccessException {
            this.sql = sql;
            this.rowMapper = (RowMapper<Task>) rm;
            this.queryParameters = args;
            return (List<T>) tasks;
        }

        /**
         * @inheritDoc
         */
        @Override
        public int update(String sql, Object... args) throws DataAccessException {
            this.sql = sql;
            this.args = args;
            return affectedRows;
        }

    }

    static class MockDataSource implements DataSource {

        /**
         * @inheritDoc
         */
        @Override
        public Connection getConnection() throws SQLException {
            throw new UnsupportedOperationException("TODO: Implement this method");
        }

        /**
         * @inheritDoc
         */
        @Override
        public Connection getConnection(String username, String password) throws SQLException {
            throw new UnsupportedOperationException("TODO: Implement this method");
        }

        /**
         * @inheritDoc
         */
        @Override
        public PrintWriter getLogWriter() throws SQLException {
            throw new UnsupportedOperationException("TODO: Implement this method");
        }

        /**
         * @inheritDoc
         */
        @Override
        public int getLoginTimeout() throws SQLException {
            throw new UnsupportedOperationException("TODO: Implement this method");
        }

        /**
         * @inheritDoc
         */
        @Override
        public void setLogWriter(PrintWriter arg0) throws SQLException {
            throw new UnsupportedOperationException("TODO: Implement this method");
        }

        /**
         * @inheritDoc
         */
        @Override
        public void setLoginTimeout(int arg0) throws SQLException {
            throw new UnsupportedOperationException("TODO: Implement this method");
        }

        /**
         * @inheritDoc
         */
        @Override
        public boolean isWrapperFor(Class<?> arg0) throws SQLException {
            throw new UnsupportedOperationException("TODO: Implement this method");
        }

        /**
         * @inheritDoc
         */
        @Override
        public <T> T unwrap(Class<T> arg0) throws SQLException {
            throw new UnsupportedOperationException("TODO: Implement this method");
        }
    }

    static class MockDataFieldMaxValueIncrementer implements DataFieldMaxValueIncrementer {

        long counter = 0;

        /**
         * @inheritDoc
         */
        @Override
        public int nextIntValue() throws DataAccessException {
            throw new UnsupportedOperationException("TODO: Implement this method");
        }

        /**
         * @inheritDoc
         */
        @Override
        public long nextLongValue() throws DataAccessException {
            return counter++;
        }

        /**
         * @inheritDoc
         */
        @Override
        public String nextStringValue() throws DataAccessException {
            throw new UnsupportedOperationException("TODO: Implement this method");
        }
    }
}
