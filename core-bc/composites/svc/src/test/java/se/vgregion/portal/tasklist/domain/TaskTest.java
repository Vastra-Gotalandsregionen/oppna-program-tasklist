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
package se.vgregion.portal.tasklist.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Date;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jonas
 * @author david
 * 
 */
public class TaskTest {

    private Task task1;
    private Task task2;
    private Task task3;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        Date date = new Date(new java.util.Date().getTime());

        task1 = new Task();
        task1.setTaskId(0);
        task1.setUserId("user-1");
        task1.setDescription("description 1");
        task1.setDueDate(date);
        task1.setPriority(Priority.LOW);
        task1.setStatus(Status.OPEN);

        task2 = new Task();
        task2.setTaskId(0);
        task2.setUserId("user-1");
        task2.setDescription("description 1");
        task2.setDueDate(date);
        task2.setPriority(Priority.LOW);
        task2.setStatus(Status.OPEN);

        task3 = new Task();
        task3.setTaskId(1);
        task3.setUserId("user-2");
        task3.setDescription("description 2");
        task3.setDueDate(date);
        task3.setPriority(Priority.LOW);
        task3.setStatus(Status.CLOSED);
    }

    /**
     * Test method for {@link se.vgregion.portal.tasklist.domain.Task#hashCode()}.
     */
    @Test
    public void testHashCode() {
        assertEquals(generateHashCode(task1), task1.hashCode());
    }

    /**
     * Test method for {@link se.vgregion.portal.tasklist.domain.Task#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject() {
        assertTrue(task1.equals(task2));
        assertTrue(task1.equals(task1));
    }

    /**
     * Test method for {@link se.vgregion.portal.tasklist.domain.Task#equals(java.lang.Object)}.
     */
    @Test
    public void testNotEqualsObject() {
        assertFalse(task1.equals(task3));
        assertFalse(task1.equals(null));
        assertFalse(task1.equals(new String()));
    }

    private int generateHashCode(Task task) {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(task.getTaskId());
        builder.append(task.getUserId());
        builder.append(task.getDescription());
        builder.append(task.getDueDate().getTime());
        builder.append(task.getPriority());
        builder.append(task.getStatus());
        return builder.toHashCode();
    }

}
