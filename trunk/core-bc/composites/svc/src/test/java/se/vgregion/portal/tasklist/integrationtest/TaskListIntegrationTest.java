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
package se.vgregion.portal.tasklist.integrationtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.vgregion.portal.tasklist.domain.Priority;
import se.vgregion.portal.tasklist.domain.Task;
import se.vgregion.portal.tasklist.services.TaskListService;

/**
 * @author jonas
 * @author david
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:**/applicationContext.xml",
        "classpath:**/testApplicationContext.xml" })
public class TaskListIntegrationTest {

    private static final String USER_ID = "user1";
    @Autowired
    TaskListService taskListService;
    @Autowired
    SimpleJdbcTemplate simpleJdbcTemplate;

    @Before
    public void setUp() {
        simpleJdbcTemplate.getJdbcOperations().execute("DROP TABLE task IF EXISTS");
        simpleJdbcTemplate.getJdbcOperations().execute("DROP SEQUENCE task_sequence IF EXISTS");
        simpleJdbcTemplate.getJdbcOperations().execute(
                "CREATE TABLE task (task_id BIGINT NOT NULL, user_id varchar(10) NOT NULL, "
                        + "description varchar(200) NOT NULL, due_date DATE, priority varchar(10), "
                        + "CONSTRAINT task_id_pk PRIMARY KEY (task_id))");
        simpleJdbcTemplate.getJdbcOperations().execute("CREATE SEQUENCE task_sequence");
    }

    @Test
    public void testTaskManagement() throws ParseException {
        List<Task> tasks = generateTask();
        assertTrue(taskListService.addTask(tasks.get(0)));
        assertTrue(taskListService.addTask(tasks.get(1)));
        List<Task> taskResultList = taskListService.getTaskList(USER_ID);
        assertEquals(tasks.get(0), taskResultList.get(0));
        assertEquals(tasks.get(1), taskResultList.get(1));
    }

    /**
     * @return
     * @throws ParseException
     */
    private List<Task> generateTask() throws ParseException {
        Task task1 = new Task();
        task1.setTaskId(0);
        task1.setDescription("Test description task1");
        task1.setDueDate(createSqlDate());
        task1.setPriority(Priority.HIGH);
        task1.setUserId(USER_ID);

        Task task2 = new Task();
        task2.setTaskId(1);
        task2.setDescription("Test description task2");
        task2.setDueDate(createSqlDate());
        task2.setPriority(Priority.LOW);
        task2.setUserId(USER_ID);

        return Arrays.asList(task1, task2);
    }

    private Date createSqlDate() throws ParseException {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        java.util.Date parseDate = formatter.parse("2009-12-02");
        return new Date(parseDate.getTime());
    }
}
