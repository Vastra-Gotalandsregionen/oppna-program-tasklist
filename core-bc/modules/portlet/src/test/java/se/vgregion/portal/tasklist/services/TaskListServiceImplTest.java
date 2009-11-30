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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import se.vgregion.portal.tasklist.domain.Priority;
import se.vgregion.portal.tasklist.domain.Task;

/**
 *
 */
public class TaskListServiceImplTest {
    private static final String userId = "1";
    private TaskListServiceImpl taskListServiceImpl;
    private MockSimpleJDBCTemplate mockSimpleJdbcTemplate;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

        taskListServiceImpl = new TaskListServiceImpl();
        mockSimpleJdbcTemplate = new TaskListServiceImplTest.MockSimpleJDBCTemplate();
        taskListServiceImpl.setSimpleJdbcTemplate(mockSimpleJdbcTemplate);
    }

    /**
     * Test method for
     * {@link se.vgregion.portal.tasklist.services.TaskListServiceImpl#getTaskList(java.lang.String)}.
     */
    @Test
    public void testGetTaskList() {
        List<Task> taskList = taskListServiceImpl.getTaskList(userId);
        assertEquals("SELECT * FROM task WHERE userId = ?", mockSimpleJdbcTemplate.sql);
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
        task.setDueDate(new Date());
        task.setPriority(Priority.LOW);
        boolean isTaskAdded = taskListServiceImpl.addTask(task);
        assertTrue(isTaskAdded);
    }

    class MockSimpleJDBCTemplate extends SimpleJdbcTemplate {
        String sql;
        RowMapper<Task> rowMapper;
        Object[] queryParameters;
        List<Task> tasks = new ArrayList<Task>();

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

    }

    class MockDataSource implements DataSource {

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

}
