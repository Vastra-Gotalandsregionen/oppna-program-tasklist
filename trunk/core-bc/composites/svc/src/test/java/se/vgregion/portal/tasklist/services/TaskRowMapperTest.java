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

import java.sql.Date;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import se.vgregion.portal.tasklist.domain.Priority;
import se.vgregion.portal.tasklist.domain.Task;

/**
 * @author jonas
 * @author david
 */
public class TaskRowMapperTest {

    private static final String USER_ID_1 = "user-1";
    private static final long TASK_ID_1 = 0;
    private static final String DESCRIPTION = "Description 1";
    private static final Date DUE_DATE = new java.sql.Date(new java.util.Date().getTime());
    private static final String PRIORITY = Priority.HIGH.toString();
    private TaskRowMapper taskRowMapper;
    private MockResultSet mockResultSet;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        taskRowMapper = new TaskRowMapper();
        mockResultSet = new MockResultSet();
        mockResultSet.getResultMap().put("user_id", USER_ID_1);
        mockResultSet.getResultMap().put("task_id", TASK_ID_1);
        mockResultSet.getResultMap().put("description", DESCRIPTION);
        mockResultSet.getResultMap().put("due_date", DUE_DATE);
        mockResultSet.getResultMap().put("priority", PRIORITY);
    }

    /**
     * Test method for {@link se.vgregion.portal.tasklist.services.TaskRowMapper#mapRow(java.sql.ResultSet, int)}.
     * 
     * @throws SQLException
     */
    @Test
    public void testMapRow() throws SQLException {
        Task mapRow = taskRowMapper.mapRow(mockResultSet, 1);
        assertEquals(USER_ID_1, mapRow.getUserId());
        assertEquals(TASK_ID_1, mapRow.getTaskId());
        assertEquals(DESCRIPTION, mapRow.getDescription());
        assertEquals(DUE_DATE, mapRow.getDueDate());
        assertEquals(PRIORITY, mapRow.getPriority().toString());
    }
}
