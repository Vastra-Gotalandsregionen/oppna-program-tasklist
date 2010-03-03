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

package se.vgregion.portal.tasklist.integrationtest;

import static org.junit.Assert.*;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.SimpleJdbcTestUtils;

import se.vgregion.portal.tasklist.domain.Priority;
import se.vgregion.portal.tasklist.domain.Status;
import se.vgregion.portal.tasklist.domain.Task;
import se.vgregion.portal.tasklist.services.TaskListService;

/**
 * @author jonas liljenfeldt
 * @author david bennehult
 */
@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class TaskListIntegrationTest {

    private static final String USER_ID = "user1";
    @Autowired
    TaskListService taskListService;

    @Autowired
    SimpleJdbcTemplate simpleJdbcTemplate;

    @After
    public void cleanTable() {
        SimpleJdbcTestUtils.deleteFromTables(simpleJdbcTemplate, "vgr_task");
    }

    @Test
    public void testAddAndGetTask() throws ParseException {
        List<Task> tasks = generateTask();
        assertTrue(taskListService.addTask(tasks.get(0)));
        assertTrue(taskListService.addTask(tasks.get(1)));
        List<Task> taskResultList = taskListService.getTaskList(USER_ID);
        assertEquals(tasks.get(0), taskResultList.get(0));
        assertEquals(tasks.get(1), taskResultList.get(1));
    }

    @Test
    public void testDeleteTask() throws ParseException {
        Task task = generateTask().get(0);
        assertTrue(taskListService.addTask(task));
        List<Task> taskList = taskListService.getTaskList(task.getUserId());
        assertEquals(1, taskList.size());
        assertTrue(taskListService.deleteTask(task.getTaskId()));
        taskList = taskListService.getTaskList(task.getUserId());
        assertEquals(0, taskList.size());
    }

    @Test
    public void testUpdateTask() throws ParseException {
        Task task = generateTask().get(0);
        assertTrue(taskListService.addTask(task));
        task.setDescription("updated description");
        task.setDueDate(createSqlDate("2009-12-03"));
        task.setPriority(Priority.LOW);
        task.setStatus(Status.CLOSED);
        String newUserId = "newUser";
        task.setUserId(newUserId);
        assertTrue(taskListService.updateTask(task));
        List<Task> taskList = taskListService.getTaskList(task.getUserId());
        assertEquals(task.getDescription(), taskList.get(0).getDescription());
        assertEquals(task.getDueDate().getTime(), taskList.get(0).getDueDate().getTime());
        assertEquals(task.getPriority(), taskList.get(0).getPriority());
        assertEquals(task.getStatus(), taskList.get(0).getStatus());
        assertEquals(task.getUserId(), taskList.get(0).getUserId());
    }

    /**
     * @return
     * @throws ParseException
     */
    private List<Task> generateTask() throws ParseException {
        Task task1 = new Task();
        task1.setTaskId(0);
        task1.setDescription("Test description task1");
        task1.setDueDate(createSqlDate("2009-12-02"));
        task1.setPriority(Priority.HIGH);
        task1.setStatus(Status.OPEN);
        task1.setUserId(USER_ID);

        Task task2 = new Task();
        task2.setTaskId(1);
        task2.setDescription("Test description task2");
        task2.setDueDate(createSqlDate("2009-12-02"));
        task2.setPriority(Priority.LOW);
        task2.setStatus(Status.CLOSED);
        task2.setUserId(USER_ID);

        return Arrays.asList(task1, task2);
    }

    private Date createSqlDate(String dateString) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        java.util.Date parseDate = formatter.parse(dateString);
        return new Date(parseDate.getTime());
    }
}
